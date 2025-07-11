import {
  Component,
  ChangeDetectorRef,
  EventEmitter,
  Output,
} from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { FilterMenu } from '../filter-menu/filter-menu';

@Component({
  selector: 'app-filter-button',
  imports: [],
  standalone: true,
  templateUrl: './filter-button.html',
  styleUrls: ['./filter-button.scss'],
})
export class FilterButton {
  @Output() childEmitter = new EventEmitter<string[]>();
  @Output() onResetBtn = new EventEmitter<boolean>();

  constructor(private dialogRef: MatDialog, private cdr: ChangeDetectorRef) {}

  openFilterModal() {
    const dialogRef = this.dialogRef.open(FilterMenu, {
      panelClass: 'filter-slide-in',
      position: { left: '0' },
      height: '100vh',
    });

    dialogRef.afterOpened().subscribe(() => {
      dialogRef.componentInstance.childEmitter.subscribe((tags: string[]) => {
        this.childEmitter.emit(tags);
      });

      dialogRef.componentInstance.onResetBtn.subscribe((msg: boolean) => {
        this.onResetBtn.emit(msg);
      });
    });

    //  handle afterClosed
    dialogRef.afterClosed().subscribe((result) => {
      console.log('Dialog closed with:', result);
      if (result === true) {
        this.cdr.detectChanges();
      }
    });
  }
}
