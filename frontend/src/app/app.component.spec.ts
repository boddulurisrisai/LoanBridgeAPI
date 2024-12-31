import { Component } from '@angular/core';

@Component({
  selector: 'app-loan-eligibility',
  templateUrl: './loan-eligibility.component.html',
  styleUrls: ['./loan-eligibility.component.css']
})
export class LoanEligibilityComponent {
  eligibilityResult: string | null = null;

  // Method to handle form submission
  onSubmit(): void {
    const income = parseFloat((<HTMLInputElement>document.getElementById('income')).value);
    const creditScore = parseFloat((<HTMLInputElement>document.getElementById('creditScore')).value);
    const loanAmount = parseFloat((<HTMLInputElement>document.getElementById('loanAmount')).value);

    if (income > 3000 && creditScore > 650 && loanAmount <= 100000) {
      this.eligibilityResult = 'Congratulations, you are eligible for the loan!';
    } else {
      this.eligibilityResult = 'Sorry, you are not eligible for the loan.';
    }
  }
}
