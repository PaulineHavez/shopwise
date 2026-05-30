import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomerCreateForm } from './customer-create-form';

describe('CustomerCreateForm', () => {
  let component: CustomerCreateForm;
  let fixture: ComponentFixture<CustomerCreateForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CustomerCreateForm]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CustomerCreateForm);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
