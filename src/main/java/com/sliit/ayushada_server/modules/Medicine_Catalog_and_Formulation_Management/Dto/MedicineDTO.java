package com.sliit.ayushada_server.modules.Medicine_Catalog_and_Formulation_Management.Dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Getter @Setter
public class MedicineDTO {
    private Long medicineId;

    @NotBlank(message = "Medicine name is required")
    private String name;

    private String sinhalaName;

    @NotBlank(message = "Type formulation is required")
    private String type;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than zero")
    private BigDecimal price;

    @NotBlank(message = "Intake instructions are required")
    private String intake;

    @NotBlank(message = "Instructions are required")
    private String instructions;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Image URL or icon is required")
    private String imageUrl;

    private Boolean prescriptionRequired;
    private Boolean isArchived;

    @NotNull(message = "Category ID is required")
    private Long categoryId;
    private String categoryName;

    private Integer totalStock;
}