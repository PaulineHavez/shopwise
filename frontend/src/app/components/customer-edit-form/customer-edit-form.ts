import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Inject } from '@angular/core';

@Component({
  selector: 'app-customer-edit-form',
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
  templateUrl: './customer-edit-form.html',
  styleUrl: './customer-edit-form.scss',
})
export class CustomerEditForm {
  searchEmail: string = '';
  isSearching: boolean = false;
  searchError: string = '';

  customerFound: boolean = false;
  customerId: string = '';

  name: string = '';
  email: string = '';
  phoneNumber: string = '';
  isLoading: boolean = false;
  emailError: string = '';

  constructor(
    private dialogRef: MatDialogRef<CustomerEditForm>,
    private http: HttpClient,
    @Inject(MAT_DIALOG_DATA) private data: { merchantId: string }
  ) {}

  isValidEmail(email: string): boolean {
    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return regex.test(email);
  }

  onSearch(): void {
    this.searchError = '';
    this.customerFound = false;

    if (!this.searchEmail) return;

    this.isSearching = true;

    this.http.get<any>(`/api/customers/email/${this.searchEmail}`).subscribe({
      next: (customer) => {
        this.isSearching = false;
        if (customer) {
          this.customerId = customer.customerId;
          this.name = customer.name;
          this.email = customer.email;
          this.phoneNumber = customer.phoneNumber;
          this.customerFound = true;
        } else {
          this.searchError = 'Aucun client trouvé avec cet e-mail.';
        }
      },
      error: () => {
        this.isSearching = false;
        this.searchError = 'Aucun client trouvé avec cet e-mail.';
      }
    });
  }

  onSubmit(): void {
    this.emailError = '';
    if (!this.email || !this.name || !this.phoneNumber) return;

    if (!this.isValidEmail(this.email)) {
      this.emailError = 'Veuillez entrer une adresse e-mail valide';
      return;
    }

    this.isLoading = true;

    this.http.put(`/api/customers/${this.customerId}`, {
      name: this.name,
      phoneNumber: this.phoneNumber,
      email: this.email,
      merchantId: this.data.merchantId
    }).subscribe({
      next: (response: any) => {
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
