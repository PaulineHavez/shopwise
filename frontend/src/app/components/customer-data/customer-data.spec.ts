import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CustomerData } from './customer-data';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { HttpClientModule, provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';

describe('CustomerData', () => {
  let component: CustomerData;
  let fixture: ComponentFixture<CustomerData>;
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CustomerData],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        { provide: MAT_DIALOG_DATA, useValue: { customerId: 'customer-1' } },
      ],
    })
    .overrideComponent(CustomerData, { remove: { imports: [HttpClientModule] } })
    .compileComponents();

    fixture = TestBed.createComponent(CustomerData);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  it('should create', () => {
    fixture.detectChanges();
    httpMock.expectOne('/api/customers/customer-1').flush({ name: 'Alice', email: 'alice@test.com', phoneNumber: '0600' });
    expect(component).toBeTruthy();
  });

  it('ngOnInit() should set isLoading to true then populate fields on success', () => {
    fixture.detectChanges();
    expect(component.isLoading).toBeTrue();

    httpMock.expectOne('/api/customers/customer-1').flush({
      name: 'Alice',
      email: 'alice@test.com',
      phoneNumber: '0600000000',
    });

    expect(component.isLoading).toBeFalse();
    expect(component.name).toBe('Alice');
    expect(component.email).toBe('alice@test.com');
    expect(component.phoneNumber).toBe('0600000000');
  });

  it('ngOnInit() should handle null customer response', () => {
    fixture.detectChanges();
    httpMock.expectOne('/api/customers/customer-1').flush(null);

    expect(component.isLoading).toBeFalse();
    expect(component.name).toBe('');
  });

  it('ngOnInit() should handle HTTP error', () => {
    fixture.detectChanges();
    httpMock.expectOne('/api/customers/customer-1').error(new ProgressEvent('error'));

    expect(component.isLoading).toBeFalse();
  });
});
