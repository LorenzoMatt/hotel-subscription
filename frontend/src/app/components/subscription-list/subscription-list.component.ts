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
        console.error('Failed to load subscriptions.', error);
        this.notificationService.showError(error);
      },
    });
  }

  viewDetails(subscription: Subscription): void {
    this.router.navigate(['/subscription', subscription.id]);
  }

  trackById(index: number, item: Subscription): any {
    return item.id;
  }
}
