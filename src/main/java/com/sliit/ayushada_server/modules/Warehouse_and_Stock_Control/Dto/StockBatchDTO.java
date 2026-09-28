package com.sliit.ayushada_server.modules.Warehouse_and_Stock_Control.Dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
public class StockBatchDTO {
    private Long id;
    private String batchNumber;
    private Long medicineId;
    private String medicineName;
    private String category;
    private int quantity;
    private String receivedDate;
    private String expiryDate;
    private String warehouseLocation;
    private String status;

    public StockBatchDTO() {}

    public StockBatchDTO(Long id, String batchNumber, Long medicineId, String medicineName, String category, int quantity, String receivedDate, String expiryDate, String warehouseLocation, String status) {
        this.id = id;
        this.batchNumber = batchNumber;
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.category = category;
        this.quantity = quantity;
        this.receivedDate = receivedDate;
        this.expiryDate = expiryDate;
        this.warehouseLocation = warehouseLocation;
        this.status = status;
    }

}