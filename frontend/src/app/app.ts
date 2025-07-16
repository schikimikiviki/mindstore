import { Header } from './components/header/header';
import { ViewChild } from '@angular/core';
import { ResultPage } from './components/result-page/result-page';
import { Text } from './text/text.model';
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, FormsModule, Header, ResultPage],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App {
  textArray: Text[] = [];
  searchAfter: string | null = null;

  @ViewChild(Header) headerRef!: Header;

  getDataFromChild(e: Text[]) {
    this.textArray = e;
  }

  getSearchAfterInitial(e: string) {
    this.searchAfter = e;
  }

  loadNextPage() {
    if (this.headerRef) {
      this.headerRef.loadNextPage(this.searchAfter);
      console.log('app emitting to header');
    }
  }
}
