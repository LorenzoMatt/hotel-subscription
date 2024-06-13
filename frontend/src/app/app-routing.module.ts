import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SubscriptionListComponent } from './components/subscription-list/subscription-list.component';

const routes: Routes = [
  { path: 'subscriptions', component: SubscriptionListComponent },
  { path: '', redirectTo: 'subscriptions', pathMatch: 'full' },
  // other routes
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
