package com.example.AdminInvoice.InvoiceSubclass;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardReportOverdueDay {
    private Double totalAmount;
    private long noOfCustomers;
    private long noOfInvoices;

}
