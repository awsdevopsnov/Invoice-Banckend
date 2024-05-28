package com.example.AdminInvoice.Controller;

import com.example.AdminInvoice.Entity.CompanySettings;
import com.example.AdminInvoice.Entity.SettingsPortals;
import com.example.AdminInvoice.Login.TokenValidator;
import com.example.AdminInvoice.Payroll.AddedResponse;
import com.example.AdminInvoice.Payroll.DeleteResponse;
import com.example.AdminInvoice.Payroll.UpdateResponse;
import com.example.AdminInvoice.Service.SettingPortalsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Portal")
@Validated
public class HomeSettingPortal{
    @Autowired private TokenValidator tokenValidator;
    @Autowired private SettingPortalsService settingPortalsService;

    @PostMapping("/createLinks")
    public ResponseEntity<AddedResponse<SettingsPortals>> create(@RequestHeader("Authorization") String authorizationHeader,@Valid @RequestBody SettingsPortals links) throws Exception{
        try {
            if (tokenValidator.isValidToken(authorizationHeader)) {
                AddedResponse<SettingsPortals> returnLinks = settingPortalsService.addLinks(links);
                return ResponseEntity.status(HttpStatus.CREATED).body(returnLinks);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new AddedResponse<>(links.getId(),"Token expired.",HttpStatus.FORBIDDEN));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AddedResponse<>(links.getId(),"Internal Server error.",HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
    @PostMapping("/list")
    public ResponseEntity<List<SettingsPortals>> getAllLinks(@RequestHeader("Authorization") String authorizationHeader)throws Exception {
        try {
            if (tokenValidator.isValidToken(authorizationHeader)) {
                List<SettingsPortals> links = settingPortalsService.getAllLinks();
                return ResponseEntity.status(HttpStatus.OK).body(links);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<UpdateResponse> updateLinks(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String id, @RequestBody SettingsPortals links) {
        try {
            if (tokenValidator.isValidToken(authorizationHeader)) {
                UpdateResponse updateResponse = settingPortalsService.updateLinks(id, links);
                return ResponseEntity.status(HttpStatus.OK).body(updateResponse);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new UpdateResponse(id,"Token expired.",HttpStatus.FORBIDDEN));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new UpdateResponse(id,"Internal Server Error",HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<DeleteResponse> deleteLink(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String id) {
        try {
            if (tokenValidator.isValidToken(authorizationHeader)) {
                DeleteResponse deleteResponse = settingPortalsService.deleteLinks(id);
                return ResponseEntity.status(HttpStatus.OK).body(deleteResponse);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new DeleteResponse(id,"Token expired.",HttpStatus.FORBIDDEN));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DeleteResponse(id,"Internal Server Error",HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

}
