package com.example.AdminInvoice.Service;

import com.example.AdminInvoice.Entity.InvoiceTdsTax;
import com.example.AdminInvoice.Payroll.AddedResponse;
import com.example.AdminInvoice.Payroll.DeleteResponse;
import com.example.AdminInvoice.Payroll.UpdateResponse;

import java.util.List;

public interface InvoiceTdsTaxService {
    public AddedResponse<InvoiceTdsTax> addInvoiceTdsTax(InvoiceTdsTax invoiceTdsTax)throws Exception;
    public InvoiceTdsTax getInvoiceTDSTax(String taxId)throws Exception;
    public UpdateResponse updateInvoiceTDSTax(String taxId,InvoiceTdsTax invoiceTdsTax)throws Exception;
    public DeleteResponse deleteInvoiceTDSTax(String taxId)throws Exception;
    public List<InvoiceTdsTax> getAllInvoiceTDSTax()throws Exception;
}
