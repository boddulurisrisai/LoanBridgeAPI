package com.example.loanmanagement.controller;

import com.example.loanmanagement.entity.Customer;
import com.example.loanmanagement.entity.LoanRequest;
import com.example.loanmanagement.service.LoanEligibilityService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@RestController
@RequestMapping("/api/loans")
@CrossOrigin(origins = "http://localhost:4200") // Replace with API Gateway's URL

public class LoanEligibilityController {

    private static final Logger logger = LogManager.getLogger(LoanEligibilityController.class);

    private final LoanEligibilityService loanEligibilityService;

    @Autowired
    public LoanEligibilityController(LoanEligibilityService loanEligibilityService) {
        this.loanEligibilityService = loanEligibilityService;
    }

    // Endpoint to check loan eligibility via POST request
    @PostMapping("/check-customer")
    public ResponseEntity<String> checkLoanEligibility(@RequestBody Customer customerRequest) {
        logger.info("Received customer eligibility check request.");

        try {
            logger.info("Received customer data for eligibility check: {} {}", customerRequest.getFirstName(), customerRequest.getLastName());

            Optional<Customer> existingCustomer = loanEligibilityService.getCustomerByName(
                    customerRequest.getFirstName(),
                    customerRequest.getLastName()
            );

            if (existingCustomer.isPresent()) {
                logger.info("Customer found in database: {}", existingCustomer.get());

                loanEligibilityService.updateCustomerCreditScore(
                        customerRequest.getFirstName(),
                        customerRequest.getLastName(),
                        customerRequest.getCreditScore()
                );

                String eligibilityStatus = loanEligibilityService.checkLoanEligibility(customerRequest);

                if (eligibilityStatus.startsWith("Approved")) {
                    return ResponseEntity.ok(eligibilityStatus);
                } else {
                    return ResponseEntity.status(400).body(eligibilityStatus);
                }
            } else {
                logger.warn("Customer not found in database: {} {}", customerRequest.getFirstName(), customerRequest.getLastName());
                return ResponseEntity.status(404).body("Customer not found.");
            }
        } catch (Exception e) {
            logger.error("Error occurred while checking eligibility: ", e);
            return ResponseEntity.status(500).body("Error occurred while checking eligibility.");
        }
    }

    // Endpoint to calculate max loan amount
    @PostMapping("/calculate-max-loan")
    public ResponseEntity<String> calculateMaxLoanAmount(@RequestBody LoanRequest loanRequest) {
        logger.info("Received max loan calculation request.");

        try {
            logger.info("Received loan request: Total Debt: {}, Annual Income: {}, Credit Score: {}",
                    loanRequest.getTotalDebt(), loanRequest.getAnnualIncome(), loanRequest.getCreditScore());

            double maxLoanAmount = loanEligibilityService.calculateMaxLoanAmount(
                    loanRequest.getTotalDebt(),
                    loanRequest.getAnnualIncome(),
                    loanRequest.getCreditScore()
            );

            return ResponseEntity.ok("Maximum loan amount: " + maxLoanAmount);
        } catch (Exception e) {
            logger.error("Error occurred while calculating max loan amount: ", e);
            return ResponseEntity.status(500).body("Error occurred while calculating loan amount.");
        }
    }
}
