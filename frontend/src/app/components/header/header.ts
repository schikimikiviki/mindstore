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

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, FormsModule],
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

  ngAfterViewInit() {
    setTimeout(() => (this.hydrated = true));
  }

  @Output() childEmitter: EventEmitter<Text[]> = new EventEmitter<Text[]>();

  // RxJS subject for search input
  searchTerm$ = new Subject<string>();
  searchTerm: string = '';

  constructor(
    private textService: TextService,
    private cdr: ChangeDetectorRef,
    private dialogRef: MatDialog,
    private authService: AuthService
  ) {}

  ngOnInit() {
    this.textService.getTexts().subscribe((texts) => {
      this.allTexts = texts.content;
      this.filteredTexts = texts.content;
      this.textCount = texts.total;
      this.filteredCount = texts.total;
      this.childEmitter.emit(this.filteredTexts);
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
      });

    // this.textService.getHistory().subscribe((historyStrings) => {
    //   console.log('history:', historyStrings);
    //   this.historyArray = historyStrings;
    //   this.cdr.detectChanges();
    // });
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
}
