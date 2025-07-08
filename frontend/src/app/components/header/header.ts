import { TextService } from '../../text/text.service';
import { Text } from '../../text/text.model';
import { CommonModule } from '@angular/common';
import {
  Component,
  OnInit,
  ChangeDetectorRef,
  ChangeDetectionStrategy,
} from '@angular/core';
import { FilterButton } from '../filter-button/filter-button';
import { Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, FilterButton],
  templateUrl: './header.html',
  styleUrls: ['./header.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Header implements OnInit {
  title = 'mindstore';
  allTexts: Text[] = [];
  filteredTexts: Text[] = [];
  loading = true;
  textCount = 0;
  filteredCount = 0;

  // RxJS subject for search input
  searchTerm$ = new Subject<string>();

  constructor(
    private textService: TextService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.textService.getTexts().subscribe((texts) => {
      this.allTexts = texts.content;
      this.filteredTexts = texts.content;
      this.textCount = texts.total;
      this.filteredCount = texts.total;
      this.loading = false;
    });

    this.searchTerm$
      .pipe(
        debounceTime(500), // wait 500ms after last input
        distinctUntilChanged() // ignore same values
      )
      .subscribe((term) => {
        if (!term || term.trim() === '') {
          this.filteredTexts = [...this.allTexts];
          this.filteredCount = this.allTexts.length;
          this.cdr.markForCheck();
          return;
        }

        this.loading = true;
        this.textService.searchTexts(term).subscribe({
          next: (result) => {
            this.filteredTexts = [...result.content];
            this.filteredCount = result.total;
            this.loading = false;
            this.cdr.markForCheck();
          },
          error: (err) => {
            console.error('Search error:', err);
            this.filteredTexts = [];
            this.filteredCount = 0;
            this.loading = false;
            this.cdr.markForCheck();
          },
        });
      });
  }

  onSearchInput(event: Event) {
    const value = (event.target as HTMLInputElement).value;
    this.searchTerm$.next(value); // push new value into the stream
  }
}
