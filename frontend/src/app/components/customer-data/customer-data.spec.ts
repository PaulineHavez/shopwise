import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomerData } from './customer-data';

describe('CustomerData', () => {
  let component: CustomerData;
  let fixture: ComponentFixture<CustomerData>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CustomerData]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CustomerData);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
