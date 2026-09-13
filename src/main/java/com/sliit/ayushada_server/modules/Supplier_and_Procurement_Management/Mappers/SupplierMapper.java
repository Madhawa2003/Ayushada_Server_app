package com.sliit.ayushada_server.modules.Supplier_and_Procurement_Management.Mappers;

import com.sliit.ayushada_server.Repository.Supplier;
import com.sliit.ayushada_server.modules.Supplier_and_Procurement_Management.Dto.SupplieGetDto;
import com.sliit.ayushada_server.modules.Supplier_and_Procurement_Management.Dto.SupplierCreateDto;
import org.springframework.stereotype.Component;

@Component
public class SupplierMapper {

    public Supplier toEntity(SupplierCreateDto dto) {
        if (dto == null) return null;

        Supplier entity = new Supplier();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setIsActive((byte) 1);
        return entity;
    }

    public SupplieGetDto toGetDto(Supplier entity) {
        if (entity == null) return null;

        SupplieGetDto dto = new SupplieGetDto();
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        return dto;
    }
}