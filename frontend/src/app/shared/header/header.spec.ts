import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HeaderComponent } from './header';
import { MatDialog } from '@angular/material/dialog';

describe('HeaderComponent', () => {
  let component: HeaderComponent;
  let fixture: ComponentFixture<HeaderComponent>;
  let matDialog: jasmine.SpyObj<MatDialog>;

  beforeEach(async () => {
    matDialog = jasmine.createSpyObj('MatDialog', ['open']);
    matDialog.open.and.returnValue({} as any);

    await TestBed.configureTestingModule({
      imports: [HeaderComponent],
      providers: [
        { provide: MatDialog, useValue: matDialog },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(HeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('openProSpaceLoginForm() should open a dialog', () => {
    component.openProSpaceLoginForm();
    expect(matDialog.open).toHaveBeenCalled();
  });

  it('openCustomerSpaceLoginForm() should open a dialog', () => {
    component.openCustomerSpaceLoginForm();
    expect(matDialog.open).toHaveBeenCalled();
  });
});
