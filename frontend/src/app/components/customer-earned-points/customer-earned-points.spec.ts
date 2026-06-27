import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CustomerEarnedPoints } from './customer-earned-points';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';

describe('CustomerEarnedPoints', () => {
  let component: CustomerEarnedPoints;
  let fixture: ComponentFixture<CustomerEarnedPoints>;
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CustomerEarnedPoints],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        { provide: MAT_DIALOG_DATA, useValue: { customerId: 'customer-1' } },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(CustomerEarnedPoints);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
    fixture.detectChanges();
    httpMock
      .match((req) => req.url.includes('/api/customers/customer-1/earnedPoints'))
      .forEach((r) => r.flush(150));
  });

  afterEach(() => httpMock.verify());

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have customerId from injected data', () => {
    expect(component.data.customerId).toBe('customer-1');
  });
});
