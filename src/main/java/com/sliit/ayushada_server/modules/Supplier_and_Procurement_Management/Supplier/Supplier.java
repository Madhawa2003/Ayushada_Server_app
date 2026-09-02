package com.sliit.ayushada_server.modules.Supplier_and_Procurement_Management.Supplier;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "supplier")
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "company_name", length = 45)
    private String companyName;

    @Column(name = "person_name", length = 45)
    private String personName;

    @Column(name = "phone_no", length = 45)
    private String phoneNo;

    @Column(name = "email", length = 45)
    private String email;

    @Column(name = "address", length = 45)
    private String address;

    @Column(name = "active_status")
    private Byte activeStatus;


}