package com.example.AdminInvoice.Payroll;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class UpdateResponse {

    String updateId;
    String message;
    HttpStatus httpStatus;
    String statusCode;

    public UpdateResponse(String updateId, String message, HttpStatus httpStatus) {
        this.updateId = updateId;
        this.message = message;
        this.httpStatus = httpStatus;
        this.statusCode =  String.valueOf(httpStatus.value());
    }
}
