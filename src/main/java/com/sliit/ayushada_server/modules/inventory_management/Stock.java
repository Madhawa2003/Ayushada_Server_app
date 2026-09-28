package com.sliit.ayushada_server.modules.inventory_management;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "stock_batches")
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String batchNumber;
    private Integer quantity;
    private LocalDate mfgDate;
    private LocalDate expDate;
    private Long medicineId;

    // Constructors
    public Stock() {}

    public Stock(String batchNumber, Integer quantity, LocalDate mfgDate, LocalDate expDate, Long medicineId) {
        this.batchNumber = batchNumber;
        this.quantity = quantity;
        this.mfgDate = mfgDate;
        this.expDate = expDate;
        this.medicineId = medicineId;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getBatchNumber() { return batchNumber; }
    public void setBatchNumber(String batchNumber) { this.batchNumber = batchNumber; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public LocalDate getMfgDate() { return mfgDate; }
    public void setMfgDate(LocalDate mfgDate) { this.mfgDate = mfgDate; }

    public LocalDate getExpDate() { return expDate; }
    public void setExpDate(LocalDate expDate) { this.expDate = expDate; }

    public Long getMedicineId() { return medicineId; }
    public void setMedicineId(Long medicineId) { this.medicineId = medicineId; }
}