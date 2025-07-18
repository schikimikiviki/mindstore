import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth/auth.service';
import { ChangeDetectorRef, EventEmitter, Output } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';

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
  @Output() onResetBtn: EventEmitter<boolean> = new EventEmitter<boolean>();

  constructor(
    private authService: AuthService,
    private cdr: ChangeDetectorRef,
    private dialogRef: MatDialogRef<FilterMenu>
  ) {}

  ngOnInit() {
    this.authService.getTags().subscribe((tags) => {
      this.tags = tags.sort();
      this.cdr.detectChanges();
      //  console.log(tags);
    });
  }

  ngAfterViewInit() {
    // load the tags from the localstore and display them

    const savedData = JSON.parse(localStorage.getItem('selectedTags') || '[]');
    if (savedData.length > 0) {
      this.selectedTags = savedData;
      this.cdr.detectChanges();
    }
  }

  toggleTag(tag: string) {
    const index = this.selectedTags.indexOf(tag);
    if (index === -1) {
      this.selectedTags.push(tag);
    } else {
      this.selectedTags.splice(index, 1); // remove if already selected
    }

    this.childEmitter.emit(this.selectedTags);

    // save this to localstorage as well, so that in the next opening, we can show that they are selected
    localStorage.setItem('selectedTags', JSON.stringify(this.selectedTags));
  }

  isTagSelected(tag: string): boolean {
    return this.selectedTags.includes(tag);
  }

  closeMenu() {
    this.dialogRef.close();
  }

  resetFilters() {
    // display all search results
    this.onResetBtn.emit(true);
    this.dialogRef.close();
    localStorage.removeItem('selectedTags');
  }
}
