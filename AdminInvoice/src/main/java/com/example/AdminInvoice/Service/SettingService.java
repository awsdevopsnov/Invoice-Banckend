package com.example.AdminInvoice.Service;

import ch.qos.logback.core.net.server.Client;
import com.example.AdminInvoice.Entity.CompanySettings;
import com.example.AdminInvoice.Entity.Customer;
import com.example.AdminInvoice.Entity.SettingsPortals;
import com.example.AdminInvoice.Payroll.AddedResponse;
import com.example.AdminInvoice.Payroll.DeleteResponse;
import com.example.AdminInvoice.Payroll.UpdateResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface SettingService {
    public AddedResponse<CompanySettings> addCompany(CompanySettings company) throws Exception;

    public CompanySettings getAllCompany() throws Exception;

    public CompanySettings findById(String id) throws Exception;

    public UpdateResponse updateCompany(String id, CompanySettings company) throws Exception;

    public DeleteResponse deleteCompany(String id) throws Exception;

}
