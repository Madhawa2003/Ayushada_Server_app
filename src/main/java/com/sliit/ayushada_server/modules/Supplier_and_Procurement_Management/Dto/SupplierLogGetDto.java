package com.sliit.ayushada_server.modules.Supplier_and_Procurement_Management.Dto;

import java.sql.Timestamp;

public class SupplierLogGetDto {

    private int id;
    private Timestamp date;
    private int productId;
    private int supplierId;

    public SupplierLogGetDto() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }
}