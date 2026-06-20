import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CustomerEditForm } from './customer-edit-form';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { HttpClientModule, provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';

describe('CustomerEditForm', () => {
  let component: CustomerEditForm;
  let fixture: ComponentFixture<CustomerEditForm>;
  let httpMock: HttpTestingController;
  let dialogRef: jasmine.SpyObj<MatDialogRef<CustomerEditForm>>;

  beforeEach(async () => {
    dialogRef = jasmine.createSpyObj('MatDialogRef', ['close']);

    await TestBed.configureTestingModule({
      imports: [CustomerEditForm],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        { provide: MatDialogRef, useValue: dialogRef },
        { provide: MAT_DIALOG_DATA, useValue: { merchantId: 'merchant-1' } },
      ],
    })
    .overrideComponent(CustomerEditForm, { remove: { imports: [HttpClientModule] } })
    .compileComponents();

    fixture = TestBed.createComponent(CustomerEditForm);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
    fixture.detectChanges();
  });

  afterEach(() => httpMock.verify());

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize with default values', () => {
    expect(component.customerFound).toBeFalse();
    expect(component.isLoading).toBeFalse();
    expect(component.searchError).toBe('');
    expect(component.emailError).toBe('');
  });

  it('isValidEmail() should return true for valid email', () => {
    expect(component.isValidEmail('user@example.com')).toBeTrue();
  });

  it('isValidEmail() should return false for invalid email', () => {
    expect(component.isValidEmail('not-an-email')).toBeFalse();
    expect(component.isValidEmail('')).toBeFalse();
  });

  it('onClose() should call dialogRef.close()', () => {
    component.onClose();
    expect(dialogRef.close).toHaveBeenCalled();
  });

  it('onSearch() should return early when searchEmail is empty', () => {
    component.searchEmail = '';
    component.onSearch();
    expect(component.isSearching).toBeFalse();
    httpMock.expectNone((req) => req.url.includes('/api/customers/email/'));
  });

  it('onSearch() should populate fields on success', () => {
    component.searchEmail = 'alice@test.com';
    component.onSearch();
    expect(component.isSearching).toBeTrue();

    httpMock.expectOne('/api/customers/email/alice@test.com').flush({
      customerId: 'c-1',
      name: 'Alice',
      email: 'alice@test.com',
      phoneNumber: '0600000000',
    });

    expect(component.isSearching).toBeFalse();
    expect(component.customerFound).toBeTrue();
    expect(component.name).toBe('Alice');
    expect(component.email).toBe('alice@test.com');
    expect(component.customerId).toBe('c-1');
  });

  it('onSearch() should set searchError when customer is null', () => {
    component.searchEmail = 'nobody@test.com';
    component.onSearch();

    httpMock.expectOne('/api/customers/email/nobody@test.com').flush(null);

    expect(component.customerFound).toBeFalse();
    expect(component.searchError).toBe('Aucun client trouvé avec cet e-mail.');
  });

  it('onSearch() should set searchError on HTTP error', () => {
    component.searchEmail = 'alice@test.com';
    component.onSearch();

    httpMock.expectOne('/api/customers/email/alice@test.com').error(new ProgressEvent('error'));

    expect(component.isSearching).toBeFalse();
    expect(component.searchError).toBe('Aucun client trouvé avec cet e-mail.');
  });

  it('onSubmit() should return early when fields are empty', () => {
    component.onSubmit();
    httpMock.expectNone((req) => req.url.includes('/api/customers/'));
  });

  it('onSubmit() should set emailError when email is invalid', () => {
    component.name = 'Alice';
    component.email = 'bad-email';
    component.phoneNumber = '0600000000';
    component.customerId = 'c-1';

    component.onSubmit();

    expect(component.emailError).toBe('Veuillez entrer une adresse e-mail valide');
    expect(component.isLoading).toBeFalse();
    httpMock.expectNone((req) => req.url.includes('/api/customers/c-1'));
  });

  it('onSubmit() should PUT and close with success on valid input', () => {
    component.name = 'Alice';
    component.email = 'alice@example.com';
    component.phoneNumber = '0600000000';
    component.customerId = 'c-1';

    component.onSubmit();
    expect(component.isLoading).toBeTrue();

    const req = httpMock.expectOne('/api/customers/c-1');
    expect(req.request.method).toBe('PUT');
    req.flush({});

    expect(component.isLoading).toBeFalse();
    expect(dialogRef.close).toHaveBeenCalledWith({ success: true });
  });

  it('onSubmit() should close with false on HTTP error', () => {
    component.name = 'Alice';
    component.email = 'alice@example.com';
    component.phoneNumber = '0600000000';
    component.customerId = 'c-1';

    component.onSubmit();
    httpMock.expectOne('/api/customers/c-1').error(new ProgressEvent('error'));

    expect(component.isLoading).toBeFalse();
    expect(dialogRef.close).toHaveBeenCalledWith({ success: false });
  });
});
