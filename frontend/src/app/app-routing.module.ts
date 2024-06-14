import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AddSubscriptionComponent } from './components/add-subscription/add-subscription.component';
import { SubscriptionListComponent } from './components/subscription-list/subscription-list.component';

const routes: Routes = [
  { path: 'add-subscription', component: AddSubscriptionComponent },
  { path: 'subscriptions', component: SubscriptionListComponent },
  { path: '', redirectTo: 'subscriptions', pathMatch: 'full' },
  // other routes
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
