import {
  Component,
  Input,
  OnInit,
  SimpleChanges,
  Output,
  EventEmitter,
  PLATFORM_ID,
  Inject,
} from '@angular/core';
import { Text } from '../../text/text.model';
import { CommonModule } from '@angular/common';
import { isPlatformBrowser } from '@angular/common';

@Component({
  selector: 'app-result-page',
  templateUrl: './result-page.html',
  styleUrl: './result-page.scss',
  standalone: true,
  imports: [CommonModule],
})
export class ResultPage implements OnInit {
  loading = true;
  expandedTextId: number | null = null;
  searchAfter: string | null = null;
  previousScrollY = 0;

  @Input() childTryingToPullData: Text[] = [];
  @Input() initialSearchAfter: string | null = null;
  @Output() loadMore = new EventEmitter<void>();

  constructor(@Inject(PLATFORM_ID) private platformId: Object) {}

  ngOnInit(): void {
    this.searchAfter = this.initialSearchAfter;
    // console.log('initial : ', this.initialSearchAfter);
    this.loading = this.childTryingToPullData.length == 0;
    console.log(this.childTryingToPullData);

    // add scroll event
    if (isPlatformBrowser(this.platformId)) {
      window.addEventListener('scroll', this.onScroll, true);
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (
      changes['childTryingToPullData'] &&
      changes['childTryingToPullData'].currentValue.length > 0
    ) {
      this.loading = false;
    }
  }

  selectCommand(command: string) {
    // wir kopieren das in die Zwischenablage
    navigator.clipboard.writeText(command);
  }

  expandSection(text: Text) {
    if (this.expandedTextId === text.id) {
      this.expandedTextId = null; // collapse if already expanded
    } else {
      this.expandedTextId = text.id;
    }
  }

  ngOnDestroy(): void {
    if (isPlatformBrowser(this.platformId)) {
      window.removeEventListener('scroll', this.onScroll, true);
    }
  }

  onScroll = (): void => {
    if (!isPlatformBrowser(this.platformId)) return;

    const scrollY = window.scrollY;
    const visibleHeight = window.innerHeight;
    const pageHeight =
      document.documentElement.scrollHeight || document.body.scrollHeight;

    const scrollingDown = scrollY > this.previousScrollY;
    this.previousScrollY = scrollY;

    const nearBottom = scrollY + visibleHeight >= pageHeight - 100;

    if (scrollingDown && nearBottom && !this.loading) {
      this.executeLoad();
    }
  };

  executeLoad() {
    console.log('scroll triggered.. ');
    this.loadMore.emit();
  }
}
