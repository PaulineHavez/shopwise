import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CustomerDashboard } from './customer-dashboard';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { of } from 'rxjs';

describe('CustomerDashboard', () => {
  let component: CustomerDashboard;
  let fixture: ComponentFixture<CustomerDashboard>;
  let httpMock: HttpTestingController;
  let matDialog: jasmine.SpyObj<MatDialog>;

  beforeEach(async () => {
    matDialog = jasmine.createSpyObj('MatDialog', ['open']);
    matDialog.open.and.returnValue({ afterClosed: () => of(null) } as any);

    await TestBed.configureTestingModule({
      imports: [CustomerDashboard],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        { provide: MatDialog, useValue: matDialog },
        {
          provide: ActivatedRoute,
          useValue: { snapshot: { paramMap: { get: () => 'customer-1' } } },
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(CustomerDashboard);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
    fixture.detectChanges();
  });

  afterEach(() => httpMock.verify());

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize customerId from route params', () => {
    expect(component.customerId).toBe('customer-1');
  });

  it('showTransactionsList should be false by default', () => {
    expect(component.showTransactionsList()).toBeFalse();
  });

  it('toggleTransactionsList() should toggle the signal', () => {
    component.toggleTransactionsList();
    expect(component.showTransactionsList()).toBeTrue();
    component.toggleTransactionsList();
    expect(component.showTransactionsList()).toBeFalse();
  });

  it('openCustomerData() should open MatDialog', () => {
    component.openCustomerData();
    expect(matDialog.open).toHaveBeenCalled();
  });

  it('openEarnedPoints() should open MatDialog', () => {
    component.openEarnedPoints();
    expect(matDialog.open).toHaveBeenCalled();
  });
});
