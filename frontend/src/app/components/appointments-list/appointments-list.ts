import { ChangeDetectionStrategy, Component, signal, input } from '@angular/core';
import { httpResource } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { MatTableModule } from '@angular/material/table';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { Appointment, AppointmentStatus } from './appointment-model';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-appointments-list',
  imports: [
    FormsModule,
    MatTableModule,
    MatFormFieldModule,
    MatSelectModule,
    MatDatepickerModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    DatePipe
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './appointments-list.html',
  styleUrl: './appointments-list.scss',
})
export class AppointmentsList {
  readonly merchantId = input.required<string>();

  readonly dateFilter = signal<Date | null>(null);
  readonly statusFilter = signal<AppointmentStatus | null>(null);
  readonly emailFilter = signal<string | null>(null);

  readonly displayedColumns = ['startAt', 'endAt', 'status', 'email', 'earnedPoints'];

  readonly appointmentsResource = httpResource<Appointment[]>(() => {
    const params = new URLSearchParams();

    const date = this.dateFilter();
    if (date) params.set('date', this.toIsoDate(date));

    const status = this.statusFilter();
    if (status) params.set('status', status);

    const email = this.emailFilter();
    if (email) params.set('email', email);

    const query = params.toString();
    return `/api/appointments/${this.merchantId()}${query ? '?' + query : ''}`;
  }, { defaultValue: [] });

  onEmailChange(value: string): void {
  this.emailFilter.set(value ? value : null);
  }

  resetFilters(): void {
    this.dateFilter.set(null);
    this.statusFilter.set(null);
    this.emailFilter.set(null);
  }

  private toIsoDate(date: Date): string {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  }
}
