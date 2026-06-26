import { inject } from '@angular/core';
import { CanActivateFn, ActivatedRouteSnapshot, Router } from '@angular/router';
import { AuthService, UserRole } from '../services/auth.service';

export function authGuard(role: UserRole): CanActivateFn {
  return (route: ActivatedRouteSnapshot) => {
    const authService = inject(AuthService);
    const router = inject(Router);

    const user = authService.getCurrentUser();
    const routeId = route.paramMap.get('id');

    if (user && user.role === role && user.id === routeId) {
      return true;
    }

    return router.createUrlTree(['/']);
  };
}
