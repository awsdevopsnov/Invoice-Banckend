package com.example.AdminInvoice.CustomerSubclass;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactPerson {
    private String contactName;
    private String contactEmail;
    private Long contactPhone;
}
