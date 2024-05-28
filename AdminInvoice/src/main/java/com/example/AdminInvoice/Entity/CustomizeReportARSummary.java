package com.example.AdminInvoice.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomizeReportARSummary {
    @JsonFormat(pattern = "dd-MM-yyyy")
    private String invoiceDate;
    private AgingByARSummary agingBy;
    private String CustomerName;
    private String agingIntervals;



}
