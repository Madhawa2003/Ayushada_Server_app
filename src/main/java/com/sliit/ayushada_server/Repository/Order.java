package com.sliit.ayushada_server.Repository;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

@Entity
public class Order {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "date_time")
    private Timestamp dateTime;
    @Basic
    @Column(name = "is_approved")
    private Byte isApproved;
    @Basic
    @Column(name = "payment_id")
    private int paymentId;
    @Basic
    @Column(name = "totel")
    private Double totel;
    @ManyToOne
    @JoinColumn(name = "Payment_type_id", referencedColumnName = "id", nullable = false)
    private PaymentType paymentType;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;
    @OneToMany(mappedBy = "order")
    private Collection<OrderList> orderLists;
    @OneToMany(mappedBy = "order")
    private Collection<Prescription> prescriptions;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getDateTime() {
        return dateTime;
    }

    public void setDateTime(Timestamp dateTime) {
        this.dateTime = dateTime;
    }

    public Byte getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(Byte isApproved) {
        this.isApproved = isApproved;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public Double getTotel() {
        return totel;
    }

    public void setTotel(Double totel) {
        this.totel = totel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id && paymentId == order.paymentId && Objects.equals(dateTime, order.dateTime) && Objects.equals(isApproved, order.isApproved) && Objects.equals(totel, order.totel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateTime, isApproved, paymentId, totel);
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Collection<OrderList> getOrderLists() {
        return orderLists;
    }

    public void setOrderLists(Collection<OrderList> orderLists) {
        this.orderLists = orderLists;
    }

    public Collection<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(Collection<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }
}
