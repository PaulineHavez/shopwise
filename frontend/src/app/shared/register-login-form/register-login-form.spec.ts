import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegistrerLoginForm } from './registrer-login-form';

describe('RegistrerLoginForm', () => {
  let component: RegistrerLoginForm;
  let fixture: ComponentFixture<RegistrerLoginForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegistrerLoginForm]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegistrerLoginForm);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
