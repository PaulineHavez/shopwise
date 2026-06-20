import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AppointmentCreateForm } from './appointment-create-form';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { HttpClientModule, provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';

describe('AppointmentCreateForm', () => {
  let component: AppointmentCreateForm;
  let fixture: ComponentFixture<AppointmentCreateForm>;
  let httpMock: HttpTestingController;
  let dialogRef: jasmine.SpyObj<MatDialogRef<AppointmentCreateForm>>;

  beforeEach(async () => {
    dialogRef = jasmine.createSpyObj('MatDialogRef', ['close']);

    await TestBed.configureTestingModule({
      imports: [AppointmentCreateForm],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        { provide: MatDialogRef, useValue: dialogRef },
        { provide: MAT_DIALOG_DATA, useValue: { merchantId: 'merchant-1' } },
      ],
    })
    .overrideComponent(AppointmentCreateForm, { remove: { imports: [HttpClientModule] } })
    .compileComponents();

    fixture = TestBed.createComponent(AppointmentCreateForm);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
    fixture.detectChanges();
  });

  afterEach(() => httpMock.verify());

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize with default values', () => {
    expect(component.isLoading).toBeFalse();
    expect(component.searchError).toBe('');
    expect(component.customerEmail).toBe('');
    expect(component.startDate).toBeNull();
    expect(component.endDate).toBeNull();
  });

  it('onClose() should call dialogRef.close()', () => {
    component.onClose();
    expect(dialogRef.close).toHaveBeenCalled();
  });

  it('onSubmit() should not make HTTP requests when fields are empty', () => {
    component.onSubmit();
    expect(component.isLoading).toBeFalse();
    httpMock.expectNone((req) => req.url.includes('/api/'));
  });

  it('onSubmit() should set isLoading to true and close with success on full submission', () => {
    fillForm(component);
    component.onSubmit();
    expect(component.isLoading).toBeTrue();

    httpMock.expectOne('/api/customers/email/test@test.com').flush({ customerId: 'c-1' });
    httpMock.expectOne('/api/services/haircut').flush({ serviceId: 's-1' });
    httpMock.expectOne('/api/appointments/').flush({});

    expect(component.isLoading).toBeFalse();
    expect(dialogRef.close).toHaveBeenCalledWith({ success: true });
  });

  it('onSubmit() should set searchError when customer is null', () => {
    fillForm(component);
    component.onSubmit();

    httpMock.expectOne('/api/customers/email/test@test.com').flush(null);

    expect(component.searchError).toBe('Aucun client trouvé avec cet e-mail.');
    expect(component.isLoading).toBeFalse();
  });

  it('onSubmit() should set searchError when service is null', () => {
    fillForm(component);
    component.onSubmit();

    httpMock.expectOne('/api/customers/email/test@test.com').flush({ customerId: 'c-1' });
    httpMock.expectOne('/api/services/haircut').flush(null);

    expect(component.searchError).toBe('Aucun service trouvé avec ce nom.');
    expect(component.isLoading).toBeFalse();
  });

  it('onSubmit() should close with false on HTTP error', () => {
    fillForm(component);
    component.onSubmit();

    httpMock.expectOne('/api/customers/email/test@test.com').error(new ProgressEvent('error'));

    expect(component.isLoading).toBeFalse();
    expect(dialogRef.close).toHaveBeenCalledWith({ success: false });
  });

  function fillForm(c: AppointmentCreateForm): void {
    c.customerEmail = 'test@test.com';
    c.serviceName = 'haircut';
    c.startDate = new Date('2026-01-01');
    c.startTime = '10:00';
    c.endDate = new Date('2026-01-01');
    c.endTime = '11:00';
  }
});
