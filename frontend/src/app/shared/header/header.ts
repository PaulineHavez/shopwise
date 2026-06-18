import { Component } from '@angular/core';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { ProSpaceLoginForm}  from '../proSpaceLoginForm/proSpaceLoginForm'
import { CustomerLoginForm } from '../customer-login-form/customer-login-form'
import { MatDialog } from '@angular/material/dialog';
import { CommonModule } from '@angular/common';
import {RegisterLoginForm} from '../register-login-form/register-login-form'

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    CommonModule
  ],
  templateUrl: './header.html',
  styleUrl: './header.scss'
})
export class HeaderComponent {
  errorMessage: string = '';
  constructor(private dialog: MatDialog) {}

  showError(message: string): void {
    this.errorMessage = message;
  }

  openProSpaceLoginForm(): void {
    this.dialog.open(ProSpaceLoginForm, {
      width: '420px',
      panelClass: 'espace-pro-dialog'
    });
  }

  openCustomerSpaceLoginForm(): void {

    const dialogRef = this.dialog.open(CustomerLoginForm, {
      width: '420px',
      panelClass: 'espace-pro-dialog'
    });
  }
}


