package com.example.AdminInvoice.Controller;

import com.example.AdminInvoice.Entity.CompanySettings;
import com.example.AdminInvoice.Login.TokenValidator;
import com.example.AdminInvoice.Payroll.AddedResponse;
import com.example.AdminInvoice.Payroll.DeleteResponse;
import com.example.AdminInvoice.Payroll.UpdateResponse;
import com.example.AdminInvoice.Service.SettingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/setting")
@Validated
public class HomeSetting {
    @Autowired private SettingService settingService;
    @Autowired private TokenValidator tokenValidator;

    @PostMapping("/create")
    public ResponseEntity<AddedResponse<CompanySettings>> createCompany(@RequestHeader("Authorization") String authorizationHeader, @Valid @RequestBody CompanySettings company) {
        try {
            if (tokenValidator.isValidToken(authorizationHeader)) {
                AddedResponse<CompanySettings> returnCompany = settingService.addCompany(company);
                return ResponseEntity.status(HttpStatus.CREATED).body(returnCompany);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new AddedResponse<>(company.getId(),"Token expired.",HttpStatus.FORBIDDEN));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AddedResponse<>(company.getId(),"Internal Server error.",HttpStatus.FORBIDDEN));
        }
    }

    @PostMapping("/list")
    public ResponseEntity<CompanySettings> getAllCompanys(@RequestHeader("Authorization") String authorizationHeader)throws Exception {
        try {
            if (tokenValidator.isValidToken(authorizationHeader)) {
                CompanySettings company = settingService.getAllCompany();
                return ResponseEntity.status(HttpStatus.OK).body(company);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<CompanySettings> getCompanyById(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String id) {
        try {
            if (tokenValidator.isValidToken(authorizationHeader)) {
                CompanySettings company = settingService.findById(id);
                return ResponseEntity.status(HttpStatus.OK).body(company);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<UpdateResponse> updateCompany(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String id, @RequestBody CompanySettings company) {
        try {
            if (tokenValidator.isValidToken(authorizationHeader)) {
                UpdateResponse updateResponse = settingService.updateCompany(id, company);
                return ResponseEntity.status(HttpStatus.OK).body(updateResponse);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new UpdateResponse(id,"Token expired.",HttpStatus.FORBIDDEN));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new UpdateResponse(id,"Internal Server Error",HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<DeleteResponse> deleteCompany(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String id) {
        try {
            if (tokenValidator.isValidToken(authorizationHeader)) {
                DeleteResponse deleteResponse = settingService.deleteCompany(id);
                return ResponseEntity.status(HttpStatus.OK).body(deleteResponse);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new DeleteResponse(id,"Token expired.",HttpStatus.FORBIDDEN));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DeleteResponse(id,"Internal Server Error",HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

}
