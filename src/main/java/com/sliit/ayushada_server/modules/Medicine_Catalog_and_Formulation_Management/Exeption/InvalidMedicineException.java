package com.sliit.ayushada_server.modules.Medicine_Catalog_and_Formulation_Management.Exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidMedicineException extends RuntimeException {
    public InvalidMedicineException(String message) {
        super(message);
    }
}