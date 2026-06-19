import { ChangeDetectionStrategy, Component, Inject } from '@angular/core';
import { httpResource } from '@angular/common/http';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatDialogModule } from '@angular/material/dialog';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-customer-earned-points',
  imports: [MatDialogModule, MatProgressSpinnerModule, MatButtonModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './customer-earned-points.html',
  styleUrl: './customer-earned-points.scss',
})
export class CustomerEarnedPoints {
  readonly earnedPointsResource = httpResource<number>(() =>
    `/api/customers/${this.data.customerId}/earnedPoints/`
  );

  constructor(@Inject(MAT_DIALOG_DATA) public data: { customerId: string }) {}
}
