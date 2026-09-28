package com.sliit.ayushada_server.modules.Billing_Invoicing_And_Digital_Payments.Dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class PaymentRequest {
    private Long invoiceId;
    private Long payTypeId;
    private BigDecimal amount;
    private String customerId;
    private String customerName;
    private String shippingAddress;
    private String phoneNo;
    private List<CheckoutItemDTO> items;

    @Getter
    @Setter
    public static class CheckoutItemDTO {
        private Long medicineId;
        private String medicineName;
        private int quantity;
        private BigDecimal unitPrice;
    }
}