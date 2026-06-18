import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import {CustomerData} from '../customer-data/customer-data'


@Component({
  selector: 'app-customer-dashboard',
  imports: [CommonModule, MatButtonModule],
  templateUrl: './customer-dashboard.html',
  styleUrl: './customer-dashboard.scss',
})
export class CustomerDashboard {;
  customerId: string = '';

  constructor(private dialog: MatDialog, private route: ActivatedRoute) {
      this.customerId = this.route.snapshot.paramMap.get('id') || '';
  }

  openCustomerData(): void {
    const dialogRef = this.dialog.open(CustomerData, {data: { customerId: this.customerId }
    });
  }

}
