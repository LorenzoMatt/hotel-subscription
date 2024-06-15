import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'src/app/models/subscription.model';
import { NotificationService } from 'src/app/services/notification.service';
import { SubscriptionService } from 'src/app/services/subscription.service';

@Component({
  selector: 'app-subscription-details',
  templateUrl: './subscription-details.component.html',
  styleUrls: ['./subscription-details.component.css']
})
export class SubscriptionDetailsComponent implements OnInit {
  subscription: Subscription | undefined;
  canRestart: boolean = false;
  canCancel: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private subscriptionService: SubscriptionService,
    private notificationService: NotificationService
  ) {}

  ngOnInit(): void {
    const subscriptionId = this.route.snapshot.params['id'];
    if (subscriptionId) {
      this.loadSubscriptionDetails(subscriptionId);
    } else {
      this.router.navigate(['/subscriptions']);
    }
  }

  loadSubscriptionDetails(id: number): void {
    this.subscriptionService.getSubscriptionById(id).subscribe({
      next: (subscription) => {
        this.subscription = subscription;
        this.checkActiveSubscriptions(subscription.hotelId);
        this.canCancel = subscription.status === 'ACTIVE';
      },
      error: (error) => {
        console.error('Error loading subscription details', error);
        this.notificationService.showError(error);
        this.router.navigate(['/subscriptions']);
      }
    });
  }

  checkActiveSubscriptions(hotelId: number): void {
    this.subscriptionService.hasActiveSubscription(hotelId).subscribe({
      next: (hasActive) => {
        this.canRestart = !hasActive && (this.subscription?.status === 'CANCELED' || this.subscription?.status === 'EXPIRED');
      },
      error: (error) => {
        console.error('Error checking active subscriptions', error);
        this.notificationService.showError(error);
      }
    });
  }

  restartSubscription(): void {
    if (this.subscription) {
      this.subscriptionService.restartSubscription(this.subscription.id).subscribe(
        response => {
          this.notificationService.showSuccess('Subscription restarted successfully!');
          this.subscription = response;
          this.canCancel = this.subscription.status === 'ACTIVE';
          this.checkActiveSubscriptions(this.subscription.hotelId);
        },
        error => {
          console.error('Error restarting subscription', error);
          this.notificationService.showError(error);
        }
      );
    }
  }

  cancelSubscription(): void {
    if (this.subscription) {
      this.subscriptionService.cancelSubscription(this.subscription.id).subscribe(
        response => {
          this.notificationService.showSuccess('Subscription cancelled successfully!');
          this.subscription = response;
          this.canCancel = this.subscription.status === 'ACTIVE';
          this.checkActiveSubscriptions(this.subscription.hotelId);
        },
        error => {
          console.error('Error cancelling subscription', error);
          this.notificationService.showError(error);
        }
      );
    }
  }
}
