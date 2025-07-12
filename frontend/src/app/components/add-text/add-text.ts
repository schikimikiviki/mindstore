import {
  Component,
  ChangeDetectorRef,
  ChangeDetectionStrategy,
  Input,
} from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  FormArray,
} from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { TextService } from '../../text/text.service';
import { CommonModule } from '@angular/common';
import { Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-add-text',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './add-text.html',
  styleUrl: './add-text.scss',
})
export class AddText {
  displayError = false;
  @Input() availableTags: string[] = [];
  selectedTags: string[] = [];

  postForm = new FormGroup({
    title: new FormControl(''),
    content_raw: new FormControl(''),
    content_html: new FormControl(''),
    tags: new FormArray([]),
    commandList: new FormArray([]),
  });

  constructor(
    private textService: TextService,
    private dialogRef: MatDialogRef<AddText>,
    private cdr: ChangeDetectorRef,
    @Inject(MAT_DIALOG_DATA) public data: { availableTags: string[] }
  ) {}

  ngOnInit() {
    this.availableTags = this.data.availableTags;
  }

  submitText() {
    const title = this.postForm.value.title ?? '';
    const content_raw = this.postForm.value.content_raw ?? '';
    const content_html = this.postForm.value.content_html ?? '';
    const tags = this.tags.value ?? [];
    const commandList = this.commandList.value ?? [];

    this.textService
      .addText(title, content_raw, content_html, tags, commandList)
      .subscribe({
        next: (response) => {
          console.log('Post successful:', response);
          this.closePopup();
        },
        error: (err) => {
          console.error('Post error:', err);
          this.displayError = true;
          this.cdr.detectChanges();
        },
      });
  }

  isTagSelected(tag: string): boolean {
    return this.selectedTags.includes(tag);
  }

  closePopup() {
    this.dialogRef.close(true);
  }

  addCommandField() {
    this.commandList.push(new FormControl(''));
  }

  removeCommandField(index: number) {
    this.commandList.removeAt(index);
  }

  get commandList(): FormArray {
    return this.postForm.get('commandList') as FormArray;
  }

  get tags(): FormArray {
    return this.postForm.get('tags') as FormArray;
  }

  toggleTag(tag: string) {
    const index = this.selectedTags.indexOf(tag);
    if (index === -1) {
      this.selectedTags.push(tag);
    } else {
      this.selectedTags.splice(index, 1); // remove if already selected
    }
  }
}
