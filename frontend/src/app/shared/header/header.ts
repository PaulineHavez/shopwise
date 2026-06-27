import { Component } from '@angular/core';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { ProSpaceLoginForm } from '../proSpaceLoginForm/proSpaceLoginForm';
import { CustomerLoginForm } from '../customer-login-form/customer-login-form';
import { MatDialog } from '@angular/material/dialog';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService, AuthUser } from '../../services/auth.service';
import { Observable } from 'rxjs';

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
  currentUser$: Observable<AuthUser | null>;

  constructor(private dialog: MatDialog, private router: Router, private authService: AuthService) {
    this.currentUser$ = this.authService.user$;
  }

  openProSpaceLoginForm(): void {
    this.dialog.open(ProSpaceLoginForm, {
      width: '420px',
      panelClass: 'espace-pro-dialog'
    });
  }

  openCustomerSpaceLoginForm(): void {
    this.dialog.open(CustomerLoginForm, {
      width: '420px',
      panelClass: 'espace-pro-dialog'
    });
  }

  goToDashboard(user: AuthUser): void {
    const path = user.role === 'merchant' ? '/merchant' : '/customer';
    this.router.navigate([path, user.id]);
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/']);
  }
}
