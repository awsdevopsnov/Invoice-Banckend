package com.example.AdminInvoice.Service;

import com.example.AdminInvoice.Entity.InvoicePaymentTerms;
import com.example.AdminInvoice.Payroll.AddedResponse;
import com.example.AdminInvoice.Payroll.DeleteResponse;
import com.example.AdminInvoice.Payroll.UpdateResponse;

import java.util.List;

public interface InvoicePaymentTermsService {
    public AddedResponse<InvoicePaymentTerms> invoiceAddedPaymentTerms(InvoicePaymentTerms invoicePaymentTerms)throws Exception;
    public InvoicePaymentTerms getInvoicePaymentTerms(String termsId)throws Exception;
    public UpdateResponse updateInvoicePaymentTerms(String termsId, InvoicePaymentTerms invoicePaymentTerms)throws Exception;
    public DeleteResponse deleteInvoicePaymentTerms(String termsId)throws Exception;
    public List<InvoicePaymentTerms> getAllInvoicePaymentTerms()throws Exception;

}
