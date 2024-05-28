package com.example.AdminInvoice.Repository;

import com.example.AdminInvoice.Entity.Services;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceDao extends MongoRepository<Services,String> {
    Optional<Services> findByserviceAccountingCode(String serviceAccountingCode);

}
