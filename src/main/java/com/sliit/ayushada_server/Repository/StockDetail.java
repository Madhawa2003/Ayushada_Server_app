package com.sliit.ayushada_server.Repository;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "stock_details")
public class StockDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Stock_id", nullable = false)
    private Stock stock;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Medicine_id", nullable = false)
    private Medicine medicine;

    @Lob
    @Column(name = "note", nullable = false)
    private String note;

    @Column(name = "quantity")
    private Double quantity;


}