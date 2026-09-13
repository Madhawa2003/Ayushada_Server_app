package com.sliit.ayushada_server.Entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "orderrequestsproductlist", schema = "se_proj_v2", catalog = "")
public class OrderReqProduct {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @ManyToOne
    @JoinColumn(name = "orderRequests_id", referencedColumnName = "id", nullable = false)
    private Orderrequests orderrequests;
    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    private Product product;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderReqProduct that = (OrderReqProduct) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Orderrequests getOrderrequests() {
        return orderrequests;
    }

    public void setOrderrequests(Orderrequests orderrequests) {
        this.orderrequests = orderrequests;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
