package com.example.loanmanagement.service;

import com.example.loanmanagement.entity.Customer;
import com.example.loanmanagement.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.time.LocalDate;  // Import LocalDate for date handling
import java.time.temporal.ChronoUnit;  // Import ChronoUnit for calculating date differences

import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoanEligibilityService {
    private static final Logger logger = LogManager.getLogger(LoanEligibilityService.class);  // Logger initialization

    @Autowired
    private CustomerRepository customerRepository;
    // Method to fetch customer by name (this should fetch from your database)
    public Optional<Customer> getCustomerByName(String firstName, String lastName) {
        logger.info("Searching for customer with first name: " + firstName + " and last name: " + lastName);
        // Retrieve customer from the database
        Optional<Customer> customer = customerRepository.findByFirstNameAndLastName(firstName, lastName);
        if (customer.isPresent()) {
            logger.info("Customer found: " + customer.get());
        } else {
            logger.warn("No customer found with the given name.");
        }
        return customer;
    }

    @Transactional // Ensure a transaction for custom query
    public void updateCustomerCreditScore(String firstName, String lastName, double creditScore) {
        try {
            customerRepository.updateCreditScoreByName(firstName, lastName, (int) creditScore);
            logger.info("Updated credit score for customer: {} {}", firstName, lastName);
        } catch (Exception e) {
            logger.error("Error updating credit score: {}", e.getMessage());
            throw e; // Rethrow the exception to ensure rollback
        }
    }



    // Method to check loan eligibility
    public String checkLoanEligibility(Customer customer) {
        // List to store failure reasons
        List<String> failureReasons = new ArrayList<>();

        // Check if customer is above 18 years old
        if (customer.getAge() <= 18) {
            failureReasons.add("Customer's age is below 18.");
        }

        // Check if the credit score is below 650
        if (customer.getCreditScore() < 650) {
            failureReasons.add("Customer's credit score is below 650.");
        }

        // Check if the total outstanding loan exceeds $10,000
        if (customer.getExistingLoans() > 10000) {
            failureReasons.add("Customer has existing loans exceeding $10,000.");
        }

        // Calculate the Income-to-Debt Ratio (IDR)
        double idr = customer.getTotalDebt() / customer.getAnnualIncome();

        // Check if IDR is below 40%
        if (idr >= 0.40) {
            failureReasons.add("Customer's Income-to-Debt Ratio exceeds 40%.");
        }

        // Check if account age is less than 1 year
        long accountAgeInMonths = ChronoUnit.MONTHS.between(customer.getCreateDate(), LocalDate.now());
        if (accountAgeInMonths < 12) {
            failureReasons.add("Customer's account age is less than 1 year.");
        }

        // Check Employment Status: Must not be Unemployed
        if (customer.getEmploymentStatus().equalsIgnoreCase("Unemployed")) {
            failureReasons.add("Customer is unemployed.");
        }

        // If there are any failure reasons, return them as a joined string
        if (!failureReasons.isEmpty()) {
            return "Rejected: " + String.join(" ", failureReasons);
        }

        // If all conditions pass, the customer is eligible
        return "Approved: Customer is eligible for the loan.";
    }

    public double calculateMaxLoanAmount(double totalDebt, double annualIncome, double creditScore) {
        // Prevent division by zero
        if (annualIncome <= 0) {
            throw new IllegalArgumentException("Annual income must be greater than 0.");
        }

        // Calculate the Income-to-Debt Ratio (IDR)
        double idr = totalDebt / annualIncome;

        // Base loan amount is assumed as twice the annual income
        double baseLoanAmount = 2 * annualIncome;

        // Calculate credit score factor
        double creditScoreFactor;
        if (creditScore >= 750) {
            creditScoreFactor = 1.2; // Excellent credit score
        } else if (creditScore >= 650) {
            creditScoreFactor = 1.1; // Good credit score
        } else {
            creditScoreFactor = 1.0; // Fair or poor credit score
        }

        // Final loan amount based on IDR and credit score factor
        double loanAmount = baseLoanAmount * (1 - idr) * creditScoreFactor;

        // Ensure loan amount is not negative
        return Math.max(loanAmount, -(loanAmount));
    }






}