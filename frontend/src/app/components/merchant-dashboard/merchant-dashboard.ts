import { Component, signal } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';
import { CustomerCreateForm } from '../customer-create-form/customer-create-form';
import { CustomerEditForm } from '../customer-edit-form/customer-edit-form';
import { ActivatedRoute } from '@angular/router';
import { AppointmentCreateForm } from '../appointment-create-form/appointment-create-form';
import { AppointmentsList } from '../appointments-list/appointments-list';

@Component({
  selector: 'app-merchant-dashboard',
  standalone: true,
  imports: [CommonModule, MatButtonModule, AppointmentsList],
  templateUrl: './merchant-dashboard.html',
  styleUrl: './merchant-dashboard.scss',
})
export class MerchantDashboard {
  alertMessage: string = '';
  alertSuccess: boolean = false;
  merchantId: string = '';
  readonly showAppointmentsList = signal(false);
  readonly menuOpen = signal(false);

  toggleAppointmentsList(): void {
    this.showAppointmentsList.set(!this.showAppointmentsList());
  }

  toggleMenu(): void {
    this.menuOpen.set(!this.menuOpen());
  }

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

  openEditCustomer(): void {
      const dialogRef = this.dialog.open(CustomerEditForm, {data: { merchantId: this.merchantId }
      });

      dialogRef.afterClosed().subscribe((result: any) => {
        if (result?.success) {
          this.alertMessage = 'Client mis à jour avec succès !';
          this.alertSuccess = true;
        } else if (result?.success === false) {
          this.alertMessage = 'Erreur lors de la mise à jour du client.';
          this.alertSuccess = false;
        }
      setTimeout(() => {
          this.alertMessage = '';
        }, 3000);
      });
    }

     openCreateAppointment(): void {
        const dialogRef = this.dialog.open(AppointmentCreateForm, {data: { merchantId: this.merchantId }
        });

        dialogRef.afterClosed().subscribe((result: any) => {
          if (result?.success) {
            this.alertMessage = 'Rendez-vous créé avec succès !';
            this.alertSuccess = true;
          } else if (result?.success === false) {
            this.alertMessage = 'Erreur lors de la création du rendez-vous.';
            this.alertSuccess = false;
          }
        setTimeout(() => {
            this.alertMessage = '';
          }, 3000);
        });
      }

    appointmentStatusUpdated(event: {success:boolean}) {

      if(event.success){

        this.alertMessage =
          'Statut du rendez-vous mis à jour avec succès !';

        this.alertSuccess = true;

      } else {

        this.alertMessage =
          'Erreur lors de la mise à jour du statut du rendez-vous.';

        this.alertSuccess = false;

      }


      setTimeout(() => {

        this.alertMessage = '';

      },3000);

    }
}
