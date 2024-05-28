package com.example.AdminInvoice.Repository;

import com.example.AdminInvoice.Entity.CompanySettings;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanySettingDao extends MongoRepository<CompanySettings,String> {
    Optional<CompanySettings> findByCompanyEmail(String companyEmail);

}
