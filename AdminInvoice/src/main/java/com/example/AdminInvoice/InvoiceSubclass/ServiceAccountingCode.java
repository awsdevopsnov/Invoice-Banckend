package com.example.AdminInvoice.InvoiceSubclass;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceAccountingCode {
    private String id;
    private String serviceAccountingCode;
    private String serviceDescription;
    private Double serviceQty;
    private Double serviceAmount;
    private Double serviceTotalAmount;
}
