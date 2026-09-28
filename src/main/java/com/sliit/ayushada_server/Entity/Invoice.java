package com.sliit.ayushada_server.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoice")
@Getter
@Setter
@NoArgsConstructor
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_id")
    private Long invoiceId;

    @Column(name = "invoice_number", unique = true, length = 50)
    private String invoiceNumber;

    @Column(name = "date")
    private LocalDateTime date = LocalDateTime.now();

    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Column(name = "discount", precision = 10, scale = 2)
    private BigDecimal discount = BigDecimal.ZERO;

    @Column(name = "tax", precision = 10, scale = 2)
    private BigDecimal tax = BigDecimal.ZERO;

    @Column(name = "delivery_fee", precision = 10, scale = 2)
    private BigDecimal deliveryFee = new BigDecimal("250.00");

    @Column(name = "net_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal netTotal;

    @Column(name = "net_term", length = 50)
    private String netTerm = "Immediate";

    @Column(name = "status", length = 30)
    private String status = "UNPAID";

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", nullable = false)
    private CustomerOrder customerOrder;
}