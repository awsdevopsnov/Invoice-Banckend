package com.example.AdminInvoice.Payroll;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class AddedResponse<U> {
    String id;
    String message;
    HttpStatus httpStatus;
    String statusCode;

    public AddedResponse(String id, String message, HttpStatus httpStatus) {
        this.id = id;
        this.message = message;
        this.httpStatus = httpStatus;
        this.statusCode = String.valueOf(httpStatus);
    }

}
