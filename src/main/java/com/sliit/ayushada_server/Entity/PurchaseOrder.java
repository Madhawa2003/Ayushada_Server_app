package com.sliit.ayushada_server.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "purchase_order")
@Getter
@Setter
@NoArgsConstructor
public class PurchaseOrder {

    @Id
    @Column(name = "po_id", length = 50)
    private String poId;

    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;

    @Column(name = "expected_date")
    private LocalDate expectedDate;

    @Column(name = "status", length = 30)
    private String status = "Sent";

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @Column(name = "total_amount", precision = 12, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<PurchaseOrderItem> items = new ArrayList<>();
}