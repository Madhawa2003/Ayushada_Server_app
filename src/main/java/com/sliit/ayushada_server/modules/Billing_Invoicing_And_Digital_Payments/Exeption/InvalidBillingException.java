package com.sliit.ayushada_server.modules.Billing_Invoicing_And_Digital_Payments.Repository;

public class InvalidBillingException extends RuntimeException {
    public InvalidBillingException(String message) {
        super(message);
    }
}
