import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Router } from '@angular/router';
import { CustomerLoginForm } from '../customer-login-form/customer-login-form';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-register-login-form',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    HttpClientModule
  ],
  templateUrl:  './register-login-form.html',
  styleUrl: './register-login-form.scss',
})

export class RegisterLoginForm {
  email: string = '';
  password: string = '';
  confirmedPassword: string = '';
  passwordError: string = '';
  isLoading: boolean = false;

  constructor(private dialogRef: MatDialogRef<RegisterLoginForm>,  private http: HttpClient, private router: Router, private dialog: MatDialog) {}

  onPasswordChange(): void {
      this.validatePassword();
  }

validatePassword(): boolean {

  this.passwordError = ''; // important : reset à chaque validation

  if (this.password.length < 8) {
    this.passwordError = 'Le mot de passe doit contenir au minimum 8 caractères.';
    return false;
  }

  if (this.password !== this.confirmedPassword) {
    this.passwordError = 'Les mots de passe ne correspondent pas.';
    return false;
  }

  return true;
}

 onSubmit(): void {
   if (!this.email || !this.password || !this.confirmedPassword) {
      return;
    }

    if (!this.validatePassword()) {
      return;
    }

    this.isLoading = true;

   this.http.put('/api/customers/register', {
     email: this.email,
     password: this.password
   }).subscribe({
     next: (response:any) => {
      this.isLoading = false;

      this.dialogRef.close({
        success: true,
        data: response
      });

      this.dialog.open(CustomerLoginForm, {
        width: '420px',
        panelClass: 'espace-pro-dialog'
      });
    },
     error: (err) => {
      this.isLoading = false;

       const backendMessage = err?.error?.message;

       let alertMessage = "Une erreur s'est produite. Veuillez réessayer.";

       if (backendMessage === 'Customer account alreasy exists') {
         alertMessage = 'Un compte existe déjà avec cette adresse e-mail.';
       }

       this.dialogRef.close({
         success: false,
         message: alertMessage
       });
     }
   });
 }

  onClose(): void {
    this.dialogRef.close();
  }
}
