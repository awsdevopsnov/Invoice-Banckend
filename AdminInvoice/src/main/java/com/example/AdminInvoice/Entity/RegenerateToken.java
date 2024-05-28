package com.example.AdminInvoice.Entity;

import lombok.Data;

@Data
public class RegenerateToken {

    private String accessToken;
    private String refreshToken;

    public RegenerateToken(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
