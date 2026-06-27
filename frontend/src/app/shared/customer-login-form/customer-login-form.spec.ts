import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { CustomerLoginForm } from './customer-login-form';
import { MatDialogRef, MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { HttpClientModule, provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';

describe('CustomerLoginForm', () => {
  let component: CustomerLoginForm;
  let fixture: ComponentFixture<CustomerLoginForm>;
  let httpMock: HttpTestingController;
  let dialogRef: jasmine.SpyObj<MatDialogRef<CustomerLoginForm>>;
  let matDialog: jasmine.SpyObj<MatDialog>;
  let router: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    dialogRef = jasmine.createSpyObj('MatDialogRef', ['close', 'afterClosed']);
    dialogRef.afterClosed.and.returnValue(of(null));
    matDialog = jasmine.createSpyObj('MatDialog', ['open']);
    matDialog.open.and.returnValue({ afterClosed: () => of(null) } as any);
    router = jasmine.createSpyObj('Router', ['navigate']);

    await TestBed.configureTestingModule({
      imports: [CustomerLoginForm],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        { provide: MatDialogRef, useValue: dialogRef },
        { provide: MatDialog, useValue: matDialog },
        { provide: Router, useValue: router },
      ],
    })
    .overrideComponent(CustomerLoginForm, {
      remove: { imports: [HttpClientModule] },
      add: { providers: [{ provide: MatDialog, useValue: matDialog }] },
    })
    .compileComponents();

    fixture = TestBed.createComponent(CustomerLoginForm);
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
    expect(component.isLoading).toBeFalse();
  });

  it('onClose() should call dialogRef.close()', () => {
    component.onClose();
    expect(dialogRef.close).toHaveBeenCalled();
  });

  it('onSubmit() should return early when fields are empty', () => {
    component.onSubmit();
    expect(component.isLoading).toBeFalse();
    httpMock.expectNone('/api/customers/login');
  });

  it('onSubmit() should post and navigate on success', () => {
    component.email = 'alice@test.com';
    component.password = 'secret';

    component.onSubmit();
    expect(component.isLoading).toBeTrue();

    httpMock.expectOne('/api/customers/login').flush({ customerId: 'c-1' });

    expect(component.isLoading).toBeFalse();
    expect(dialogRef.close).toHaveBeenCalledWith({ customerId: 'c-1' });
    expect(router.navigate).toHaveBeenCalledWith(['/customer', 'c-1']);
  });

  it('onSubmit() should set isLoading to false on HTTP error', () => {
    component.email = 'alice@test.com';
    component.password = 'secret';

    component.onSubmit();
    httpMock.expectOne('/api/customers/login').error(new ProgressEvent('error'));

    expect(component.isLoading).toBeFalse();
  });

  it('openRegisterDialog() should close current dialog and open RegisterLoginForm', () => {
    component.openRegisterDialog();
    expect(dialogRef.close).toHaveBeenCalled();
    expect(matDialog.open).toHaveBeenCalled();
  });
});
