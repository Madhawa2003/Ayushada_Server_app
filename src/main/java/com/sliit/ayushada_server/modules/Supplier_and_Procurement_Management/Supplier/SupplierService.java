package com.sliit.ayushada_server.modules.Supplier_and_Procurement_Management.Supplier;

import com.sliit.ayushada_server.modules.Supplier_and_Procurement_Management.Supplier.Dto.SupplierSendDataDto;
import com.sliit.ayushada_server.modules.Supplier_and_Procurement_Management.Supplier.Exception.SupplierDuplicateException;
import com.sliit.ayushada_server.modules.Supplier_and_Procurement_Management.Supplier.Exception.SupplierNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierService {

    @Autowired
    SupplierRepository supplierRepository;

    @Autowired
    SupplierMapper supplierMapper;

    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }
    public SupplierSendDataDto getSupplierById(int id) {
        Supplier supdata = supplierRepository.findById(id)
                .orElseThrow(() -> new SupplierNotFoundException(id));
        return supplierMapper.toDto(supdata);
    }

    public SupplierSendDataDto saveSupplier(SupplierSendDataDto supdto) {
        if (supplierRepository.existsByEmail(supdto.getEmail())) {
            throw new SupplierDuplicateException(supdto.getEmail());
        }
        Supplier savedSupplier = supplierRepository.save(supplierMapper.toEntity(supdto));
        return supplierMapper.toDto(savedSupplier);
    }
}
