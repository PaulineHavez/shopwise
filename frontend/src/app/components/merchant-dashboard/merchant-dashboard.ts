import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';
import { CustomerCreateForm } from '../customer-create-form/customer-create-form';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-merchant-dashboard',
  standalone: true,
  imports: [CommonModule, MatButtonModule],
  templateUrl: './merchant-dashboard.html',
  styleUrl: './merchant-dashboard.scss',
})
export class MerchantDashboard {
  alertMessage: string = '';
  alertSuccess: boolean = false;
  merchantId: string = '';

  constructor(private dialog: MatDialog, private route: ActivatedRoute) {
      this.merchantId = this.route.snapshot.paramMap.get('id') || '';
      }

  openCreateCustomer(): void {
    const dialogRef = this.dialog.open(CustomerCreateForm, {data: { merchantId: this.merchantId }
    });

    dialogRef.afterClosed().subscribe((result: any) => {
      if (result?.success) {
        this.alertMessage = 'Client créé avec succès !';
        this.alertSuccess = true;
      } else if (result?.success === false) {
        this.alertMessage = 'Erreur lors de la création du client.';
        this.alertSuccess = false;
      }
    setTimeout(() => {
        this.alertMessage = '';
      }, 3000);
    });
  }
}
