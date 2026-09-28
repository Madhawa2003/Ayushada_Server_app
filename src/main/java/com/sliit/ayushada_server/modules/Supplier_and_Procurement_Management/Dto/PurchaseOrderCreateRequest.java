package com.sliit.ayushada_server.modules.Supplier_and_Procurement_Management.Dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class PurchaseOrderCreateRequest {
    private String poId;
    private Long supplierId;
    private String expectedDate;
    private String note;
    private BigDecimal totalAmount;
    private List<ItemRequest> items;

    @Getter
    @Setter
    public static class ItemRequest {
        private String medicineOrHerbName;
        private int quantityOrdered;
        private String unit;
        private BigDecimal unitCost;
    }
}