import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { AppointmentsList } from './appointments-list';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideNativeDateAdapter } from '@angular/material/core';

describe('AppointmentsList', () => {
  let component: AppointmentsList;
  let fixture: ComponentFixture<AppointmentsList>;
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AppointmentsList],
      providers: [
        provideNativeDateAdapter(),
        provideHttpClient(),
        provideHttpClientTesting(),
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(AppointmentsList);
    component = fixture.componentInstance;
    fixture.componentRef.setInput('merchantId', 'merchant-1');
    httpMock = TestBed.inject(HttpTestingController);
    fixture.detectChanges();
    httpMock.expectOne('/api/appointments/merchant-1').flush([]);
  });

  afterEach(() => httpMock.verify());

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have default signal values', () => {
    expect(component.editingAppointmentId()).toBeNull();
    expect(component.dateFilter()).toBeNull();
    expect(component.statusFilter()).toBeNull();
    expect(component.emailFilter()).toBeNull();
  });

  it('editStatus() should set editingAppointmentId', () => {
    component.editStatus('appt-42');
    expect(component.editingAppointmentId()).toBe('appt-42');
  });

  it('updateStatus() success should reset editingAppointmentId and emit success', fakeAsync(() => {
    const emitted: { success: boolean }[] = [];
    component.statusUpdated.subscribe((v) => emitted.push(v));

    component.editStatus('appt-1');
    component.updateStatus('appt-1', 'COMPLETED');

    const putReq = httpMock.expectOne('/api/appointments/appt-1');
    expect(putReq.request.method).toBe('PUT');
    expect(putReq.request.body).toEqual({ status: 'COMPLETED' });
    putReq.flush({});

    tick(0);
    httpMock.match((req) => req.url.includes('/api/appointments/merchant-1')).forEach((r) => r.flush([]));

    expect(component.editingAppointmentId()).toBeNull();
    expect(emitted).toEqual([{ success: true }]);
  }));

  it('updateStatus() error should reset editingAppointmentId and emit failure', () => {
    const emitted: { success: boolean }[] = [];
    component.statusUpdated.subscribe((v) => emitted.push(v));

    component.editStatus('appt-1');
    component.updateStatus('appt-1', 'CANCELLED');

    httpMock.expectOne('/api/appointments/appt-1').error(new ProgressEvent('error'));

    expect(component.editingAppointmentId()).toBeNull();
    expect(emitted).toEqual([{ success: false }]);
  });

  it('onEmailChange() with a value should set emailFilter', () => {
    component.onEmailChange('user@test.com');
    expect(component.emailFilter()).toBe('user@test.com');
    httpMock.match((req) => req.url.includes('/api/appointments/merchant-1')).forEach((r) => r.flush([]));
  });

  it('onEmailChange() with empty string should set emailFilter to null', () => {
    component.onEmailChange('');
    expect(component.emailFilter()).toBeNull();
  });

  it('resetFilters() should reset all filters to null', fakeAsync(() => {
    component.onEmailChange('user@test.com');
    httpMock.match((req) => req.url.includes('/api/appointments/merchant-1')).forEach((r) => r.flush([]));

    component.resetFilters();
    tick(0);
    httpMock.match((req) => req.url.includes('/api/appointments/merchant-1')).forEach((r) => r.flush([]));

    expect(component.dateFilter()).toBeNull();
    expect(component.statusFilter()).toBeNull();
    expect(component.emailFilter()).toBeNull();
  }));
});
