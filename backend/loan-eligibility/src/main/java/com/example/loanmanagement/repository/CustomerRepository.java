package com.example.loanmanagement.repository;

import com.example.loanmanagement.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.loanmanagement.entity.Customer;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    // Custom query to find customer by first name and last name
    Optional<Customer> findByFirstNameAndLastName(String firstName, String lastName);

    // Update credit score by first name and last name
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("UPDATE Customer c SET c.creditScore = :creditScore WHERE c.firstName = :firstName AND c.lastName = :lastName")
    void updateCreditScoreByName(@Param("firstName") String firstName,
                                 @Param("lastName") String lastName,
                                 @Param("creditScore") int creditScore);

}
