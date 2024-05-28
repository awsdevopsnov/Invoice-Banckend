package com.example.AdminInvoice.Payroll;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ForgetResponse {

    Object mail;
    String message;
    HttpStatus httpStatus;
    String statusCode;

    public ForgetResponse(Object mail, String message, HttpStatus httpStatus) {
        this.mail = mail;
        this.message = message;
        this.httpStatus = httpStatus;
        this.statusCode = String.valueOf(httpStatus.value());
    }
}
