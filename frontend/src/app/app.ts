import { Component } from '@angular/core';

import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Header } from './components/header/header';
import { ResultPage } from './components/result-page/result-page';
import { Text } from './text/text.model';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, FormsModule, Header, ResultPage],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App {
  textArray: Text[] = [];

  constructor() {}

  getDataFromChild(e: Text[]) {
    // we receive the texts from the Header component and send it to the result page component
    this.textArray = e;
  }
}
