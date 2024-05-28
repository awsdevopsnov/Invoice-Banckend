package com.example.AdminInvoice.Service;

import com.example.AdminInvoice.Entity.CompanySettings;
import com.example.AdminInvoice.Payroll.AddedResponse;
import com.example.AdminInvoice.Payroll.DeleteResponse;
import com.example.AdminInvoice.Payroll.UpdateResponse;
import com.example.AdminInvoice.Repository.CompanySettingDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SettingServiceImpl implements SettingService{
    @Autowired private CompanySettingDao companySettingDao;

    @Override
    public AddedResponse<CompanySettings> addCompany(CompanySettings company) throws Exception{
        Optional<CompanySettings> existingCompany = companySettingDao.findByCompanyEmail(company.getCompanyEmail());
        if (existingCompany.isPresent()) {
            return new AddedResponse<>(company.getId(),"company Details already exists!",HttpStatus.CONFLICT);
        }
        CompanySettings savedCompany = companySettingDao.save(company);
        return new AddedResponse<>(savedCompany.getId(), "company Details added successfully", HttpStatus.CREATED);
    }

    @Override
    public CompanySettings findById(String id) throws Exception {
        Optional<CompanySettings> company = companySettingDao.findById(id);
        return company.get();
    }

    @Override
    public CompanySettings getAllCompany() throws Exception {
        return (CompanySettings) companySettingDao.findAll();
    }

    public UpdateResponse updateCompany(String id, CompanySettings company) {
        try {
            Optional<CompanySettings> optional = companySettingDao.findById(id);
            if (optional.isPresent()) {
                CompanySettings existingCompany = optional.get();
                existingCompany.setCompanyName(company.getCompanyName());
                existingCompany.setCompanyAddress(company.getCompanyAddress());
                existingCompany.setCompanyState(company.getCompanyState());
                existingCompany.setCompanyCountry(company.getCompanyCountry());
                existingCompany.setCompanyEmail(company.getCompanyEmail());
                existingCompany.setCompanyPhone(company.getCompanyPhone());
                existingCompany.setCompanyCell(company.getCompanyCell());
                existingCompany.setCompanyWebsite(company.getCompanyWebsite());
                existingCompany.setCompanyTaxNumber(company.getCompanyTaxNumber());
                existingCompany.setCompanyRegNumber(company.getCompanyRegNumber());
                companySettingDao.save(existingCompany);
                return new UpdateResponse(existingCompany.getId(), "Company Details updated successfully", HttpStatus.OK);
            } else {
                return new UpdateResponse(id, "Company Details not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new UpdateResponse(id, "Failed to update Company Details: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public DeleteResponse deleteCompany(String id)throws Exception {
        Optional<CompanySettings> optional= companySettingDao.findById(id);
        if(optional.isPresent()){
            companySettingDao.deleteById(id);
            return new DeleteResponse(id,"Company deleted",HttpStatus.OK);
        }
        return new DeleteResponse(id,"Invalid ID",HttpStatus.UNAUTHORIZED);
    }

}
