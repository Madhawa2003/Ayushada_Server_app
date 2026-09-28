package com.sliit.ayushada_server.modules.Order_And_Fulfillment_Management.Dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderDTO {
    private Long orderId;
    private String orderNumber;
    private LocalDateTime orderDate;
    private String status;

    @NotBlank(message = "Shipping address is mandatory")
    private String shippingAddress;

    private BigDecimal deliveryFee;
    private String assignedTo;

    @NotBlank(message = "Customer ID is required")
    private String customerId;
    private String customerName;

    private Long prescriptionId;

    @NotEmpty(message = "Order must contain at least one item")
    private List<OrderItemDTO> items;

    private BigDecimal totalAmount;

   @Setter
   @Getter
    public static class OrderItemDTO {
        private Long orderItemId;
        @NotNull private Long medicineId;
        private String medicineName;
        @NotNull @Min(1) private Integer quantity;
        @NotNull private BigDecimal unitPrice;
    }
}