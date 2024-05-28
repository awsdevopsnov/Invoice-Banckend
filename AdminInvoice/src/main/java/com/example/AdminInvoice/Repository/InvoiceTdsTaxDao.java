package com.example.AdminInvoice.Repository;

import com.example.AdminInvoice.Entity.InvoiceTdsTax;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvoiceTdsTaxDao extends MongoRepository<InvoiceTdsTax,String> {
    Optional<InvoiceTdsTax> findByTaxName(String taxName);
}
