import { CommonModule } from '@angular/common';
import {
  Component,
  ChangeDetectorRef,
  ChangeDetectionStrategy,
} from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth/auth.service';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-popup',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './popup.html',
  styleUrl: './popup.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Popup {
  displayError = false;

  applyForm = new FormGroup({
    email: new FormControl(''),
    password: new FormControl(''),
  });

  constructor(
    private authService: AuthService,
    private dialogRef: MatDialogRef<Popup>,
    private cdr: ChangeDetectorRef
  ) {}
  submitLogin() {
    const email = this.applyForm.value.email ?? '';
    const password = this.applyForm.value.password ?? '';

    this.authService.loginUser(email, password).subscribe({
      next: () => this.dialogRef.close(true),
      error: () => this.dialogRef.close(false),
    });

    // this.authService.loginUser(email, password).subscribe({
    //   next: (response) => {
    //     // console.log('Login successful:', response);

    //     const expiresAt = Date.now() + response.expiresIn * 1000;
    //     localStorage.setItem('token', response.token);
    //     localStorage.setItem('token_expiry', expiresAt.toString());

    //     // start the timer to auto-logout
    //     this.authService.startTokenExpiryTimer(response.expiresIn);

    //     // close window
    //     this.closePopup();
    //   },
    //   error: (err) => {
    //     console.error('Login error:', err);
    //     // display error message
    //     this.displayError = true;
    //     this.cdr.detectChanges();
    //   },
    // });
  }

  closePopup() {
    this.dialogRef.close(true);
  }
}
