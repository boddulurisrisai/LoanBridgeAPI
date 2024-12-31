package com.example.loanmanagement.service;

import com.example.loanmanagement.entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class LoanEligibilityServiceTest {

    private LoanEligibilityService loanEligibilityService;

    @BeforeEach
    void setUp() {
        // Initialize the service before each test
        loanEligibilityService = new LoanEligibilityService();
    }

    @Test
    void testCheckLoanEligibility_PositiveCase() {
        // Arrange: Set up a customer who meets all eligibility criteria
        Customer eligibleCustomer = createMockCustomer(
                "CHARLIE", "BROWN", "barbara@gmail.com",
                86, 500000, 900, "Employed", 3000, 8000, LocalDate.of(1988, 6, 15)
        );

        // Act: Call the method under test
        String result = loanEligibilityService.checkLoanEligibility(eligibleCustomer);

        // Assert: Validate the output
        assertEquals("Approved: Customer is eligible for the loan.", result);
    }

    @Test
    void testCheckLoanEligibility_NegativeCase() {
        // Arrange: Set up a customer who fails eligibility checks
        Customer ineligibleCustomer = createMockCustomer(
                "BARBARA", "JONES", "john.doe@example.com",
                12, 500000, 400, "Unemployed", 3000, 8000, LocalDate.of(2024, 6, 15)
        );

        // Act: Call the method under test
        String result = loanEligibilityService.checkLoanEligibility(ineligibleCustomer);

        // Assert: Validate the output message for rejections
        assertEquals(
                "Rejected: Customer's age is below 18. " +
                        "Customer's credit score is below 650. " +
                        "Customer's account age is less than 1 year. " +
                        "Customer is unemployed.",
                result
        );
    }

    // Utility method to create mock customer objects
    private Customer createMockCustomer(String firstName, String lastName, String email,
                                        int age, double annualIncome, int creditScore,
                                        String employmentStatus, double existingLoans,
                                        double totalDebt, LocalDate createDate) {
        Customer customer = new Customer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setEmail(email);
        customer.setAge(age);
        customer.setAnnualIncome(annualIncome);
        customer.setCreditScore(creditScore);
        customer.setEmploymentStatus(employmentStatus);
        customer.setExistingLoans(existingLoans);
        customer.setTotalDebt(totalDebt);
        customer.setCreateDate(createDate);
        return customer;
    }
}
