import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ProSpaceLoginForm } from './proSpaceLoginForm';
import { MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { HttpClientModule, provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';

describe('ProSpaceLoginForm', () => {
  let component: ProSpaceLoginForm;
  let fixture: ComponentFixture<ProSpaceLoginForm>;
  let httpMock: HttpTestingController;
  let dialogRef: jasmine.SpyObj<MatDialogRef<ProSpaceLoginForm>>;
  let router: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    dialogRef = jasmine.createSpyObj('MatDialogRef', ['close']);
    router = jasmine.createSpyObj('Router', ['navigate']);

    await TestBed.configureTestingModule({
      imports: [ProSpaceLoginForm],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        { provide: MatDialogRef, useValue: dialogRef },
        { provide: Router, useValue: router },
      ],
    })
    .overrideComponent(ProSpaceLoginForm, { remove: { imports: [HttpClientModule] } })
    .compileComponents();

    fixture = TestBed.createComponent(ProSpaceLoginForm);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
    fixture.detectChanges();
  });

  afterEach(() => httpMock.verify());

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize with default values', () => {
    expect(component.email).toBe('');
    expect(component.password).toBe('');
    expect(component.isLoading).toBeFalse();
  });

  it('onClose() should call dialogRef.close()', () => {
    component.onClose();
    expect(dialogRef.close).toHaveBeenCalled();
  });

  it('onSubmit() should return early when fields are empty', () => {
    component.onSubmit();
    expect(component.isLoading).toBeFalse();
    httpMock.expectNone('/api/merchants/login');
  });

  it('onSubmit() should post and navigate on success', () => {
    component.email = 'merchant@test.com';
    component.password = 'secret';

    component.onSubmit();
    expect(component.isLoading).toBeTrue();

    httpMock.expectOne('/api/merchants/login').flush({ merchantId: 'm-1' });

    expect(component.isLoading).toBeFalse();
    expect(dialogRef.close).toHaveBeenCalledWith({ merchantId: 'm-1' });
    expect(router.navigate).toHaveBeenCalledWith(['/merchant', 'm-1']);
  });

  it('onSubmit() should set isLoading to false on HTTP error', () => {
    component.email = 'merchant@test.com';
    component.password = 'secret';

    component.onSubmit();
    httpMock.expectOne('/api/merchants/login').error(new ProgressEvent('error'));

    expect(component.isLoading).toBeFalse();
  });
});
