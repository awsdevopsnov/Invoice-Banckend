package com.example.AdminInvoice.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ARsummaryDueDateFilter {
    private String id;
    private String customerName;
    private double days0to30;
    private double days30to45;
    private double above45;
    private double total;
}
