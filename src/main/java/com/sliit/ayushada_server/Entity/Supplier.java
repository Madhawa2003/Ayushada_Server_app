package com.sliit.ayushada_server.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "supplier")
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 150)
    @NotNull
    @Column(name = "company_name", nullable = false, length = 150)
    private String companyName;

    @Size(max = 100)
    @Column(name = "person_name", length = 100)
    private String personName;

    @Size(max = 100)
    @Column(name = "email", length = 100)
    private String email;

    @Size(max = 20)
    @Column(name = "phone_no", length = 20)
    private String phoneNo;

    @Lob
    @Column(name = "address")
    private String address;

    @ColumnDefault("1")
    @Column(name = "active_status")
    private Boolean activeStatus;


}