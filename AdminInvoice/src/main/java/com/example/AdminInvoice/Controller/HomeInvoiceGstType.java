package com.example.AdminInvoice.Controller;

import com.example.AdminInvoice.Entity.InvoiceGstType;
import com.example.AdminInvoice.Login.TokenValidator;
import com.example.AdminInvoice.Payroll.AddedResponse;
import com.example.AdminInvoice.Payroll.DeleteResponse;
import com.example.AdminInvoice.Payroll.UpdateResponse;
import com.example.AdminInvoice.Service.InvoiceGstTypeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gstType")
@Validated
public class HomeInvoiceGstType {
    @Autowired private InvoiceGstTypeService invoiceGstTypeService;
    @Autowired private TokenValidator tokenValidator;

    //Create
    @PostMapping("/create")
    public ResponseEntity<AddedResponse<InvoiceGstType>> create(@RequestHeader("Authorization") String authorizationHeader, @Valid @RequestBody InvoiceGstType invoiceGstType) {
        try {
            if (tokenValidator.isValidToken(authorizationHeader)) {
                AddedResponse<InvoiceGstType> invoiceGstTypeAddedResponse = invoiceGstTypeService.addInvoiceGstType(invoiceGstType);
                return ResponseEntity.status(HttpStatus.CREATED).body(invoiceGstTypeAddedResponse);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new AddedResponse<>(invoiceGstType.getId(), "Token expired.",HttpStatus.FORBIDDEN));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AddedResponse<>("","Internal Server Error",HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
    //Get
    @PostMapping("/get/{gstId}")
    public ResponseEntity<?> getGst(@RequestHeader("Authorization") String accessToken,@PathVariable String gstId)throws Exception{
        if (tokenValidator.isValidToken(accessToken)) {
            InvoiceGstType invoiceGstType = invoiceGstTypeService.getInvoiceGstType(gstId);
            return new ResponseEntity<>(invoiceGstType, HttpStatus.OK);
        }else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token expired.");
        }
    }
    //Update
    @PostMapping("/update/{gstId}")
    public ResponseEntity<?> updateGst(@RequestHeader("Authorization") String accessToken,@PathVariable String gstId,@RequestBody InvoiceGstType invoiceGstType)throws Exception{
        if(tokenValidator.isValidToken(accessToken)){
            UpdateResponse invoiceGSTUpdate  = invoiceGstTypeService.updateInvoiceGstType(gstId, invoiceGstType);
            return new ResponseEntity<>(invoiceGSTUpdate,HttpStatus.OK);
        }else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token expired.");
        }
    }
    //Delete
    @PostMapping("/delete/{gstId}")
    public ResponseEntity<?> deleteGst(@RequestHeader("Authorization") String accessToken,@PathVariable String gstId)throws Exception{
        if (tokenValidator.isValidToken(accessToken)) {
            DeleteResponse deleteResponse= invoiceGstTypeService.deleteInvoiceGstType(gstId);
            return new ResponseEntity<>(deleteResponse, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token expired.");
        }
    }
    //getAll
    @PostMapping("/list")
    public ResponseEntity<List<InvoiceGstType>> getAllGstList(@RequestHeader("Authorization") String accessToken)throws Exception{
        if(tokenValidator.isValidToken(accessToken)){
            List<InvoiceGstType> allInvoices=invoiceGstTypeService.getAllInvoiceGstType();
            return new ResponseEntity<>(allInvoices,HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

}
