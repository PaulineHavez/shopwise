import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { RegisterLoginForm } from './register-login-form';
import { MatDialogRef, MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { HttpClientModule, provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';

describe('RegisterLoginForm', () => {
  let component: RegisterLoginForm;
  let fixture: ComponentFixture<RegisterLoginForm>;
  let httpMock: HttpTestingController;
  let dialogRef: jasmine.SpyObj<MatDialogRef<RegisterLoginForm>>;
  let matDialog: jasmine.SpyObj<MatDialog>;
  let router: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    dialogRef = jasmine.createSpyObj('MatDialogRef', ['close']);
    matDialog = jasmine.createSpyObj('MatDialog', ['open']);
    matDialog.open.and.returnValue({ afterClosed: () => of(null) } as any);
    router = jasmine.createSpyObj('Router', ['navigate']);

    await TestBed.configureTestingModule({
      imports: [RegisterLoginForm],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        { provide: MatDialogRef, useValue: dialogRef },
        { provide: MatDialog, useValue: matDialog },
        { provide: Router, useValue: router },
      ],
    })
    .overrideComponent(RegisterLoginForm, {
      remove: { imports: [HttpClientModule] },
      add: { providers: [{ provide: MatDialog, useValue: matDialog }] },
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegisterLoginForm);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
    fixture.detectChanges();
  });

  afterEach(() => httpMock.verify());

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize with default values', () => {
    expect(component.email).toBe('');
    expect(component.password).toBe('');
    expect(component.confirmedPassword).toBe('');
    expect(component.passwordError).toBe('');
    expect(component.isLoading).toBeFalse();
  });

  it('onClose() should call dialogRef.close()', () => {
    component.onClose();
    expect(dialogRef.close).toHaveBeenCalled();
  });

  it('validatePassword() should return false and set error when password is too short', () => {
    component.password = 'abc';
    component.confirmedPassword = 'abc';
    const result = component.validatePassword();
    expect(result).toBeFalse();
    expect(component.passwordError).toBe('Le mot de passe doit contenir au minimum 8 caractères.');
  });

  it('validatePassword() should return false and set error when passwords do not match', () => {
    component.password = 'password123';
    component.confirmedPassword = 'different123';
    const result = component.validatePassword();
    expect(result).toBeFalse();
    expect(component.passwordError).toBe('Les mots de passe ne correspondent pas.');
  });

  it('validatePassword() should return true and clear error when passwords are valid', () => {
    component.password = 'password123';
    component.confirmedPassword = 'password123';
    const result = component.validatePassword();
    expect(result).toBeTrue();
    expect(component.passwordError).toBe('');
  });

  it('onPasswordChange() should call validatePassword()', () => {
    spyOn(component, 'validatePassword').and.callThrough();
    component.onPasswordChange();
    expect(component.validatePassword).toHaveBeenCalled();
  });

  it('onSubmit() should return early when fields are empty', () => {
    component.onSubmit();
    expect(component.isLoading).toBeFalse();
    httpMock.expectNone('/api/customers/register');
  });

  it('onSubmit() should return early when password validation fails', () => {
    component.email = 'alice@test.com';
    component.password = 'short';
    component.confirmedPassword = 'short';

    component.onSubmit();
    expect(component.isLoading).toBeFalse();
    httpMock.expectNone('/api/customers/register');
  });

  it('onSubmit() should PUT and close with success then open login dialog', () => {
    component.email = 'alice@test.com';
    component.password = 'password123';
    component.confirmedPassword = 'password123';

    component.onSubmit();
    expect(component.isLoading).toBeTrue();

    httpMock.expectOne('/api/customers/register').flush({ customerId: 'c-1' });

    expect(component.isLoading).toBeFalse();
    expect(dialogRef.close).toHaveBeenCalledWith({ success: true, data: { customerId: 'c-1' } });
    expect(matDialog.open).toHaveBeenCalled();
  });

  it('onSubmit() should close with error message when account already exists', () => {
    component.email = 'alice@test.com';
    component.password = 'password123';
    component.confirmedPassword = 'password123';

    component.onSubmit();
    httpMock.expectOne('/api/customers/register').flush(
      { message: 'Customer account alreasy exists' },
      { status: 409, statusText: 'Conflict' }
    );

    expect(component.isLoading).toBeFalse();
    expect(dialogRef.close).toHaveBeenCalledWith({
      success: false,
      message: 'Un compte existe déjà avec cette adresse e-mail.',
    });
  });

  it('onSubmit() should close with generic error message on other HTTP error', () => {
    component.email = 'alice@test.com';
    component.password = 'password123';
    component.confirmedPassword = 'password123';

    component.onSubmit();
    httpMock.expectOne('/api/customers/register').flush(
      { message: 'Internal server error' },
      { status: 500, statusText: 'Server Error' }
    );

    expect(component.isLoading).toBeFalse();
    expect(dialogRef.close).toHaveBeenCalledWith({
      success: false,
      message: "Une erreur s'est produite. Veuillez réessayer.",
    });
  });
});
