package com.sliit.ayushada_server.modules.Billing_Invoicing_And_Digital_Payments.Exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidBillingException extends RuntimeException {
    public InvalidBillingException(String message) {
        super(message);
    }
}