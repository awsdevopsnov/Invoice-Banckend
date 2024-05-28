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
@Document(collection = "paymentTerms_db")
public class InvoicePaymentTerms {
    @Id
    private String id;
    private String termName;
    private int totalDays;
}
