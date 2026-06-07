import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Inject } from '@angular/core';
import { switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-appointment-create-form',
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
    HttpClientModule,
    MatDatepickerModule,
    MatNativeDateModule
  ],
  templateUrl: './appointment-create-form.html',
  styleUrl: './appointment-create-form.scss',
})
export class AppointmentCreateForm {
  customerEmail = '';
  serviceName = '';
  startDate: Date | null = null;
  startTime = '';
  endDate: Date | null = null;
  endTime = '';
  serviceId = '';
  customerId = '';
  isLoading = false;
  searchError = '';

  constructor(
    private dialogRef: MatDialogRef<AppointmentCreateForm>,  // ← était CustomerCreateForm
    private http: HttpClient,
    @Inject(MAT_DIALOG_DATA) private data: { merchantId: string }
  ) {}

  onSubmit(): void {
    if (!this.customerEmail || !this.serviceName || !this.startDate || !this.startTime || !this.endDate || !this.endTime) return;

    const [startHours, startMinutes] = this.startTime.split(':').map(Number);
    const startAt = new Date(this.startDate);
    startAt.setHours(startHours, startMinutes, 0, 0);

    const [endHours, endMinutes] = this.endTime.split(':').map(Number);
    const endAt = new Date(this.endDate);
    endAt.setHours(endHours, endMinutes, 0, 0);

    this.isLoading = true;
    this.searchError = '';

    this.http.get<any>(`/api/customers/email/${this.customerEmail}`).pipe(
      switchMap((customer) => {
        if (!customer) throw new Error('client');
        this.customerId = customer.customerId;
        return this.http.get<any>(`/api/services/${this.serviceName}`);
      }),
      switchMap((service) => {
        if (!service) throw new Error('service');
        this.serviceId = service.serviceId;
        return this.http.post('/api/appointments/', {
          startAt,
          endAt,
          serviceId: this.serviceId,
          merchantId: this.data.merchantId,
          customerId: this.customerId,
        });
      })
    ).subscribe({
      next: () => {
        this.isLoading = false;
        this.dialogRef.close({ success: true });
      },
      error: (err) => {
        this.isLoading = false;
        if (err.message === 'client') {
          this.searchError = 'Aucun client trouvé avec cet e-mail.';
        } else if (err.message === 'service') {
          this.searchError = 'Aucun service trouvé avec ce nom.';
        } else {
          this.dialogRef.close({ success: false });
        }
      }
    });
  }

  onClose(): void {
    this.dialogRef.close();
  }
}
