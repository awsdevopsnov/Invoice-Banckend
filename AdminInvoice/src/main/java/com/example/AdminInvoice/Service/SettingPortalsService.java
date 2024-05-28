package com.example.AdminInvoice.Service;

import com.example.AdminInvoice.Entity.CompanySettings;
import com.example.AdminInvoice.Entity.SettingsPortals;
import com.example.AdminInvoice.Payroll.AddedResponse;
import com.example.AdminInvoice.Payroll.DeleteResponse;
import com.example.AdminInvoice.Payroll.UpdateResponse;

import java.util.List;
import java.util.Optional;

public interface SettingPortalsService{
    public AddedResponse<SettingsPortals> addLinks(SettingsPortals links) throws Exception;
    public UpdateResponse updateLinks(String id,SettingsPortals links) throws Exception;
    public List<SettingsPortals> getAllLinks() throws Exception;
    public DeleteResponse deleteLinks(String id) throws Exception;


}
