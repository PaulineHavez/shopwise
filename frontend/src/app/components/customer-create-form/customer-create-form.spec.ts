import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CustomerCreateForm } from './customer-create-form';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { HttpClientModule, provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';

describe('CustomerCreateForm', () => {
  let component: CustomerCreateForm;
  let fixture: ComponentFixture<CustomerCreateForm>;
  let httpMock: HttpTestingController;
  let dialogRef: jasmine.SpyObj<MatDialogRef<CustomerCreateForm>>;

  beforeEach(async () => {
    dialogRef = jasmine.createSpyObj('MatDialogRef', ['close']);

    await TestBed.configureTestingModule({
      imports: [CustomerCreateForm],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        { provide: MatDialogRef, useValue: dialogRef },
        { provide: MAT_DIALOG_DATA, useValue: { merchantId: 'merchant-1' } },
      ],
    })
    .overrideComponent(CustomerCreateForm, { remove: { imports: [HttpClientModule] } })
    .compileComponents();

    fixture = TestBed.createComponent(CustomerCreateForm);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
    fixture.detectChanges();
  });

  afterEach(() => httpMock.verify());

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize with default values', () => {
    expect(component.name).toBe('');
    expect(component.email).toBe('');
    expect(component.phoneNumber).toBe('');
    expect(component.isLoading).toBeFalse();
    expect(component.emailError).toBe('');
  });

  it('isValidEmail() should return true for a valid email', () => {
    expect(component.isValidEmail('user@example.com')).toBeTrue();
  });

  it('isValidEmail() should return false for an invalid email', () => {
    expect(component.isValidEmail('not-an-email')).toBeFalse();
    expect(component.isValidEmail('missing@domain')).toBeFalse();
    expect(component.isValidEmail('')).toBeFalse();
  });

  it('onClose() should call dialogRef.close()', () => {
    component.onClose();
    expect(dialogRef.close).toHaveBeenCalled();
  });

  it('onSubmit() should return early when fields are empty', () => {
    component.onSubmit();
    expect(component.isLoading).toBeFalse();
    httpMock.expectNone('/api/customers/');
  });

  it('onSubmit() should set emailError when email is invalid', () => {
    component.name = 'Alice';
    component.email = 'invalid-email';
    component.phoneNumber = '0600000000';

    component.onSubmit();

    expect(component.emailError).toBe('Veuillez entrer une adresse e-mail valide');
    expect(component.isLoading).toBeFalse();
    httpMock.expectNone('/api/customers/');
  });

  it('onSubmit() should reset emailError and post on valid input then close with success', () => {
    component.name = 'Alice';
    component.email = 'alice@example.com';
    component.phoneNumber = '0600000000';

    component.onSubmit();
    expect(component.emailError).toBe('');
    expect(component.isLoading).toBeTrue();

    httpMock.expectOne('/api/customers/').flush({ customerId: 'c-1' });

    expect(component.isLoading).toBeFalse();
    expect(dialogRef.close).toHaveBeenCalledWith({ success: true });
  });

  it('onSubmit() should close with false on HTTP error', () => {
    component.name = 'Alice';
    component.email = 'alice@example.com';
    component.phoneNumber = '0600000000';

    component.onSubmit();
    httpMock.expectOne('/api/customers/').error(new ProgressEvent('error'));

    expect(component.isLoading).toBeFalse();
    expect(dialogRef.close).toHaveBeenCalledWith({ success: false });
  });
});
