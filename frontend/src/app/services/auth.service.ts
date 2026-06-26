import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export type UserRole = 'customer' | 'merchant';

export interface AuthUser {
  id: string;
  role: UserRole;
}

const STORAGE_KEY = 'auth_user';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private _user$ = new BehaviorSubject<AuthUser | null>(this._readStorage());

  readonly user$ = this._user$.asObservable();

  login(id: string, role: UserRole): void {
    const user: AuthUser = { id, role };
    localStorage.setItem(STORAGE_KEY, JSON.stringify(user));
    this._user$.next(user);
  }

  logout(): void {
    localStorage.removeItem(STORAGE_KEY);
    this._user$.next(null);
  }

  isLoggedIn(): boolean {
    return this._user$.getValue() !== null;
  }

  getCurrentUser(): AuthUser | null {
    return this._user$.getValue();
  }

  private _readStorage(): AuthUser | null {
    const raw = localStorage.getItem(STORAGE_KEY);
    return raw ? (JSON.parse(raw) as AuthUser) : null;
  }
}
