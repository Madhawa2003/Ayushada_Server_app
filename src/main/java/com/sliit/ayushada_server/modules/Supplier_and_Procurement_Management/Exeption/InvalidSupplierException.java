package com.sliit.ayushada_server.modules.Supplier_and_Procurement_Management.Exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidSupplierException extends RuntimeException {

    public InvalidSupplierException(String message) {
        super(message);
    }

    public InvalidSupplierException(String message, Throwable cause) {
        super(message, cause);
    }
}