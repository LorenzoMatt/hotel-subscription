import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Subscription } from '../models/subscription.model';
import { SubscriptionRequest } from '../models/subscription.request.model';

@Injectable({
  providedIn: 'root'
})
export class SubscriptionService {
  private apiUrl = `${environment.apiUrl}/api/subscriptions`;

  constructor(private http: HttpClient) { }

  getAllSubscriptions(): Observable<Subscription[]> {
    return this.http.get<Subscription[]>(this.apiUrl);
  }

  startSubscription(subscription: SubscriptionRequest): Observable<Subscription> {
    return this.http.post<Subscription>(this.apiUrl, subscription);
  }

  cancelSubscription(id: number): Observable<Subscription> {
    return this.http.post<Subscription>(`${this.apiUrl}/${id}/cancel`, {});
  }

  restartSubscription(id: number): Observable<Subscription> {
    return this.http.post<Subscription>(`${this.apiUrl}/${id}/restart`, {});
  }

  hasActiveSubscription(hotelId: number): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/has-active/${hotelId}`);
  }

  getSubscriptionById(id: number): Observable<Subscription> {
    return this.http.get<Subscription>(`${this.apiUrl}/${id}`, {});
  }

  getSubscriptionsByStatus(status: string): Observable<Subscription[]> {
    return this.http.get<Subscription[]>(`${this.apiUrl}/status`, { params: { status } });
  }

  getSubscriptionsByMonth(month: number): Observable<Subscription[]> {
    return this.http.get<Subscription[]>(`${this.apiUrl}/month`, { params: { month } });
  }
}
