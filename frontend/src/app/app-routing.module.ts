import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoanEligibilityComponent } from './loan-eligibility/loan-eligibility.component';
import { MaxloanComponent } from './maxloan/maxloan.component';

const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' }, // Default route
  { path: 'loan-eligibility', component: LoanEligibilityComponent },
  { path: 'maxloan', component: MaxloanComponent },  // Route for maxloan page
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
