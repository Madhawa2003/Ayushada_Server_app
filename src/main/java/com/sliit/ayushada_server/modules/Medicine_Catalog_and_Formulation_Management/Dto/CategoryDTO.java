package com.sliit.ayushada_server.modules.Medicine_Catalog_and_Formulation_Management.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter
public class CategoryDTO {
    private Long categoryId;
    @NotBlank(message = "Category name is required")
    private String name;
    private String description;
}