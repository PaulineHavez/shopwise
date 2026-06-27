import { Component, Inject, OnInit } from '@angular/core';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

@Component({
  selector: 'app-customer-data',
  imports: [
    CommonModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    HttpClientModule
  ],
  templateUrl: './customer-data.html',
  styleUrl: './customer-data.scss',
})
export class CustomerData implements OnInit {

  name = '';
  email = '';
  phoneNumber = '';
  isLoading = false;

  constructor(
    private http: HttpClient,
    private dialogRef: MatDialogRef<CustomerData>,
    @Inject(MAT_DIALOG_DATA) private data: { customerId: string }
  ) {}

  ngOnInit(): void {

    this.isLoading = true;

    this.http.get<any>(`/api/customers/${this.data.customerId}`)
      .subscribe({
        next: (customer) => {

          this.isLoading = false;

          if (customer) {
            this.name = customer.name;
            this.email = customer.email;
            this.phoneNumber = customer.phoneNumber;
          }
        },

        error: () => {
          this.isLoading = false;
          this.dialogRef.close({ success: false });
        }
      });

  }
}
