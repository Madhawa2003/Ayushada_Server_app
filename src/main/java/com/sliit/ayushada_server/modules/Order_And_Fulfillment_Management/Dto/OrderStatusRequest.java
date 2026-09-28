package com.sliit.ayushada_server.modules.Order_And_Fulfillment_Management.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderStatusRequest {
    private String status; // Processing, Prepared, Dispatched, Delivered, Cancelled
    private String assignedTo;
}