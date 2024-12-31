import { Component } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-maxloan',
  templateUrl: './maxloan.component.html',
  styleUrls: ['./maxloan.component.css'],
  standalone: true, // If this is a standalone component
  imports: [CommonModule, FormsModule, HttpClientModule],
})
export class MaxloanComponent {
  // Form fields
  totalDebt: number = 0;
  annualIncome: number = 0;
  creditScore: number = 0;

  // Response fields
  eligibilityResult: string = '';
  splitEligibilityResult: string[] = [];

  // Error handling
  errorMessage: string = '';

  // API endpoint
  private apiUrl = 'http://localhost:8080/api/loans/calculate-max-loan';

  constructor(private http: HttpClient) {}

  // Method to calculate the max loan amount
  onCalculateLoan(): void {
    // Create the request payload
    const requestBody = {
      totalDebt: this.totalDebt,
      annualIncome: this.annualIncome,
      creditScore: this.creditScore,
    };

    // Make the POST request to the backend API
    this.http
      .post(this.apiUrl, requestBody, { responseType: 'text' }) // Expect plain text response
      .subscribe({
        next: (response: string) => {
          console.log('Response received:', response);
          this.eligibilityResult = response; // Store the raw response
          // Split the result by '.' or ':', filter out empty or whitespace-only strings
          this.splitEligibilityResult = response
            .split(/[.:]/)
            .map(item => item.trim()) // Remove extra spaces
            .filter(item => item.length > 0); // Remove empty strings
        },
        error: (error: HttpErrorResponse) => {
          console.error('Error occurred:', error);
          // Handle error response
          if (typeof error.error === 'string') {
            this.eligibilityResult = error.error; // Store error message
            this.splitEligibilityResult = error.error
              .split(/[.:]/)
              .map(item => item.trim())
              .filter(item => item.length > 0);
          } else {
            this.eligibilityResult =
              'An unexpected error occurred. Please try again later.';
            this.splitEligibilityResult = []; // Clear the split results
          }
        },
      });
  }
}