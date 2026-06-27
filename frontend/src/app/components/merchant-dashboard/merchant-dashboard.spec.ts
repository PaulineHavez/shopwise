import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { MerchantDashboard } from './merchant-dashboard';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { of } from 'rxjs';

describe('MerchantDashboard', () => {
  let component: MerchantDashboard;
  let fixture: ComponentFixture<MerchantDashboard>;
  let httpMock: HttpTestingController;
  let matDialog: jasmine.SpyObj<MatDialog>;

  function setupDialogResult(result: any) {
    matDialog.open.and.returnValue({ afterClosed: () => of(result) } as any);
  }

  beforeEach(async () => {
    matDialog = jasmine.createSpyObj('MatDialog', ['open']);
    setupDialogResult(null);

    await TestBed.configureTestingModule({
      imports: [MerchantDashboard],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        { provide: MatDialog, useValue: matDialog },
        {
          provide: ActivatedRoute,
          useValue: { snapshot: { paramMap: { get: () => 'merchant-1' } } },
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(MerchantDashboard);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
    fixture.detectChanges();
  });

  afterEach(() => httpMock.verify());

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize merchantId from route params', () => {
    expect(component.merchantId).toBe('merchant-1');
  });

  it('showAppointmentsList should be false by default', () => {
    expect(component.showAppointmentsList()).toBeFalse();
  });

  it('toggleAppointmentsList() should toggle the signal', () => {
    component.toggleAppointmentsList();
    expect(component.showAppointmentsList()).toBeTrue();
    component.toggleAppointmentsList();
    expect(component.showAppointmentsList()).toBeFalse();
  });

  it('openCreateCustomer() should set success message on success result', fakeAsync(() => {
    setupDialogResult({ success: true });
    component.openCreateCustomer();
    tick(0);

    expect(component.alertMessage).toBe('Client créé avec succès !');
    expect(component.alertSuccess).toBeTrue();

    tick(3000);
    expect(component.alertMessage).toBe('');
  }));

  it('openCreateCustomer() should set error message on failure result', fakeAsync(() => {
    setupDialogResult({ success: false });
    component.openCreateCustomer();
    tick(0);

    expect(component.alertMessage).toBe('Erreur lors de la création du client.');
    expect(component.alertSuccess).toBeFalse();

    tick(3000);
    expect(component.alertMessage).toBe('');
  }));

  it('openCreateCustomer() should not set message when result is null', fakeAsync(() => {
    setupDialogResult(null);
    component.openCreateCustomer();
    tick(3000);

    expect(component.alertMessage).toBe('');
  }));

  it('openEditCustomer() should set success message on success result', fakeAsync(() => {
    setupDialogResult({ success: true });
    component.openEditCustomer();
    tick(0);

    expect(component.alertMessage).toBe('Client mis à jour avec succès !');
    expect(component.alertSuccess).toBeTrue();

    tick(3000);
    expect(component.alertMessage).toBe('');
  }));

  it('openEditCustomer() should set error message on failure result', fakeAsync(() => {
    setupDialogResult({ success: false });
    component.openEditCustomer();
    tick(0);

    expect(component.alertMessage).toBe('Erreur lors de la mise à jour du client.');
    expect(component.alertSuccess).toBeFalse();

    tick(3000);
  }));

  it('openCreateAppointment() should set success message on success result', fakeAsync(() => {
    setupDialogResult({ success: true });
    component.openCreateAppointment();
    tick(0);

    expect(component.alertMessage).toBe('Rendez-vous créé avec succès !');
    expect(component.alertSuccess).toBeTrue();

    tick(3000);
    expect(component.alertMessage).toBe('');
  }));

  it('openCreateAppointment() should set error message on failure result', fakeAsync(() => {
    setupDialogResult({ success: false });
    component.openCreateAppointment();
    tick(0);

    expect(component.alertMessage).toBe('Erreur lors de la création du rendez-vous.');
    expect(component.alertSuccess).toBeFalse();

    tick(3000);
  }));

  it('appointmentStatusUpdated() should set success message', fakeAsync(() => {
    component.appointmentStatusUpdated({ success: true });
    tick(0);

    expect(component.alertMessage).toBe('Statut du rendez-vous mis à jour avec succès !');
    expect(component.alertSuccess).toBeTrue();

    tick(3000);
    expect(component.alertMessage).toBe('');
  }));

  it('appointmentStatusUpdated() should set error message', fakeAsync(() => {
    component.appointmentStatusUpdated({ success: false });
    tick(0);

    expect(component.alertMessage).toBe('Erreur lors de la mise à jour du statut du rendez-vous.');
    expect(component.alertSuccess).toBeFalse();

    tick(3000);
  }));
});
