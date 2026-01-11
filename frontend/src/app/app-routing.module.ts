import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { BenefitListComponent } from './components/benefit-list/benefit-list.component';
import { PaymentFormComponent } from './components/payment-form/payment-form.component';

const routes: Routes = [
  { path: '', redirectTo: '/benefits', pathMatch: 'full' },
  { path: 'benefits', component: BenefitListComponent },
  { path: 'payment', component: PaymentFormComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
