import { Routes } from '@angular/router';
import { MerchantDashboard } from './components/merchant-dashboard/merchant-dashboard';
import { CustomerDashboard } from './components/customer-dashboard/customer-dashboard';

export const routes: Routes = [
  { path: 'merchant/:id', component: MerchantDashboard },
  { path: 'customer/:id', component: CustomerDashboard }
];
