package com.example.AdminInvoice.Controller;

import lombok.*;
import org.springframework.http.HttpStatus;

@Data
public class LoginMessage {

    String message;
    Boolean status;
    String accessToken;
    String refreshToken;
    String userRole;
    String userName;
    HttpStatus httpStatus;
    String statusCode;


    public LoginMessage(String string, boolean b, String tok, String refreshToken, String userRole, String userName, HttpStatus httpStatus) {
        this.message=string;
        this.status=b;
        this.accessToken=tok;
        this.refreshToken=refreshToken;
        this.userRole = userRole;
        this.userName = userName;
        this.httpStatus = httpStatus;
        this.statusCode = String.valueOf(httpStatus.value());
    }


    public void LoginMesage(String message, Boolean status, String token,String refreshToken, String userName,HttpStatus httpStatus) {
        this.message = message;
        this.status = status;
        this.accessToken = token;
        this.refreshToken=refreshToken;
        this.userName=userName;
        this.httpStatus = httpStatus;

    }

}
