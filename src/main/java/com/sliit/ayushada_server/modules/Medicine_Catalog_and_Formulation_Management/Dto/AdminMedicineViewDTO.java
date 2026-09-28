package com.sliit.ayushada_server.modules.Medicine_Catalog_and_Formulation_Management.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter

public class AdminMedicineViewDTO {
    private Long id;
    private String name;
    private String category;
    private BigDecimal price;
    private String imageUrl;
    private boolean prescriptionRequired;
    private int stock;
    public AdminMedicineViewDTO() {}
    public AdminMedicineViewDTO(Long id, String name, String category, BigDecimal price, String imageUrl, boolean prescriptionRequired, int stock) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.imageUrl = imageUrl;
        this.prescriptionRequired = prescriptionRequired;
        this.stock = stock;
    }
}