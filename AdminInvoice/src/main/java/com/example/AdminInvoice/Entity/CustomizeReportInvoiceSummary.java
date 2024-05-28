package com.example.AdminInvoice.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomizeReportInvoiceSummary {
    private String dateRange;
    private RepostsByCustomizeSummary repostsByCustomizeSummary;
    private String customerName;
    private String invoiceStatus;


}
