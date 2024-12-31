package com.example.loanmanagement.controller;

import com.example.loanmanagement.entity.Customer;
import com.example.loanmanagement.service.LoanEligibilityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LoanEligibilityControllerTest {

    @Mock
    private LoanEligibilityService loanEligibilityService;

    @InjectMocks
    private LoanEligibilityController loanEligibilityController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks before each test
    }

    @Test
    void testCheckLoanEligibility_PositiveCase() {
        // Arrange
        Customer mockCustomer = createMockCustomer("BARBARA", "JONES", 29, 900, "Employed", 3000, 8000);
        mockCustomer.setCreateDate(java.time.LocalDate.of(2020, 6, 15));

        when(loanEligibilityService.getCustomerByName("BARBARA", "JONES"))
                .thenReturn(Optional.of(mockCustomer));
        when(loanEligibilityService.checkLoanEligibility(mockCustomer))
                .thenReturn("Approved: Customer is eligible for the loan.");

        // Act
        ResponseEntity<String> response = loanEligibilityController.checkLoanEligibility(mockCustomer);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Approved: Customer is eligible for the loan.", response.getBody());

        verify(loanEligibilityService).getCustomerByName("BARBARA", "JONES");
        verify(loanEligibilityService).updateCustomerCreditScore("BARBARA", "JONES", 900);
        verify(loanEligibilityService).checkLoanEligibility(mockCustomer);
    }

    @Test
    void testCheckLoanEligibility_NegativeCase() {
        // Arrange
        Customer mockCustomer = createMockCustomer("JOHN", "DOE", 17, 500, "Unemployed", 15000, 25000);
        mockCustomer.setCreateDate(java.time.LocalDate.of(2021, 6, 15));

        when(loanEligibilityService.getCustomerByName("JOHN", "DOE"))
                .thenReturn(Optional.of(mockCustomer));
        when(loanEligibilityService.checkLoanEligibility(mockCustomer))
                .thenReturn("Rejected: Customer's age is below 18. Customer's credit score is below 650.");

        // Act
        ResponseEntity<String> response = loanEligibilityController.checkLoanEligibility(mockCustomer);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Rejected: Customer's age is below 18. Customer's credit score is below 650.", response.getBody());

        verify(loanEligibilityService).getCustomerByName("JOHN", "DOE");
        verify(loanEligibilityService).updateCustomerCreditScore("JOHN", "DOE", 500);
        verify(loanEligibilityService).checkLoanEligibility(mockCustomer);
    }

    @Test
    void testCheckLoanEligibility_CustomerNotFound() {
        // Arrange
        Customer mockCustomer = createMockCustomer("ALICE", "SMITH", 35, 750, "Employed", 0, 0);

        when(loanEligibilityService.getCustomerByName("ALICE", "SMITH"))
                .thenReturn(Optional.empty());

        // Act
        ResponseEntity<String> response = loanEligibilityController.checkLoanEligibility(mockCustomer);

        // Assert
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Customer not found in the database.", response.getBody());

        verify(loanEligibilityService).getCustomerByName("ALICE", "SMITH");
        verify(loanEligibilityService, never()).updateCustomerCreditScore(anyString(), anyString(), anyDouble());
        verify(loanEligibilityService, never()).checkLoanEligibility(any(Customer.class));
    }

    @Test
    void testCheckLoanEligibility_ExceptionCase() {
        // Arrange
        Customer mockCustomer = createMockCustomer("CHARLIE", "BROWN", 40, 800, "Employed", 0, 20000);

        when(loanEligibilityService.getCustomerByName("CHARLIE", "BROWN"))
                .thenThrow(new RuntimeException("Database connection error"));

        // Act
        ResponseEntity<String> response = loanEligibilityController.checkLoanEligibility(mockCustomer);

        // Assert
        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Error occurred while checking eligibility.", response.getBody());

        verify(loanEligibilityService).getCustomerByName("CHARLIE", "BROWN");
        verify(loanEligibilityService, never()).updateCustomerCreditScore(anyString(), anyString(), anyDouble());
        verify(loanEligibilityService, never()).checkLoanEligibility(any(Customer.class));
    }

    // Utility method to create mock customer instances
    private Customer createMockCustomer(String firstName, String lastName, int age, int creditScore, String employmentStatus, double existingLoans, double totalDebt) {
        Customer customer = new Customer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setAge(age);
        customer.setCreditScore(creditScore);
        customer.setEmploymentStatus(employmentStatus);
        customer.setExistingLoans(existingLoans);
        customer.setTotalDebt(totalDebt);
        customer.setAnnualIncome(50000); // Default value
        customer.setCreateDate(java.time.LocalDate.of(2022, 1, 1)); // Default creation date
        return customer;
    }
}
