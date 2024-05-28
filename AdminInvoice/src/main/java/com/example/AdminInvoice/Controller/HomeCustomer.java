package com.example.AdminInvoice.Controller;

import com.example.AdminInvoice.Entity.Customer;
import com.example.AdminInvoice.Login.TokenValidator;
import com.example.AdminInvoice.Payroll.AddedResponse;
import com.example.AdminInvoice.Payroll.DeleteResponse;
import com.example.AdminInvoice.Payroll.UpdateResponse;
import com.example.AdminInvoice.Service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/customer")
@Validated
public class HomeCustomer {
    @Autowired private CustomerService customerService;
    @Autowired private TokenValidator tokenValidator;
    //Create
    @PostMapping("/create")
    public ResponseEntity<AddedResponse<Customer>> create(@RequestHeader("Authorization") String authorizationHeader, @Valid @RequestBody Customer client) {
        try {
            if (tokenValidator.isValidToken(authorizationHeader)) {
                AddedResponse<Customer> returnClient = customerService.addClient(client);
                return ResponseEntity.status(HttpStatus.CREATED).body(returnClient);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new AddedResponse<>(client.getId(),"Token expired.",HttpStatus.FORBIDDEN));
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //List
    @PostMapping("/list")
    public ResponseEntity<List<Customer>> getAllCustomers(@RequestHeader("Authorization") String authorizationHeader)throws Exception {
        try {
            if (tokenValidator.isValidToken(authorizationHeader)) {
                List<Customer> clients = customerService.getallClient();
                return ResponseEntity.status(HttpStatus.OK).body(clients);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //Get
    @PostMapping("/get/{id}")
    public ResponseEntity<Customer> getCustomerById(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String id) {
        try {
            if (tokenValidator.isValidToken(authorizationHeader)) {
                Customer customer = customerService.findbyId(id);
                return ResponseEntity.status(HttpStatus.OK).body(customer);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //Update
    @PostMapping("/update/{id}")
    public ResponseEntity<UpdateResponse> updateCustomer(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String id, @RequestBody Customer client) {
        try {
            if (tokenValidator.isValidToken(authorizationHeader)) {
                UpdateResponse updateResponse = customerService.updateClient(id, client);
                return ResponseEntity.status(HttpStatus.OK).body(updateResponse);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new UpdateResponse(id,"Token expired.",HttpStatus.FORBIDDEN));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new UpdateResponse(id,"Internal Server Error",HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
    //Delete
    @PostMapping("/delete/{id}")
    public ResponseEntity<DeleteResponse> deleteCustomer(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String id) {
        try {
            if (tokenValidator.isValidToken(authorizationHeader)) {
                DeleteResponse deleteResponse = customerService.deleteClient(id);
                return ResponseEntity.status(HttpStatus.OK).body(deleteResponse);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new DeleteResponse(id,"Token expired.",HttpStatus.FORBIDDEN));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DeleteResponse(id,"Internal Server Error",HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
}



