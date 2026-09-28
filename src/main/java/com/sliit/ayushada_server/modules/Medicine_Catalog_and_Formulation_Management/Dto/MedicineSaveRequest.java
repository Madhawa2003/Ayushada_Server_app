package com.sliit.ayushada_server.modules.Medicine_Catalog_and_Formulation_Management.Dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class MedicineSaveRequest {
    private String name;
    private String category;
    private BigDecimal price;
    private String imageUrl;
    private boolean prescriptionRequired;
    private int initialStock;
}