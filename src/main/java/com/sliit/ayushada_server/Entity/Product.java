package com.sliit.ayushada_server.Entity;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.Objects;

@Entity
public class Product {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "price")
    private Double price;
    @Basic
    @Column(name = "description")
    private String description;
    @Basic
    @Column(name = "discount")
    private Double discount;
    @Basic
    @Column(name = "quantity")
    private String quantity;
    @Basic
    @Column(name = "domain")
    private String domain;
    @OneToMany(mappedBy = "product")
    private Collection<OrderList> orderLists;
    @OneToMany(mappedBy = "product")
    private Collection<OrderReqProduct> orderrequestsproductlists;
    @ManyToOne
    @JoinColumn(name = "productType_id", referencedColumnName = "id", nullable = false)
    private ProductType productType;
    @OneToMany(mappedBy = "product")
    private Collection<SupplierLogs> supplierLogs;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id && Objects.equals(name, product.name) && Objects.equals(price, product.price) && Objects.equals(description, product.description) && Objects.equals(discount, product.discount) && Objects.equals(quantity, product.quantity) && Objects.equals(domain, product.domain);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, description, discount, quantity, domain);
    }

    public Collection<OrderList> getOrderLists() {
        return orderLists;
    }

    public void setOrderLists(Collection<OrderList> orderLists) {
        this.orderLists = orderLists;
    }

    public Collection<OrderReqProduct> getOrderrequestsproductlists() {
        return orderrequestsproductlists;
    }

    public void setOrderrequestsproductlists(Collection<OrderReqProduct> orderrequestsproductlists) {
        this.orderrequestsproductlists = orderrequestsproductlists;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public Collection<SupplierLogs> getSupplierLogs() {
        return supplierLogs;
    }

    public void setSupplierLogs(Collection<SupplierLogs> supplierLogs) {
        this.supplierLogs = supplierLogs;
    }
}
