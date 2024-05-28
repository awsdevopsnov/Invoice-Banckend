package com.example.AdminInvoice.Entity;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
@Document(collection = "user_db")
public class User {
    @Id
    private String id;
    @NotBlank(message = "User name cannot be blank")
    private String userName;
    @NotBlank(message = "User Designation cannot be blank")
    private String userDesignation;
    @NotBlank(message = "User role cannot be blank")
    private String userRole;
    @NotBlank(message = "User Access cannot be blank")
    private String userAccess;
    @NotBlank(message = "User Email cannot be blank")
    @Email(message = "Invalid email format")
    private String userEmail;
    private Long phoneNumber;
}
