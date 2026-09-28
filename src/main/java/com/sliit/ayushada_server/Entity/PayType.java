package com.sliit.ayushada_server.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pay_type")
@Getter
@Setter
@NoArgsConstructor
public class PayType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pay_type_id")
    private Long payTypeId;

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
}