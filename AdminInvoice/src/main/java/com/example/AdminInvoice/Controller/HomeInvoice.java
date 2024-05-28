package com.example.AdminInvoice.Controller;

import com.example.AdminInvoice.Entity.*;
import com.example.AdminInvoice.Login.TokenValidator;
import com.example.AdminInvoice.Payroll.AddedResponse;
import com.example.AdminInvoice.Payroll.DeleteResponse;
import com.example.AdminInvoice.Payroll.UpdateResponse;
import com.example.AdminInvoice.Service.InvoiceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/invoice")
@Validated
public class HomeInvoice {

    @Autowired private InvoiceService invoiceService;
    @Autowired private TokenValidator tokenValidator;

    @PostMapping("/create")
    public ResponseEntity<AddedResponse<InvoiceDto>> createInvoice(@RequestHeader("Authorization") String accessToken, @Valid @RequestBody InvoiceDto invoiceDto)throws Exception {
        try {
            if (tokenValidator.isValidToken(accessToken)) {
                AddedResponse<InvoiceDto> createdInvoice;
                switch (invoiceDto.getInvoiceType().toLowerCase()) {
                    case "retainer":
                        createdInvoice = createRetainerInvoice(invoiceDto);
                        break;
                    case "onetime":
                        createdInvoice = createSubscriptionInvoice(invoiceDto);
                        break;
                    default:
                        return ResponseEntity.badRequest().body(new AddedResponse<>("", "Invalid invoice type", HttpStatus.BAD_REQUEST));
                }
                return ResponseEntity.ok(createdInvoice);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new AddedResponse<>(invoiceDto.getId(), "Token expired.",HttpStatus.FORBIDDEN));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AddedResponse<>("", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
    private AddedResponse<InvoiceDto> createRetainerInvoice(InvoiceDto invoice) throws Exception {
        return invoiceService.createInvoice(invoice);
    }
    private AddedResponse<InvoiceDto> createSubscriptionInvoice(InvoiceDto invoice) throws Exception {
        return invoiceService.createInvoice(invoice);
    }


    @PostMapping("/list")
    public ResponseEntity<List<InvoiceDto>> getAllInvoices(@RequestHeader("Authorization") String accessToken)throws Exception {
        if(tokenValidator.isValidToken(accessToken)){
            List<InvoiceDto> allInvoices=invoiceService.getAllInvoices();
            return new ResponseEntity<>(allInvoices,HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<UpdateResponse> updateInvoice(@RequestHeader("Authorization") String accessToken,@PathVariable String id,@RequestBody InvoiceDto invoiceDto) throws Exception {
        if(tokenValidator.isValidToken(accessToken)){
            UpdateResponse updateResponse = invoiceService.updateInvoice(id, invoiceDto);
            return new ResponseEntity<>(updateResponse,HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<DeleteResponse> response(@RequestHeader("Authorization") String accessToken, @PathVariable String id)throws Exception{
        if (tokenValidator.isValidToken(accessToken)) {
            DeleteResponse deleteResponse= invoiceService.deleteInvoice(id);
            return new ResponseEntity<>(deleteResponse, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/get/{id}")
    public ResponseEntity<InvoiceDto> getId(@RequestHeader("Authorization") String accessToken, @PathVariable String id)throws Exception{
        if (tokenValidator.isValidToken(accessToken)) {
            InvoiceDto invoice = invoiceService.getInvoice(id);
            return new ResponseEntity<>(invoice, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    //AR Summary Report
    @PostMapping("/arReport")
    public ResponseEntity<List<ARsummaryDueDateFilter>> generateARInvoiceReport(@RequestHeader("Authorization") String accessToken, @RequestBody ReportARSummary reportARSummary) throws Exception {
        if (tokenValidator.isValidToken(accessToken)) {
            List<ARsummaryDueDateFilter> report = invoiceService.reportARSummary(reportARSummary);
            return ResponseEntity.ok(report);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    //Invoice Summary Report
    @PostMapping("/invoiceReport")
    public ResponseEntity<List<InvoiceDto>> generateInvoiceReport(@RequestHeader("Authorization") String accessToken, @RequestBody ReportInvoiceSummary reportInvoiceSummary) throws Exception {
        if (tokenValidator.isValidToken(accessToken)) {
            List<InvoiceDto> report = invoiceService.reportInvoiceSummary(reportInvoiceSummary);
            return ResponseEntity.ok(report);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
    @PostMapping("/CustomizeReport")
    public List<InvoiceDto> CustomizeARSummary(@RequestBody CustomizeReportARSummary customizeReportARSummary)throws Exception {
        return invoiceService.customizeARSummary(customizeReportARSummary);
    }
    @PostMapping("/CustomizeReportInvoiceSummary")
    public List<InvoiceDto> CustomizeInvoiceSummary(@RequestBody CustomizeReportInvoiceSummary customizeReportInvoiceSummary)throws Exception {
        return invoiceService.CustomizeInvoiceSummary(customizeReportInvoiceSummary);
    }
}
