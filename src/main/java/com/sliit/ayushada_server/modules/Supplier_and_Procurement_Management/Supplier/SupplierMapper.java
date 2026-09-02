package com.sliit.ayushada_server.modules.Supplier_and_Procurement_Management.Supplier;

import com.sliit.ayushada_server.modules.Supplier_and_Procurement_Management.Supplier.Dto.SupplierSendDataDto;
import org.springframework.stereotype.Component;

@Component
public class SupplierMapper {


    public SupplierSendDataDto toDto(Supplier supplier) {
        if (supplier == null) {
            return null;
        }

        SupplierSendDataDto dto = new SupplierSendDataDto();
        dto.setPersonName(supplier.getPersonName());
        dto.setEmail(supplier.getEmail());
        dto.setActiveStatus(supplier.getActiveStatus());
        dto.setAddress(supplier.getAddress());
        dto.setPhoneNo(supplier.getPhoneNo());
        dto.setCompanyName(supplier.getCompanyName());

        return dto;
    }


    public Supplier toEntity(SupplierSendDataDto dto) {
        if (dto == null) {
            return null;
        }

        Supplier supplier = new Supplier();
        supplier.setPersonName(dto.getPersonName());
        supplier.setEmail(dto.getEmail());
        supplier.setActiveStatus(dto.getActiveStatus());
        supplier.setAddress(dto.getAddress());
        supplier.setPhoneNo(dto.getPhoneNo());
        supplier.setCompanyName(dto.getCompanyName());

        return supplier;
    }
}