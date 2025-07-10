import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth/auth.service';
import { ChangeDetectorRef, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-filter-menu',
  imports: [CommonModule],
  standalone: true,
  templateUrl: './filter-menu.html',
  styleUrl: './filter-menu.scss',
})
export class FilterMenu {
  tags: string[] = [];
  selectedTags: string[] = [];

  @Output() childEmitter: EventEmitter<string[]> = new EventEmitter<string[]>();

  constructor(
    private authService: AuthService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.authService.getTags().subscribe((tags) => {
      this.tags = tags.sort();
      this.cdr.detectChanges();
      console.log(tags);
    });
  }

  toggleTag(tag: string) {
    const index = this.selectedTags.indexOf(tag);
    if (index === -1) {
      this.selectedTags.push(tag);
    } else {
      this.selectedTags.splice(index, 1); // remove if already selected
    }

    this.childEmitter.emit(this.selectedTags);
  }

  isTagSelected(tag: string): boolean {
    return this.selectedTags.includes(tag);
  }
}
