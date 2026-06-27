import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-espace-pro-dialog',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    HttpClientModule
  ],
  templateUrl: './proSpaceLoginForm.html',
  styleUrl: './proSpaceLoginForm.scss'
})
export class ProSpaceLoginForm {
  email: string = '';
  password: string = '';
  isLoading: boolean = false;

  constructor(private dialogRef: MatDialogRef<ProSpaceLoginForm>, private http: HttpClient, private router: Router, private authService: AuthService) {}

 onSubmit(): void {
   if (!this.email || !this.password) return;

   this.isLoading = true;

   this.http.post('/api/merchants/login', {
     email: this.email,
     password: this.password
   }).subscribe({
     next: (response:any) => {
       this.isLoading = false;
       this.authService.login(response.merchantId, 'merchant');
       this.dialogRef.close(response);
       this.router.navigate(['/merchant', response.merchantId]);
     },
     error: (err) => {
       this.isLoading = false;
     }
   });
 }

  onClose(): void {
    this.dialogRef.close();
  }
}
