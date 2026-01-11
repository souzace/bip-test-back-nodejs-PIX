import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { BenefitListComponent } from './components/benefit-list/benefit-list.component';

const routes: Routes = [
  { path: '', redirectTo: '/benefits', pathMatch: 'full' },
  { path: 'benefits', component: BenefitListComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
