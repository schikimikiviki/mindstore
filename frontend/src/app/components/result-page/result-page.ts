import { Component, Input, OnInit, SimpleChanges } from '@angular/core';
import { Text } from '../../text/text.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-result-page',
  templateUrl: './result-page.html',
  styleUrl: './result-page.scss',
  standalone: true,
  imports: [CommonModule],
})
export class ResultPage implements OnInit {
  loading = true;

  @Input() childTryingToPullData: Text[] = [];

  constructor() {}

  ngOnInit(): void {
    this.loading = this.childTryingToPullData.length == 0;
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (
      changes['childTryingToPullData'] &&
      changes['childTryingToPullData'].currentValue.length > 0
    ) {
      this.loading = false;
    }
  }
}
