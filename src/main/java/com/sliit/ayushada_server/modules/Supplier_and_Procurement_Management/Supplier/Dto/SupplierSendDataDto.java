package com.sliit.ayushada_server.modules.Supplier_and_Procurement_Management.Supplier.Dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupplierSendDataDto {

    private String companyName;
    private String personName;
    private String phoneNo;
    private String email;
    private String address;
    private Byte activeStatus;
}
