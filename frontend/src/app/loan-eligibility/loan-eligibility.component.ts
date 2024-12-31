import { Component } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http'; // Import HttpClientModule

@Component({
  selector: 'app-loan-eligibility',
  standalone: true,
  templateUrl: './loan-eligibility.component.html',
  styleUrls: ['./loan-eligibility.component.css'],
  imports: [FormsModule, CommonModule, HttpClientModule], // Add HttpClientModule here
})
export class LoanEligibilityComponent {
  // Form data
  formData = {
    firstName: '',
    lastName: '',
    age: null,
    annualIncome: null,
    creditScore: null,
    // existingDebts: null,
    existingLoans: null,
    totalDebt: null,
    createDate: null,
    employmentStatus: '',
  };

  eligibilityResult?: string; // Holds the result of the eligibility check
  splitEligibilityResult: string[] = []; // Holds the split string

  // Inject HttpClient
  constructor(private http: HttpClient) {}

  // Submit form
  onSubmit(): void {
    console.log('Sending customer data:', this.formData);

    // Send POST request to backend
    this.http
        .post('http://localhost:8080/api/loans/check-customer', this.formData, { responseType: 'text' })
      .subscribe(
        (response: string) => {
          console.log('Response received:', response);
          this.eligibilityResult = response; // Display plain text response
          // Split the result based on both '.' and ':' and filter out empty or whitespace-only strings
          this.splitEligibilityResult = response
            .split(/[.:]/) // Split by '.' or ':'
            .map(item => item.trim()) // Remove extra spaces
            .filter(item => item.length > 0); // Filter out empty strings
        },
        (error: HttpErrorResponse) => {
          console.error('Error occurred:', error);
          // Check if error contains a text message
          if (typeof error.error === 'string') {
            this.eligibilityResult = error.error; // Display the error message from the server
            this.splitEligibilityResult = error.error
              .split(/[.:]/)
              .map(item => item.trim())
              .filter(item => item.length > 0); // Filter empty strings
          } else {
            this.eligibilityResult = 'An unexpected error occurred. Please try again later.';
          }
        }
      );
  }
}
