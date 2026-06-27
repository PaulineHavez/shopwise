import { Routes } from '@angular/router';
import { MerchantDashboard } from './components/merchant-dashboard/merchant-dashboard';
import { CustomerDashboard } from './components/customer-dashboard/customer-dashboard';
import { Home } from './components/home/home';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: '', component: Home },
  { path: 'merchant/:id', component: MerchantDashboard, canActivate: [authGuard('merchant')] },
  { path: 'customer/:id', component: CustomerDashboard, canActivate: [authGuard('customer')] }
];
