package com.example.AdminInvoice.Service;

import com.example.AdminInvoice.Entity.CompanySettings;
import com.example.AdminInvoice.Entity.SettingsPortals;
import com.example.AdminInvoice.Payroll.AddedResponse;
import com.example.AdminInvoice.Payroll.DeleteResponse;
import com.example.AdminInvoice.Payroll.UpdateResponse;
import com.example.AdminInvoice.Repository.SettingPortalsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SettingPortalsServiceImpl implements SettingPortalsService{
    @Autowired private SettingPortalsDao settingPortalsDao;

    @Override
    public AddedResponse<SettingsPortals> addLinks(SettingsPortals links) throws Exception{
        Optional<SettingsPortals> settingsPortals=settingPortalsDao.findByid(links.getId());
        if (settingsPortals.isPresent()) {
            return new AddedResponse<>(links.getId(),"Links already exists!", HttpStatus.CONFLICT);
        }
        SettingsPortals savedLinks = settingPortalsDao.save(links);
        return new AddedResponse<>(savedLinks.getId(), "Links added successfully", HttpStatus.CREATED);
    }

    @Override
    public List<SettingsPortals> getAllLinks() throws Exception {
        return settingPortalsDao.findAll();
    }


    @Override
    public UpdateResponse updateLinks(String id, SettingsPortals links) {
        try {
            Optional<SettingsPortals> optional = settingPortalsDao.findById(id);
            if (optional.isPresent()) {
                SettingsPortals existingLinks = optional.get();
                existingLinks.setLabel(links.getLabel());
                existingLinks.setUrl(links.getUrl());
                existingLinks.setDescription(links.getDescription());
                settingPortalsDao.save(existingLinks);
                return new UpdateResponse(existingLinks.getId(), "Links updated successfully", HttpStatus.OK);
            } else {
                return new UpdateResponse(id, "Links not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new UpdateResponse(id, "Error updating Company: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Override
    public DeleteResponse deleteLinks(String id)throws Exception {
        Optional<SettingsPortals> optional= settingPortalsDao.findById(id);
        if(optional.isPresent()){
            settingPortalsDao.deleteById(id);
            return new DeleteResponse(id,"Link deleted",HttpStatus.OK);
        }
        return new DeleteResponse(id,"Invalid ID",HttpStatus.UNAUTHORIZED);
    }
}
