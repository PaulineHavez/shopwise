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
import { MatDialog } from '@angular/material/dialog';
import { RegisterLoginForm } from '../register-login-form/register-login-form'

@Component({
  selector: 'app-customer-login-form',
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
  templateUrl: './customer-login-form.html',
  styleUrl: './customer-login-form.scss'
})

export class CustomerLoginForm {
  email: string = '';
  password: string = '';
  isLoading: boolean = false;

  constructor(private dialogRef: MatDialogRef<CustomerLoginForm>,   private http: HttpClient, private router: Router, private dialog: MatDialog) {}

   openRegisterDialog(): void {
          this.dialogRef.close();
          this.dialog.open(RegisterLoginForm, {
            width: '420px',
            panelClass: 'espace-pro-dialog'
          });}

 onSubmit(): void {
   if (!this.email || !this.password) return;

   this.isLoading = true;

   this.http.post('/api/customers/login', {
     email: this.email,
     password: this.password
   }).subscribe({
     next: (response:any) => {
       this.isLoading = false;
       this.dialogRef.close(response);
      this.router.navigate(['/customer', response.customerId]);
     },
     error: (err) => {
       this.isLoading = false;
     }
   });
 }

  onClose(): void {
    this.dialogRef.close();
  }

}
