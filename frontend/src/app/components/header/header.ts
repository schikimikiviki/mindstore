import { TextService } from '../../text/text.service';
import { Text } from '../../text/text.model';
import { CommonModule } from '@angular/common';
import {
  Component,
  OnInit,
  ChangeDetectorRef,
  ChangeDetectionStrategy,
  Output,
  EventEmitter,
} from '@angular/core';
import { MatDialog } from '@angular/material/dialog';

import { Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';
import { FormsModule } from '@angular/forms';
import { Popup } from '../popup/popup';
import { AuthService } from '../../services/auth/auth.service';
import { FilterButton } from '../filter-button/filter-button';
import { AddText } from '../add-text/add-text';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, FormsModule, FilterButton],
  templateUrl: './header.html',
  styleUrls: ['./header.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Header implements OnInit {
  title = 'mindstore';
  allTexts: Text[] = [];
  filteredTexts: Text[] = [];
  textCount = 0;
  filteredCount = 0;
  historyArray: string[] = [];
  loggedIn = false;
  hydrated = false;
  tags: string[] = [];
  searchAfter: string | null = null;
  tagsSelected: string[] = [];
  searchTerm$ = new Subject<string>(); // RxJS subject for search input
  searchTerm: string = '';
  tagSearchActivated = false;
  autocompleteArray: string[] = [];

  @Output() childEmitter: EventEmitter<Text[]> = new EventEmitter<Text[]>();
  @Output() searchAfterEmitter = new EventEmitter<string>();

  constructor(
    private textService: TextService,
    private cdr: ChangeDetectorRef,
    private dialogRef: MatDialog,
    private authService: AuthService
  ) {}

  ngAfterViewInit() {
    setTimeout(() => (this.hydrated = true));
  }

  ngOnInit() {
    this.authService.getTags().subscribe((tags) => {
      this.tags = tags.sort();
    });

    this.authService.checkLogin().subscribe((state) => {
      console.log(state);
      this.loggedIn = state;
      this.cdr.markForCheck();
    });

    this.textService.getTexts().subscribe((texts) => {
      this.allTexts = texts.content;
      this.filteredTexts = texts.content;
      this.textCount = texts.total;
      this.filteredCount = texts.total;
      this.childEmitter.emit(this.filteredTexts);
      this.searchAfter = texts.searchAfter;
      this.searchAfterEmitter.emit(this.searchAfter);

      // console.log(texts);
    });

    this.searchTerm$
      .pipe(
        debounceTime(500) // wait 500ms after last input
        // distinctUntilChanged() // ignore same values
      )
      .subscribe((term) => {
        if (!term || term.trim() === '') {
          this.filteredTexts = [...this.allTexts];
          this.filteredCount = this.allTexts.length;
          this.cdr.markForCheck();
          this.childEmitter.emit(this.filteredTexts);
          this.autocompleteArray = [];
          return;
        }

        this.textService.searchTexts(term).subscribe({
          next: (result) => {
            this.filteredTexts = [...result.content];
            this.filteredCount = result.total;
            this.cdr.markForCheck();
            this.childEmitter.emit(result.content);
            this.loadSearchHistory();
          },
          error: (err) => {
            console.error('Search error:', err);
            this.filteredTexts = [];
            this.filteredCount = 0;
            this.cdr.markForCheck();
          },
        });

        // also get the autocomplete results for this

        this.textService.getAutocompletion(term).subscribe({
          next: (result) => {
            // only display if the result does not equal the searched term (otherwise its unnccessary)
            if (result[0] != term) {
              this.autocompleteArray = result;
            }

            this.cdr.markForCheck();
            console.log('autocomplete', result);
          },
          error: (err) => {
            console.error('autocomplete error:', err);
            this.cdr.markForCheck();
          },
        });
      });

    this.loadSearchHistory();
  }

  onSearchInput(event: Event) {
    const value = (event.target as HTMLInputElement).value;
    this.searchTerm$.next(value); // push new value into the stream
  }

  private loadSearchHistory() {
    this.textService.getHistory().subscribe((historyStrings) => {
      this.historyArray = historyStrings;
      this.cdr.markForCheck();
    });
  }

  onClickItem(item: string) {
    // we want to search for that
    // also, display that search string in the bar
    this.searchTerm$.next(item);
    this.searchTerm = item;
    this.loadSearchHistory();
    this.autocompleteArray = []; // reset so that it disappears
  }

  manageLoginOrLogout() {
    // if the user is logged out, log in
    // if the user is logged in, log out
    this.loggedIn ? this.logOutUser() : this.openPopup();
  }

  openPopup() {
    this.dialogRef
      .open(Popup)
      .afterClosed()
      .subscribe((result) => {
        console.log('Dialog closed with:', result);
        if (result === true) {
          this.loggedIn = true;
          this.cdr.detectChanges(); // Force view update
        }
      });
  }

  logOutUser() {
    this.authService.logoutUser().subscribe({
      next: () => {
        console.log('User logged out!');

        this.loggedIn = false;
        this.cdr.detectChanges(); // Force view update
      },
      error: (err) => {
        console.error('Logout error:', err);
      },
    });
  }

  getTagsFromChild(tags: string[]) {
    console.log('Tags received:', tags);

    this.tagsSelected = tags;
    // die neuen Texte fetch &
    // jetzt hier wieder mit der Result Page reden

    this.textService.getTextsWithTags(tags).subscribe({
      next: (result) => {
        this.filteredTexts = [...result.content];
        this.filteredCount = result.total;
        this.cdr.markForCheck();
        this.childEmitter.emit(result.content);
        this.tagSearchActivated = true;
      },
      error: (err) => {
        console.error('Search error:', err);
        this.filteredTexts = [];
        this.filteredCount = 0;
        this.cdr.markForCheck();
      },
    });
  }

  receiveResetMsg(msg: boolean) {
    console.log('Reset received');
    this.textService.getTexts().subscribe((texts) => {
      this.allTexts = texts.content;
      this.filteredTexts = texts.content;
      this.textCount = texts.total;
      this.filteredCount = texts.total;
      this.childEmitter.emit(this.filteredTexts);
      this.tagSearchActivated = false;
    });
  }

  onClickPlus() {
    // open add text component

    console.log('Opening popup with tags: ', this.tags);

    this.dialogRef
      .open(AddText, { data: { availableTags: this.tags } })
      .afterClosed()
      .subscribe((result) => {
        console.log('Dialog closed with:', result);
        this.cdr.detectChanges(); // Force view update
      });
  }

  loadNextPage(searchAfter: string | null) {
    console.log('Header received the emit!', searchAfter);
    if (!searchAfter) return;

    if (this.tagSearchActivated) {
      // when this is true, we want to load the next page but for the specific tag

      this.textService
        .getTextsWithTags(this.tagsSelected, searchAfter)
        .subscribe({
          next: (result) => {
            const newTexts = result.content;
            this.allTexts = [...this.allTexts, ...newTexts];
            this.filteredTexts = this.allTexts;
            this.textCount = result.total;
            this.filteredCount = result.total;
            this.childEmitter.emit(this.filteredTexts);
          },
          error: (err) => {
            console.error('Load more error:', err);
          },
        });
    } else {
      // this loads more content for a normal search
      this.textService.getTexts(searchAfter).subscribe({
        next: (result) => {
          const newTexts = result.content;
          this.allTexts = [...this.allTexts, ...newTexts];
          this.filteredTexts = this.allTexts;
          this.textCount = result.total;
          this.filteredCount = result.total;
          this.childEmitter.emit(this.filteredTexts);
        },
        error: (err) => {
          console.error('Load more error:', err);
        },
      });
    }
  }
}
