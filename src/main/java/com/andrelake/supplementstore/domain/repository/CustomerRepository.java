package com.andrelake.supplementstore.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.andrelake.supplementstore.domain.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>{

}
