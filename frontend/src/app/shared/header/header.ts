import { Component } from '@angular/core';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { ProSpaceLoginForm}  from '../proSpaceLoginForm/proSpaceLoginForm'
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
  ],
  templateUrl: './header.html',
  styleUrl: './header.scss'
})
export class HeaderComponent {
  constructor(private dialog: MatDialog) {}

  openProSpaceLoginForm(): void {
    this.dialog.open(ProSpaceLoginForm, {
      width: '420px',
      panelClass: 'espace-pro-dialog'
    });
  }
}


