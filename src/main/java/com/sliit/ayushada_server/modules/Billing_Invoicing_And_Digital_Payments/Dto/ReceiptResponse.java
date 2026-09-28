package com.sliit.ayushada_server.modules.Billing_Invoicing_And_Digital_Payments.Dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Builder
@Getter
@Setter
public class ReceiptResponse {
    private String receiptReference;
    private String invoiceNumber;
    private String orderNumber;
    private LocalDateTime paymentDate;
    private String paymentMethod;
    private String customerName;
    private String shippingAddress;
    private String phoneNo;
    private List<ReceiptItemDTO> items;
    private BigDecimal subtotal;
    private BigDecimal deliveryFee;
    private BigDecimal netTotal;
    private String status;


    @Builder
   @Setter
   @Getter
    public static class ReceiptItemDTO {
        private String medicineName;
        private int quantity;
        private BigDecimal unitPrice;
        private BigDecimal total;
    }
}