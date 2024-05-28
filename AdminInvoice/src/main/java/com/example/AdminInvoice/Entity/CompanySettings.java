package com.example.AdminInvoice.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanySettings {
    @Id
    private String id;
    private String companyName;
    private String companyAddress;
    private String companyState;
    private String companyCountry;
    private String companyEmail;
    private Long companyPhone;
    private Long companyCell;
    private String companyWebsite;
    private String companyTaxNumber;
    private String companyRegNumber;
}
