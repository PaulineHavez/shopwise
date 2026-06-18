import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomerLoginForm } from './customer-login-form';

describe('CustomerLoginForm', () => {
  let component: CustomerLoginForm;
  let fixture: ComponentFixture<CustomerLoginForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CustomerLoginForm]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CustomerLoginForm);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
