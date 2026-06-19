import { Component, signal } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { CustomerData } from '../customer-data/customer-data';
import { CustomerEarnedPoints } from '../customer-earned-points/customer-earned-points';
import { CustomerTransactionsList } from '../customer-transactions-list/customer-transactions-list';

@Component({
  selector: 'app-customer-dashboard',
  imports: [CommonModule, MatButtonModule, CustomerTransactionsList],
  templateUrl: './customer-dashboard.html',
  styleUrl: './customer-dashboard.scss',
})
export class CustomerDashboard {
  customerId: string = '';
  readonly showTransactionsList = signal(false);

  constructor(private dialog: MatDialog, private route: ActivatedRoute) {
    this.customerId = this.route.snapshot.paramMap.get('id') || '';
  }

  openCustomerData(): void {
    this.dialog.open(CustomerData, { data: { customerId: this.customerId } });
  }

  openEarnedPoints(): void {
    this.dialog.open(CustomerEarnedPoints, { data: { customerId: this.customerId } });
  }

  toggleTransactionsList(): void {
    this.showTransactionsList.set(!this.showTransactionsList());
  }
}
