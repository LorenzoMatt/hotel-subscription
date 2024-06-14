import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SubscriptionRequest } from 'src/app/models/subscription.request.model';
import { SubscriptionService } from 'src/app/services/subscription.service';
import { futureDateValidator } from 'src/app/validators/future-date.validator';

@Component({
  selector: 'app-add-subscription',
  templateUrl: './add-subscription.component.html',
  styleUrls: ['./add-subscription.component.css'],
})
export class AddSubscriptionComponent {
  subscriptionForm: FormGroup;
  minDate: Date;

  constructor(
    private fb: FormBuilder,
    private subscriptionService: SubscriptionService,
    private snackBar: MatSnackBar
  ) {
    const today = new Date();
    this.minDate = new Date(
      today.getFullYear(),
      today.getMonth(),
      today.getDate() + 1
    );

    this.subscriptionForm = this.fb.group({
      hotelId: ['', [Validators.required, Validators.min(1)]],
      startDate: ['', [Validators.required, futureDateValidator(this.minDate)]],
      term: ['', Validators.required],
    });
  }

  onAddSubscription() {
    if (this.subscriptionForm.invalid) {
      console.log('Form is not valid:', this.subscriptionForm.errors);
      return;
    }
    const localDate = new Date(this.subscriptionForm.value.startDate);
    localDate.setHours(23, 59, 59, 999);
    this.subscriptionForm.value.startDate = localDate.toISOString();

    const newSubscription: SubscriptionRequest = this.subscriptionForm.value;

    this.subscriptionService.startSubscription(newSubscription).subscribe({
      next: (res) => {
        this.snackBar.open('Subscription added successfully!', 'Close', {
          duration: 3000,
          horizontalPosition: 'center',
          verticalPosition: 'top',
          panelClass: ['success-snackbar'],
        });
        this.resetForm();
      },
      error: (err) => {
        this.snackBar.open(err, 'Close', {
          duration: 3000,
          horizontalPosition: 'center',
          verticalPosition: 'top',
          panelClass: ['error-snackbar'],
        });
      },
    });
  }

  private resetForm() {
    this.subscriptionForm.reset({
      hotelId: '',
      startDate: '',
      term: '',
    });
    Object.keys(this.subscriptionForm.controls).forEach((key) => {
      const control = this.subscriptionForm.get(key);
      control?.setErrors(null);
      control?.markAsPristine();
      control?.markAsUntouched();
    });
  }
}
