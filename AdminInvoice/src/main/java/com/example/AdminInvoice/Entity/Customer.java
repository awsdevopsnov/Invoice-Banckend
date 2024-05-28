package com.example.AdminInvoice.Entity;


import com.example.AdminInvoice.CustomerSubclass.ContactPerson;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "customer_db")
public class Customer {
    @Id
    private String id;
    @NotBlank(message = "Customer email is required")
    @Email(message = "Invalid email format")
    private String customerEmail;
    @NotBlank(message = "Customer type is required")
    private String customerType;
    @NotBlank(message = "Customer name is required")
    private String customerName;
    private String companyName;
    @NotNull(message = "Customer phone number is required")
    private Long customerPhone;
    private String paymentTerms;
    @NotBlank(message = "Country is required")
    private String country;
    private String address;
    private String city;
    private String state;
    @NotNull(message = "Pin code is required")
    private Long pinCode;
    private List<ContactPerson> contactPersons;

}
