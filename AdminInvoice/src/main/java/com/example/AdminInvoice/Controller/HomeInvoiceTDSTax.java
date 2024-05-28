package com.example.AdminInvoice.Controller;

import com.example.AdminInvoice.Entity.InvoiceTdsTax;
import com.example.AdminInvoice.Login.TokenValidator;
import com.example.AdminInvoice.Payroll.AddedResponse;
import com.example.AdminInvoice.Payroll.DeleteResponse;
import com.example.AdminInvoice.Payroll.UpdateResponse;
import com.example.AdminInvoice.Service.InvoiceTdsTaxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/tdsTax")
@Validated
public class HomeInvoiceTDSTax {
    @Autowired private InvoiceTdsTaxService invoiceTdsTaxService;
    @Autowired private TokenValidator tokenValidator;
    //Invoice TDS Tax
    //Creation
    @PostMapping("/create")
    public ResponseEntity<AddedResponse<InvoiceTdsTax>> createTDS(@RequestHeader("Authorization") String accessToken, @RequestBody InvoiceTdsTax invoiceTdsTax)throws Exception{
        try {
            if (tokenValidator.isValidToken(accessToken)) {
                AddedResponse<InvoiceTdsTax> createTsd=invoiceTdsTaxService.addInvoiceTdsTax(invoiceTdsTax);
                return ResponseEntity.ok(createTsd);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new AddedResponse<>(invoiceTdsTax.getId(), "Token expired.",HttpStatus.FORBIDDEN));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AddedResponse<>("", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
    //Get
    @PostMapping("/get/{taxId}")
    public ResponseEntity<InvoiceTdsTax> getTDS(@RequestHeader("Authorization") String accessToken,@PathVariable String taxId)throws Exception{
        if (tokenValidator.isValidToken(accessToken)) {
            InvoiceTdsTax tdsTaxAmount = invoiceTdsTaxService.getInvoiceTDSTax(taxId);
            return new ResponseEntity<>(tdsTaxAmount, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
    //Update
    @PostMapping("/update/{taxId}")
    public ResponseEntity<UpdateResponse> updateTDS(@RequestHeader("Authorization") String accessToken,@PathVariable String taxId,@RequestBody InvoiceTdsTax invoiceTdsTax)throws Exception{
        if(tokenValidator.isValidToken(accessToken)){
            UpdateResponse invoiceDTSUpdate  = invoiceTdsTaxService.updateInvoiceTDSTax(taxId, invoiceTdsTax);
            return new ResponseEntity<>(invoiceDTSUpdate,HttpStatus.OK);
        }else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new UpdateResponse(taxId,"Token expired.",HttpStatus.FORBIDDEN));
        }
    }
    //Delete
    @PostMapping("/delete/{taxId}")
    public ResponseEntity<DeleteResponse> deleteTDS(@RequestHeader("Authorization") String accessToken,@PathVariable String taxId)throws Exception{
        if (tokenValidator.isValidToken(accessToken)) {
            DeleteResponse deleteResponse= invoiceTdsTaxService.deleteInvoiceTDSTax(taxId);
            return new ResponseEntity<>(deleteResponse, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new DeleteResponse(taxId,"Token expired.",HttpStatus.FORBIDDEN));
        }
    }
    //getAll
    @PostMapping("/list")
    public ResponseEntity<List<InvoiceTdsTax>> getAllTDSList(@RequestHeader("Authorization") String accessToken)throws Exception{
        if(tokenValidator.isValidToken(accessToken)){
            List<InvoiceTdsTax> allInvoices=invoiceTdsTaxService.getAllInvoiceTDSTax();
            return new ResponseEntity<>(allInvoices,HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

}
