import { Component, OnInit } from '@angular/core';
import { TextService } from '../../text/text.service';
import { Text } from '../../text/text.model';
import { FilterButton } from '../filter-button/filter-button';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [FilterButton, CommonModule],
  templateUrl: './header.html',
  styleUrls: ['./header.css'],
})
export class Header implements OnInit {
  title = 'mindstore';
  allTexts: Text[] = [];
  filteredTexts: Text[] = [];
  loading = true;
  textCount = 0;

  constructor(private textService: TextService) {}

  ngOnInit() {
    this.textService.getTexts().subscribe((texts) => {
      console.log('Fetched texts:', texts);
      this.textCount = texts.length;
      this.allTexts = texts;
      this.filteredTexts = texts;
      this.loading = false;
    });
  }

  getInputValue(event: Event): string {
    return (event.target as HTMLInputElement).value;
  }

  filterTexts(searchTerm: string) {
    const term = searchTerm.toLowerCase();
    this.filteredTexts = this.allTexts.filter(
      (t) =>
        t.title.toLowerCase().includes(term) ||
        t.content_raw.toLowerCase().includes(term)
    );
  }
}
