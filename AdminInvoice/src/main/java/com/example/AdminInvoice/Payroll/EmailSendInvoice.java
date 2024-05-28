package com.example.AdminInvoice.Payroll;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class EmailSendInvoice {

     String Email;
     String Message;
     HttpStatus httpStatus;
     String statusCode;

    public EmailSendInvoice(String email, String message, HttpStatus httpStatus) {
        Email = email;
        Message = message;
        this.httpStatus = httpStatus;
        this.statusCode = String.valueOf(httpStatus);
    }


}
