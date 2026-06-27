import { Component, signal } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { ActivatedRoute } from '@angular/router';
import { CustomerData } from '../customer-data/customer-data';
import { CustomerEarnedPoints } from '../customer-earned-points/customer-earned-points';
import { CustomerTransactionsList } from '../customer-transactions-list/customer-transactions-list';

@Component({
  selector: 'app-customer-dashboard',
  imports: [MatButtonModule, CustomerTransactionsList],
  templateUrl: './customer-dashboard.html',
  styleUrl: './customer-dashboard.scss',
})
export class CustomerDashboard {
  customerId: string = '';
  alertMessage: string = '';
  alertSuccess: boolean = false;
  readonly showTransactionsList = signal(false);
  readonly menuOpen = signal(false);

  constructor(private dialog: MatDialog, private route: ActivatedRoute) {
    this.customerId = this.route.snapshot.paramMap.get('id') || '';
  }

  toggleMenu(): void {
    this.menuOpen.set(!this.menuOpen());
  }

  openCustomerData(): void {
    const dialogRef = this.dialog.open(CustomerData, { data: { customerId: this.customerId } });

    dialogRef.afterClosed().subscribe((result: any) => {
      if (result?.success === false) {
        this.alertMessage = 'Erreur lors du chargement des informations personnelles.';
        this.alertSuccess = false;
        setTimeout(() => { this.alertMessage = ''; }, 3000);
      }
    });
  }

  openEarnedPoints(): void {
    const dialogRef = this.dialog.open(CustomerEarnedPoints, { data: { customerId: this.customerId } });

    dialogRef.afterClosed().subscribe((result: any) => {
      if (result?.success === false) {
        this.alertMessage = 'Erreur lors du chargement des points fidélité.';
        this.alertSuccess = false;
        setTimeout(() => { this.alertMessage = ''; }, 3000);
      }
    });
  }

  toggleTransactionsList(): void {
    this.showTransactionsList.set(!this.showTransactionsList());
  }
}
