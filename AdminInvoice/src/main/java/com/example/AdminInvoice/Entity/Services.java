package com.example.AdminInvoice.Entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "services_db")
public class Services {
    @Id
    private String id;
    private String serviceAccountingCode;
    private String serviceDescription;
    private Double serviceAmount;

}
