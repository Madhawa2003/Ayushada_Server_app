package com.sliit.ayushada_server.modules.Prescription_and_Verification_Management.Dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CustomerApproveAndPayRequest {
    private String shippingAddress;
    private Long payTypeId; // 1 = Credit Card, 2 = COD
    private BigDecimal amountPaid;
}