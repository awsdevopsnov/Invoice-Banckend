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
@Document(collection = "login_db")
public class Login {
    @Id
    private String id;
    private String username;
    private String password;
    private String accessToken;
    private String refreshToken;
    private String userRole;


}
