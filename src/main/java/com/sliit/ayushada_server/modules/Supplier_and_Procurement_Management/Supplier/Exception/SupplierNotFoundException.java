package com.sliit.ayushada_server.modules.Supplier_and_Procurement_Management.Supplier.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SupplierNotFoundException extends RuntimeException {

    public SupplierNotFoundException(String message) {
        super(message);
    }

    public SupplierNotFoundException(int id) {
        super(String.format("Supplier not found with ID: %d", id));
    }
}