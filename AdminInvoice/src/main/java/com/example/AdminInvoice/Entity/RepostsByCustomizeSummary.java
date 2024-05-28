package com.example.AdminInvoice.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepostsByCustomizeSummary {
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date invoiceDueDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date invoiceDate;
}
