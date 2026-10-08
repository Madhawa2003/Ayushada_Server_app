package com.sliit.ayushada_server.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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

    @NotBlank(message = "Company name is required")
    @Column(name = "company_name", nullable = false, length = 150)
    private String companyName;

    @Column(name = "person_name", length = 100)
    private String personName;

    @Pattern(regexp = "^(\\+?[0-9]{9,15})?$", message = "Invalid phone number format")
    @Column(name = "phone_no", length = 20)
    private String phoneNo;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "supplied_herbs", length = 255)
    private String suppliedHerbs;

    @DecimalMin(value = "0.0", inclusive = true, message = "Rating must be at least 0.0")
    @DecimalMax(value = "5.0", inclusive = true, message = "Rating cannot exceed 5.0")
    @Digits(integer = 1, fraction = 1, message = "Rating format must be X.X")
    @Column(name = "rating", precision = 2, scale = 1)
    private BigDecimal rating = new BigDecimal("5.0");


    @Column(name = "active_status")
    private Boolean activeStatus = true;
}