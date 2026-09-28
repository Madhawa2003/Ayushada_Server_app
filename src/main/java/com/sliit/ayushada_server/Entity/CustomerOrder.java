package com.sliit.ayushada_server.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customer_order")
@Getter
@Setter
@NoArgsConstructor
public class CustomerOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "order_number", unique = true, length = 50)
    private String orderNumber;

    @Column(name = "order_date")
    private LocalDateTime orderDate = LocalDateTime.now();

    @Column(name = "status", length = 40)
    private String status = "Processing";

    @Column(name = "shipping_address", columnDefinition = "TEXT", nullable = false)
    private String shippingAddress;

    @Column(name = "delivery_fee", precision = 10, scale = 2)
    private BigDecimal deliveryFee = new BigDecimal("250.00");

    @Column(name = "assigned_to", length = 100)
    private String assignedTo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "prescription_id")
    private Prescription prescription;

    @OneToMany(mappedBy = "customerOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<OrderItem> items = new ArrayList<>();
}