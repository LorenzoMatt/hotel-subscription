import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatTableDataSource } from '@angular/material/table';
import { Subscription } from 'src/app/models/subscription.model';
import { SubscriptionService } from 'src/app/services/subscription.service';

@Component({
  selector: 'app-subscription-list',
  templateUrl: './subscription-list.component.html',
  styleUrls: ['./subscription-list.component.css'],
})
export class SubscriptionListComponent implements OnInit {
  dataSource = new MatTableDataSource<Subscription>([]);
  displayedColumns: string[] = [
    'hotelId',
    'startDate',
    'nextPayment',
    'status',
    'actions',
  ];

  @ViewChild(MatPaginator, { static: true }) paginator!: MatPaginator;

  constructor(
    private subscriptionService: SubscriptionService,
    private snackBar: MatSnackBar,
    private changeDetector: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadSubscriptions();
  }

  loadSubscriptions(): void {
    this.subscriptionService.getAllSubscriptions().subscribe({
      next: (data) => {
        this.dataSource.data = data;
        this.dataSource.paginator = this.paginator;
        this.changeDetector.detectChanges(); // Ensures paginator is updated whenever data changes
      },
      error: (error) => {
        console.error('Failed to load subscriptions.', error);
        this.snackBar.open(error, 'Close', {
          duration: 3000,
          horizontalPosition: 'center',
          verticalPosition: 'top',
          panelClass: ['error-snackbar'],
        });
      },
    });
  }

  cancelSubscription(id: number): void {
    this.subscriptionService.cancelSubscription(id).subscribe(
      (response) => {
        this.updateDataSource(response, id);
        this.snackBar.open('Subscription cancelled successfully!', 'Close', {
          duration: 3000,
          horizontalPosition: 'center',
          verticalPosition: 'top',
          panelClass: ['success-snackbar'],
        });
      },
      (error) => {
        this.snackBar.open(error, 'Close', {
          duration: 3000,
          horizontalPosition: 'center',
          verticalPosition: 'top',
          panelClass: ['error-snackbar'],
        });
        console.error('Error cancelling subscription', error);
      }
    );
  }

  restartSubscription(id: number): void {
    this.subscriptionService.restartSubscription(id).subscribe(
      (response) => {
        this.updateDataSource(response, id);
        this.snackBar.open('Subscription restarted successfully!', 'Close', {
          duration: 3000,
          horizontalPosition: 'center',
          verticalPosition: 'top',
          panelClass: ['success-snackbar'],
        });
      },
      (error) => {
        this.snackBar.open(error, 'Close', {
          duration: 3000,
          horizontalPosition: 'center',
          verticalPosition: 'top',
          panelClass: ['error-snackbar'],
        });
        console.error('Error restarting subscription', error);
      }
    );
  }

  updateDataSource(response: Subscription, id: number): void {
    const index = this.dataSource.data.findIndex((sub) => sub.id === id);
    if (index > -1) {
      this.dataSource.data[index] = response;
      this.dataSource.data = [...this.dataSource.data]; // Refresh the dataSource
    }
  }

  trackById(index: number, item: Subscription): any {
    return item.id;
  }
}
