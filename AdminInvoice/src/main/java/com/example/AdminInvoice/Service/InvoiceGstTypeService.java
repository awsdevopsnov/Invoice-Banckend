package com.example.AdminInvoice.Service;

import com.example.AdminInvoice.Entity.InvoiceGstType;
import com.example.AdminInvoice.Payroll.AddedResponse;
import com.example.AdminInvoice.Payroll.DeleteResponse;
import com.example.AdminInvoice.Payroll.UpdateResponse;

import java.util.List;

public interface InvoiceGstTypeService {
    public AddedResponse<InvoiceGstType> addInvoiceGstType(InvoiceGstType invoiceGstType) throws Exception;
    public InvoiceGstType getInvoiceGstType(String gstId)throws Exception;
    public UpdateResponse updateInvoiceGstType(String gstId, InvoiceGstType invoiceGstType)throws Exception;
    public DeleteResponse deleteInvoiceGstType(String gstId)throws Exception;
    public List<InvoiceGstType> getAllInvoiceGstType()throws Exception;

}
