package com.example.AdminInvoice.Login;

import com.example.AdminInvoice.JwtToken.JwtUtill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class TokenValidator implements TokenvalidateInteface{
    @Autowired private JwtUtill jwtUtill;
    public boolean isValidToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            return jwtUtill.validateToken(jwtToken);
        }
        return false;
    }


}
