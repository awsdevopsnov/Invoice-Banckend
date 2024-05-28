package com.example.AdminInvoice.Repository;

import com.example.AdminInvoice.Entity.InvoicePaymentTerms;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvoicePaymentTermsDao extends MongoRepository<InvoicePaymentTerms,String> {
    Optional<InvoicePaymentTerms> findByTermName(String termName);

}
