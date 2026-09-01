package com.sliit.ayushada_server.Repository;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "medicine")
public class Medicine {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", length = 45)
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "usage_instructions", length = 100)
    private String usageInstructions;

    @Column(name = "price")
    private Double price;

    @Column(name = "instock", length = 1)
    private String instock;

    @Lob
    @Column(name = "image")
    private String image;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Unit_type_id", nullable = false)
    private UnitType unitType;


}