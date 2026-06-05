import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

@Component({
  selector: 'app-espace-pro-dialog',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule
  ],
  templateUrl: '/proSpaceLoginForm.html',
  styleUrl: '/proSpaceLoginForm.scss'
})
export class ProSpaceLoginForm {
  email: string = '';
  password: string = '';
  isLoading: boolean = false;

  constructor(private dialogRef: MatDialogRef<ProSpaceLoginForm>) {}

  onSubmit(): void {
    if (!this.email || !this.password) return;

    this.isLoading = true;

    // Remplace par ton appel API
    /*
    setTimeout(() => {
      this.isLoading = false;
      console.log('Connexion commerçant :', this.email);
      this.dialogRef.close({ email: this.email });
    }, 1500);
  */
  }


  onClose(): void {
    this.dialogRef.close();
  }
}
