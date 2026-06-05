import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomerEditForm } from './customer-edit-form';

describe('CustomerEditForm', () => {
  let component: CustomerEditForm;
  let fixture: ComponentFixture<CustomerEditForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CustomerEditForm]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CustomerEditForm);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
