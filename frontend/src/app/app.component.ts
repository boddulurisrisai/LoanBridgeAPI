import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common'; // Import CommonModule

@Component({
  // standalone: true, // Standalone declaration
  imports: [RouterModule, CommonModule], // Add CommonModule here
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
    dropdownVisible: boolean = false;
  
    toggleDropdown() {
      this.dropdownVisible = !this.dropdownVisible;
    }
  
    viewAccount() {
      // Logic to view account information (e.g., navigate to account page)
      console.log('Viewing account information...');
      this.dropdownVisible = false; // Hide the dropdown after action
    }
  
    logout() {
      // Logic for logging out (e.g., clear session, redirect to login)
      console.log('Logging out...');
      this.dropdownVisible = false; // Hide the dropdown after action
    }
}
