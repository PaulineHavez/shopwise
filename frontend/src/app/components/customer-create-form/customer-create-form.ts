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
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Inject } from '@angular/core';

@Component({
  selector: 'app-customer-create-form',
  standalone : true,
  imports: [ CommonModule,
                FormsModule,
                MatDialogModule,
                MatFormFieldModule,
                MatInputModule,
                MatButtonModule,
                MatIconModule,
                MatProgressSpinnerModule,
                HttpClientModule],
  templateUrl: './customer-create-form.html',
  styleUrl: './customer-create-form.scss',
})

export class CustomerCreateForm {
     name: string = '';
     email: string = '';
     phoneNumber : string = '';
     isLoading: boolean = false;
     emailError: string = '';

       isValidEmail(email: string): boolean {
         const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
         return regex.test(email);
       }

     constructor(private dialogRef: MatDialogRef<CustomerCreateForm>,   private http: HttpClient, @Inject(MAT_DIALOG_DATA) private data: { merchantId: string }) {}

    onSubmit(): void {
       this.emailError = '';
      if (!this.email || !this.name || !this.phoneNumber) return;

      if (!this.isValidEmail(this.email)) {
            this.emailError = 'Veuillez entrer une adresse e-mail valide';
            return;
          }

      this.isLoading = true;

      this.http.post('/api/customers/', {
        name : this.name,
        phoneNumber : this.phoneNumber,
        email: this.email,
        merchantId: this.data.merchantId
      }).subscribe({
        next: (response:any) => {
          this.isLoading = false;
          this.dialogRef.close({ success: true });
        },
        error: (err) => {
          this.isLoading = false;
          this.dialogRef.close({ success: false });
        }
      });
    }

     onClose(): void {
       this.dialogRef.close();
     }
}
