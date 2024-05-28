package com.example.AdminInvoice.Payroll;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class DeleteResponse {

    String deleteId;
    String message;
    HttpStatus httpStatus;
    String statusCode;

    public DeleteResponse(String deleteId, String message, HttpStatus httpStatus) {
        this.deleteId = deleteId;
        this.message = message;
        this.httpStatus = httpStatus;
        this.statusCode =  String.valueOf(httpStatus.value());
    }
}
