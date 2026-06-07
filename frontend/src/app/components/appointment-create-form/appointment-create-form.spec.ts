import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AppointmentCreateForm } from './appointment-create-form';

describe('AppointmentCreateForm', () => {
  let component: AppointmentCreateForm;
  let fixture: ComponentFixture<AppointmentCreateForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AppointmentCreateForm]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AppointmentCreateForm);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
