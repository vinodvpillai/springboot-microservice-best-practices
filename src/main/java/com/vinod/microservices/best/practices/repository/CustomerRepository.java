package com.vinod.microservices.best.practices.repository;

import com.vinod.microservices.best.practices.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer,Long> {

    Optional<Customer> findCustomerByEmailId(String emailId);
}
