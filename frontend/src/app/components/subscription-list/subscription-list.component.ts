import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import { Subscription } from 'src/app/models/subscription.model';
import { NotificationService } from 'src/app/services/notification.service';
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
    private notificationService: NotificationService,
    private changeDetector: ChangeDetectorRef,
    private router: Router
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
        this.notificationService.showSnackBar('Failed to load subscriptions.', 'Close', 3000, 'error-snackbar');
      },
    });
  }

  cancelSubscription(id: number): void {
    this.subscriptionService.cancelSubscription(id).subscribe(
      (response) => {
        this.updateDataSource(response, id);
        this.notificationService.showSnackBar('Subscription cancelled successfully!', 'Close', 3000, 'success-snackbar');
      },
      (error) => {
        this.notificationService.showSnackBar('Error cancelling subscription', 'Close', 3000, 'error-snackbar');
        console.error('Error cancelling subscription', error);
      }
    );
  }

  restartSubscription(id: number): void {
    this.subscriptionService.restartSubscription(id).subscribe(
      (response) => {
        this.updateDataSource(response, id);
        this.notificationService.showSnackBar('Subscription restarted successfully!', 'Close', 3000, 'success-snackbar');
      },
      (error) => {
        this.notificationService.showSnackBar('Error restarting subscription', 'Close', 3000, 'error-snackbar');
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
