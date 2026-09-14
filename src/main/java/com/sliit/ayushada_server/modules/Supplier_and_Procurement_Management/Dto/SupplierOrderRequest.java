package com.sliit.ayushada_server.modules.Supplier_and_Procurement_Management.Dto;

public class SupplierOrderRequest {

    private int productId;
    private int supplierId;

    // Default constructor
    public SupplierOrderRequest() {}

    public SupplierOrderRequest(int productId, int supplierId) {
        this.productId = productId;
        this.supplierId = supplierId;
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