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

  constructor(
    private textService: TextService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.textService.getTexts().subscribe((texts) => {
      console.log(ChangeDetectionStrategy);
      console.log('Fetched texts:', texts);
      this.textCount = texts.content?.length;
      this.allTexts = texts.content;
      this.filteredTexts = texts.content;
      this.loading = false;
    });
  }

  getInputValue(event: Event): string {
    return (event.target as HTMLInputElement).value;
  }

  searchBackend(query: string) {
    if (!query || query.trim() === '') {
      this.filteredTexts = [...this.allTexts];
      this.filteredCount = this.allTexts.length;
      this.cdr.markForCheck();
      return;
    }

    this.loading = true;
    console.log('Starting search for:', query);

    this.textService.searchTexts(query).subscribe({
      next: (result) => {
        console.log('Search results received:', result);
        // this.filteredTexts = result.content;
        this.filteredTexts = [...result.content]; // create new array reference

        this.filteredCount = result.total;
        this.loading = false;
        this.cdr.markForCheck();
        console.log('Loading set to false, filteredTexts:', this.filteredTexts);
      },
      error: (err) => {
        console.error('Search error:', err);
        this.loading = false;
        this.filteredTexts = [];
        this.filteredCount = 0;
        this.cdr.markForCheck();
      },
    });
  }
}
