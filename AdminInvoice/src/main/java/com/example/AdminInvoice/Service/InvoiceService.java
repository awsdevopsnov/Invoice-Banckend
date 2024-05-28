package com.example.AdminInvoice.Service;

import com.example.AdminInvoice.Entity.*;
import com.example.AdminInvoice.Payroll.AddedResponse;
import com.example.AdminInvoice.Payroll.DeleteResponse;
import com.example.AdminInvoice.Payroll.UpdateResponse;

import java.util.List;

public interface InvoiceService {
    public AddedResponse<InvoiceDto> createInvoice(InvoiceDto invoice) throws Exception;
    public List<InvoiceDto> getAllInvoices() throws Exception;
    public UpdateResponse updateInvoice(String id,InvoiceDto invoiceDto) throws Exception;
    public DeleteResponse deleteInvoice(String id) throws Exception;
    public InvoiceDto getInvoice(String id)throws Exception;

    public List<ARsummaryDueDateFilter> reportARSummary(ReportARSummary reportARSummary)throws Exception;
    public List<InvoiceDto> reportInvoiceSummary(ReportInvoiceSummary reportInvoiceSummary)throws Exception;

    public List<InvoiceDto> customizeARSummary(CustomizeReportARSummary customizeReportARSummary)throws Exception;
    public List<InvoiceDto> CustomizeInvoiceSummary(CustomizeReportInvoiceSummary customizeReportInvoiceSummary)throws Exception;
}
