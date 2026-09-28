package com.sliit.ayushada_server.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "supplier")
@Getter
@Setter
@NoArgsConstructor
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "supplier_id")
    private Long supplierId;

    @Column(name = "company_name", nullable = false, length = 150)
    private String companyName;

    @Column(name = "person_name", length = 100)
    private String personName;

    @Column(name = "phone_no", length = 20)
    private String phoneNo;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "supplied_herbs", length = 255)
    private String suppliedHerbs;

    @Column(name = "rating", precision = 2, scale = 1)
    private BigDecimal rating = new BigDecimal("5.0");

    @Column(name = "active_status")
    private Boolean activeStatus = true;
}