package com.example.AdminInvoice.Entity;

import com.example.AdminInvoice.InvoiceSubclass.InvoiceStatus;
import com.example.AdminInvoice.InvoiceSubclass.ServiceAccountingCode;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "invoice_db")
public class InvoiceDto {
    @Id
    private String id;
    @NotBlank
    private String invoiceType;
    private String invoiceNumber;
    @NotBlank
    private String customerName;
    @NotBlank
    private String gstType;
    private Double gstPercentage;
    // datatype to used with String It Change Him  --- 17-04-2024  ----
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date invoiceDate;
    @NotBlank
    private String paymentTerms;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date startDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date dueDate;
    @NotNull
    private InvoiceStatus invoiceStatus;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date lastModified;
    private String gstInNumber;
    @DecimalMin(value = "0.0")
    private Double retainerFee;
    private String notes;
    private String termsAndConditions;
    @NotEmpty
    private List<ServiceAccountingCode> servicesList;
    private TaxAmount taxAmount;
    @DecimalMin(value = "0.0")
    private Double discountPercentage;
    @DecimalMin(value = "0.0")
    private Double totalAmount;

}



