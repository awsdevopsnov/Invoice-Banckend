package com.example.AdminInvoice.Controller;

import com.example.AdminInvoice.Entity.InvoicePaymentTerms;
import com.example.AdminInvoice.Login.TokenValidator;
import com.example.AdminInvoice.Payroll.AddedResponse;
import com.example.AdminInvoice.Payroll.DeleteResponse;
import com.example.AdminInvoice.Payroll.UpdateResponse;
import com.example.AdminInvoice.Service.InvoicePaymentTermsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/paymentTerms")
@Validated
public class HomeInvoicePaymentTerms {

    @Autowired
    private InvoicePaymentTermsService invoicePaymentTermsService;
    @Autowired private TokenValidator tokenValidator;

    //Create
    @PostMapping("/create")
    public ResponseEntity<AddedResponse<InvoicePaymentTerms>> create(@RequestHeader("Authorization") String authorizationHeader, @Valid @RequestBody InvoicePaymentTerms invoicePaymentTerms) {
        try {
            if (tokenValidator.isValidToken(authorizationHeader)) {
                AddedResponse<InvoicePaymentTerms> invoicePaymentTermsAddedResponse = invoicePaymentTermsService.invoiceAddedPaymentTerms(invoicePaymentTerms);
                return ResponseEntity.status(HttpStatus.CREATED).body(invoicePaymentTermsAddedResponse);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new AddedResponse<>(invoicePaymentTerms.getId(), "Token expired.",HttpStatus.FORBIDDEN));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AddedResponse<>(invoicePaymentTerms.getId(),"Internal Server Error",HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
    //Get
    @PostMapping("/get/{termsId}")
    public ResponseEntity<InvoicePaymentTerms> getGst(@RequestHeader("Authorization") String accessToken,@PathVariable String termsId)throws Exception{
        if (tokenValidator.isValidToken(accessToken)) {
            InvoicePaymentTerms paymentTerms = invoicePaymentTermsService.getInvoicePaymentTerms(termsId);
            return new ResponseEntity<>(paymentTerms, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
    //Update
    @PostMapping("/update/{termsId}")
    public ResponseEntity<UpdateResponse> updateGst(@RequestHeader("Authorization") String accessToken,@PathVariable String termsId,@RequestBody InvoicePaymentTerms invoicePaymentTerms)throws Exception{
        if(tokenValidator.isValidToken(accessToken)){
            UpdateResponse invoicePaymentTermsUpdate  = invoicePaymentTermsService.updateInvoicePaymentTerms(termsId, invoicePaymentTerms);
            return new ResponseEntity<>(invoicePaymentTermsUpdate,HttpStatus.OK);
        }else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new UpdateResponse(termsId,"Token expired.",HttpStatus.FORBIDDEN));
        }
    }
    //Delete
    @PostMapping("/delete/{termsId}")
    public ResponseEntity<DeleteResponse> deleteGst(@RequestHeader("Authorization") String accessToken,@PathVariable String termsId)throws Exception{
        if (tokenValidator.isValidToken(accessToken)) {
            DeleteResponse deleteResponse= invoicePaymentTermsService.deleteInvoicePaymentTerms(termsId);
            return new ResponseEntity<>(deleteResponse, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new DeleteResponse(termsId,"Token expired.",HttpStatus.FORBIDDEN));
        }
    }
    //getAll
    @PostMapping("/list")
    public ResponseEntity<List<InvoicePaymentTerms>> getAllGstList(@RequestHeader("Authorization") String accessToken)throws Exception{
        if(tokenValidator.isValidToken(accessToken)){
            List<InvoicePaymentTerms> allInvoicePaymentTerms=invoicePaymentTermsService.getAllInvoicePaymentTerms();
            return new ResponseEntity<>(allInvoicePaymentTerms,HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
