package com.example.loanmanagement.controller;

import com.example.loanmanagement.entity.Customer;
import com.example.loanmanagement.service.LoanEligibilityService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.loanmanagement.entity.LoanRequest;
import java.util.Optional;


@RestController
@RequestMapping("/api/loans")
@CrossOrigin(origins = "http://localhost:4200") // Allow CORS for this controller from frontend
public class LoanEligibilityController {

    private static final Logger logger = LogManager.getLogger(LoanEligibilityController.class);  // Logger initialization

    private final LoanEligibilityService loanEligibilityService;

    @Autowired
    public LoanEligibilityController(LoanEligibilityService loanEligibilityService) {
        this.loanEligibilityService = loanEligibilityService;
    }

    // Endpoint to check loan eligibility via POST request
    @PostMapping("/check-customer")
    public ResponseEntity<String> checkLoanEligibility(@RequestBody Customer customerRequest) {
        try {
            // Log the received customer data for debugging
            logger.info("Received customer request: " + customerRequest);

            // Fetch customer from the database using firstName and lastName
            Optional<Customer> existingCustomer = loanEligibilityService.getCustomerByName(
                    customerRequest.getFirstName(),
                    customerRequest.getLastName()
            );

            if (existingCustomer.isPresent()) {
                logger.info("Customer found: " + existingCustomer.get());  // Log customer details

                // Update credit score
                loanEligibilityService.updateCustomerCreditScore(
                        customerRequest.getFirstName(),
                        customerRequest.getLastName(),
                        customerRequest.getCreditScore()
                );

                // Use the updated details to perform the eligibility check
                String eligibilityStatus = loanEligibilityService.checkLoanEligibility(customerRequest);

                // Return the eligibility status
                if (eligibilityStatus.startsWith("Approved")) {
                    return ResponseEntity.ok(eligibilityStatus);
                } else {
                    return ResponseEntity.status(400).body(eligibilityStatus);
                }
            } else {
                // Customer not found in the database
                logger.warn("Customer not found in the database for: " + customerRequest.getFirstName() + " " + customerRequest.getLastName());
                return ResponseEntity.status(404).body("Customer not found in the database.");
            }
        } catch (Exception e) {
            // Log the error
            logger.error("Error occurred while checking eligibility: ", e);
            return ResponseEntity.status(500).body("Error occurred while checking eligibility.");
        }
    }

    @PostMapping("/calculate-max-loan")
    public ResponseEntity<String> calculateMaxLoanAmount(@RequestBody LoanRequest loanRequest) {
        try {
            // Log the received data for debugging
            logger.info("Received loan calculation request: Total Debt: "
                    + loanRequest.getTotalDebt() + ", Annual Income: "
                    + loanRequest.getAnnualIncome() + ", Credit Score: "
                    + loanRequest.getCreditScore());

            // Call service method to calculate the loan amount
            double maxLoanAmount = loanEligibilityService.calculateMaxLoanAmount(
                    loanRequest.getTotalDebt(),
                    loanRequest.getAnnualIncome(),
                    loanRequest.getCreditScore()
            );

            // Return the calculated loan amount as a response
            return ResponseEntity.ok("Maximum loan amount you qualify for: " + maxLoanAmount);
        } catch (Exception e) {
            // Log and handle any unexpected errors
            logger.error("Error occurred while calculating maximum loan amount: ", e);
            return ResponseEntity.status(500).body("Error occurred while processing the loan calculation.");
        }
    }



    // Helper method to calculate the Income to Debt Ratio (IDR)
    private double calculateIDR(double totalDebt, double annualIncome) {
        if (annualIncome == 0) {
            throw new IllegalArgumentException("Annual income cannot be zero.");
        }
        return totalDebt / annualIncome;
    }

}
