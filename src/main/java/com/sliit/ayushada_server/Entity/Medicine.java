package com.sliit.ayushada_server.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "medicine")
@Getter
@Setter
@NoArgsConstructor
public class Medicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medicine_id")
    private Long medicineId;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "sinhala_name", length = 150)
    private String sinhalaName;

    @Column(name = "type", length = 50)
    private String type;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "intake", length = 255)
    private String intake;

    @Column(name = "instructions", columnDefinition = "TEXT")
    private String instructions;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "prescription_required")
    private Boolean prescriptionRequired = false;

    @Column(name = "is_archived")
    private Boolean isArchived = false;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}