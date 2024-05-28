package com.example.AdminInvoice.Controller;

import com.example.AdminInvoice.Entity.Services;
import com.example.AdminInvoice.Login.TokenValidator;
import com.example.AdminInvoice.Payroll.AddedResponse;
import com.example.AdminInvoice.Payroll.DeleteResponse;
import com.example.AdminInvoice.Payroll.UpdateResponse;
import com.example.AdminInvoice.Service.ItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/service")
@Validated
public class HomeServices {
    @Autowired private ItemService item;
    @Autowired private TokenValidator tokenValidator;
    //Create
    @PostMapping("/create")
    public ResponseEntity<AddedResponse<Services>> createService(@RequestHeader("Authorization") String accessToken, @Valid @RequestBody Services services) {
        try {
            if (tokenValidator.isValidToken(accessToken)) {
                AddedResponse<Services> responseService = item.addService(services);
                return ResponseEntity.status(HttpStatus.CREATED).body(responseService);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new AddedResponse<>(null, "Token expired.", HttpStatus.FORBIDDEN));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AddedResponse<>(null, e.getMessage(), HttpStatus.BAD_REQUEST));
        }
    }
    //Get
    @PostMapping("/get/{IdOrAccounting}")
    public ResponseEntity<Services> getServiceByIdOrCode(@RequestHeader("Authorization") String accessToken, @PathVariable String IdOrAccounting) {
        try {
            if (tokenValidator.isValidToken(accessToken)) {
                Services services = item.getServicesByIdOrCode(IdOrAccounting);
                return ResponseEntity.ok(services);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    //List
    @PostMapping("/list")
    public ResponseEntity<List<Services>> getAllServices(@RequestHeader("Authorization") String accessToken)throws Exception {
        try {
            if (tokenValidator.isValidToken(accessToken)) {
                List<Services> listServices = item.getallServices();
                return ResponseEntity.ok(listServices);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    //Update
    @PostMapping("/update/{id}")
    public ResponseEntity<UpdateResponse> updateService(@RequestHeader("Authorization") String accessToken, @PathVariable String id, @RequestBody Services services) {
        try {
            if (tokenValidator.isValidToken(accessToken)) {
                UpdateResponse updateResponse = item.updateService(id, services);
                return ResponseEntity.ok(updateResponse);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new UpdateResponse(id,"Token expired.",HttpStatus.FORBIDDEN));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    //Delete
    @PostMapping("/delete/{id}")
    public ResponseEntity<DeleteResponse> deleteService(@RequestHeader("Authorization") String accessToken, @PathVariable String id) {
        try {
            if (tokenValidator.isValidToken(accessToken)) {
                DeleteResponse deleteResponse = item.delteService(id);
                return ResponseEntity.ok(deleteResponse);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new DeleteResponse(id,"Token expired.",HttpStatus.FORBIDDEN));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

