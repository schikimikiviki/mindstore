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

import { Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule],
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

  @Output() childEmitter: EventEmitter<Text[]> = new EventEmitter<Text[]>();

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
      this.childEmitter.emit(this.filteredTexts);
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

        this.textService.searchTexts(term).subscribe({
          next: (result) => {
            this.filteredTexts = [...result.content];
            this.filteredCount = result.total;
            this.cdr.markForCheck();
            this.childEmitter.emit(result.content);
          },
          error: (err) => {
            console.error('Search error:', err);
            this.filteredTexts = [];
            this.filteredCount = 0;
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
