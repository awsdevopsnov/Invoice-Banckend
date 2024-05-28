package com.example.AdminInvoice.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "invoiceTax_db")
public class InvoiceTdsTax {
    @Id
    private String id;
    private String taxName;
    private Double taxPercentage;
}
