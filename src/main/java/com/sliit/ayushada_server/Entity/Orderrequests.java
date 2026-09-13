package com.sliit.ayushada_server.Entity;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.Objects;

@Entity
public class Orderrequests {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "date")
    private String date;
    @Basic
    @Column(name = "status")
    private String status;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;
    @OneToMany(mappedBy = "orderrequests")
    private Collection<OrderReqProduct> orderrequestsproductlists;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Orderrequests that = (Orderrequests) o;
        return id == that.id && Objects.equals(date, that.date) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, status);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Collection<OrderReqProduct> getOrderrequestsproductlists() {
        return orderrequestsproductlists;
    }

    public void setOrderrequestsproductlists(Collection<OrderReqProduct> orderrequestsproductlists) {
        this.orderrequestsproductlists = orderrequestsproductlists;
    }
}
