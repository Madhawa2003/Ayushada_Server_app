package com.sliit.ayushada_server.modules.Supplier_and_Procurement_Management.Supplier.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class SupplierDuplicateException extends RuntimeException {

    public SupplierDuplicateException(String email) {
        super(String.format("Supplier already exists with email: %s", email));
    }
}