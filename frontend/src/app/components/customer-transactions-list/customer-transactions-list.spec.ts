import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CustomerTransactionsList } from './customer-transactions-list';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';

describe('CustomerTransactionsList', () => {
  let component: CustomerTransactionsList;
  let fixture: ComponentFixture<CustomerTransactionsList>;
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CustomerTransactionsList],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(CustomerTransactionsList);
    component = fixture.componentInstance;
    fixture.componentRef.setInput('customerId', 'customer-1');
    httpMock = TestBed.inject(HttpTestingController);
    fixture.detectChanges();
    httpMock
      .match((req) => req.url.includes('/api/transactions/customer-1'))
      .forEach((r) => r.flush([]));
  });

  afterEach(() => httpMock.verify());

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should expose correct displayedColumns', () => {
    expect(component.displayedColumns).toEqual(['transaction_date', 'status', 'earnedPoints']);
  });

  it('should use customerId from input', () => {
    expect(component.customerId()).toBe('customer-1');
  });
});
