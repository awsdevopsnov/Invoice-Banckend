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
@Document(collection = "register_db")
public class Register {
    @Id
    private String id;
    private String username;
    private String password;
    private Role userRole;
    private String  userEmail;
    private long userMobile;
    private String userAccess;
    private String description;
}
