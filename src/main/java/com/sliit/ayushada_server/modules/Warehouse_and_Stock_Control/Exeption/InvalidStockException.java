package com.sliit.ayushada_server.modules.Warehouse_and_Stock_Control.Exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidStockException extends RuntimeException {
    public InvalidStockException(String message) {
        super(message);
    }
}