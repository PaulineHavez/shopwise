import { Routes } from '@angular/router';
import { MerchantDashboard } from './components/merchant-dashboard/merchant-dashboard';

export const routes: Routes = [
  { path: 'merchant/:id', component: MerchantDashboard }
];
