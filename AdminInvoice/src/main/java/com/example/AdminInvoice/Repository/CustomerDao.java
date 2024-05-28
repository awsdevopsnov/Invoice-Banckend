package com.example.AdminInvoice.Repository;

import com.example.AdminInvoice.Entity.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CustomerDao extends MongoRepository<Customer,String> {
    Optional<Customer> findByid(String id);
    Optional<Customer> findByCustomerEmail(String customerEmail);

}
