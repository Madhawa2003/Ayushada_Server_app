package com.sliit.ayushada_server.modules.Warehouse_and_Stock_Control.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockAdjustRequest {
    private Integer quantity;
    private String reason;

    public StockAdjustRequest() {}

    public StockAdjustRequest(Integer quantity, String reason) {
        this.quantity = quantity;
        this.reason = reason;
    }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}