import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { httpResource } from '@angular/common/http';
import { MatTableModule } from '@angular/material/table';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { DatePipe } from '@angular/common';
import { Transaction } from './transaction-model';

@Component({
  selector: 'app-customer-transactions-list',
  imports: [MatTableModule, MatProgressSpinnerModule, DatePipe],
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './customer-transactions-list.html',
  styleUrl: './customer-transactions-list.scss',
})
export class CustomerTransactionsList {
  readonly customerId = input.required<string>();

  readonly displayedColumns = ['transaction_date', 'status', 'earnedPoints'];

  readonly transactionsResource = httpResource<Transaction[]>(
    () => `/api/transactions/${this.customerId()}`,
    { defaultValue: [] }
  );
}
