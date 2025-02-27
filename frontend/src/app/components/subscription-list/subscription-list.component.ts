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
  displayedColumns: string[] = ['hotelId', 'startDate', 'nextPayment', 'status', 'actions'];

  statusOptions: string[] = ['ACTIVE', 'EXPIRED', 'CANCELED'];
  monthOptions: number[] = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12];

  selectedStatus: string | null = null;
  selectedMonth: number | null = null;

  @ViewChild(MatPaginator, { static: true }) paginator!: MatPaginator;

  constructor(
    private subscriptionService: SubscriptionService,
    private changeDetector: ChangeDetectorRef,
    private router: Router,
    private notificationService: NotificationService
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
        console.error('Failed to load subscriptions', error);
        this.notificationService.showError(error);
      },
    });
  }

  onStatusChange(event: any): void {
    this.selectedMonth = null;
    if (event.value) {
      this.subscriptionService.getSubscriptionsByStatus(event.value).subscribe({
        next: (data) => {
          this.dataSource.data = data;
          this.changeDetector.detectChanges();
        },
        error: (error) => {
          console.error('Failed to load subscriptions by status', error);
          this.notificationService.showError(error);
        },
      });
    } else {
      this.loadSubscriptions();
    }
  }

  onMonthChange(event: any): void {
    this.selectedStatus = null;
    if (event.value) {
      this.subscriptionService.getSubscriptionsByMonth(event.value).subscribe({
        next: (data) => {
          this.dataSource.data = data;
          this.changeDetector.detectChanges();
        },
        error: (error) => {
          console.error('Failed to load subscriptions by month', error);
          this.notificationService.showError(error);
        },
      });
    } else {
      this.loadSubscriptions();
    }
  }

  resetFilters(): void {
    this.selectedStatus = null;
    this.selectedMonth = null;
    this.loadSubscriptions();
  }

  viewDetails(subscription: Subscription): void {
    this.router.navigate(['/subscription', subscription.id]);
  }

  trackById(index: number, item: Subscription): any {
    return item.id;
  }
}
