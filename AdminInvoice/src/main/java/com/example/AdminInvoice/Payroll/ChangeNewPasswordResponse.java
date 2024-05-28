package com.example.AdminInvoice.Payroll;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ChangeNewPasswordResponse<I> {
    String message;
    HttpStatus httpStatus;
    String statusCode;

    public ChangeNewPasswordResponse(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.statusCode = String.valueOf(httpStatus.value());
    }
}
