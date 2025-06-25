import { Component, OnInit } from '@angular/core';
import { TextService } from './text/text.service';
import { Text } from './text/text.model';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, FormsModule],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App implements OnInit {
  title = 'mindstore';
  allTexts: Text[] = [];
  filteredTexts: Text[] = [];

  constructor(private textService: TextService) {}

  ngOnInit() {
    this.textService.getTexts().subscribe((texts) => {
      this.allTexts = texts;
      this.filteredTexts = texts;
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
