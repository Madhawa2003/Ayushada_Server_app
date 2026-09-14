package com.sliit.ayushada_server.modules.Supplier_and_Procurement_Management;


import com.sliit.ayushada_server.Entity.PurchaseOrder;
import com.sliit.ayushada_server.Entity.Supplier;
import com.sliit.ayushada_server.modules.Supplier_and_Procurement_Management.Exeption.InvalidSupplierException;
import com.sliit.ayushada_server.modules.Supplier_and_Procurement_Management.Repositories.PurchaseOrderRepository;
import com.sliit.ayushada_server.modules.Supplier_and_Procurement_Management.Repositories.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class SapmService {

    @Autowired
    SupplierRepository supplierRepository;
    @Autowired
    PurchaseOrderRepository orderRepository;


    public SapmService(SupplierRepository supplierRepository, PurchaseOrderRepository orderRepository) {
        this.supplierRepository = supplierRepository;
        this.orderRepository = orderRepository;
    }

    public Supplier addSupplier(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    public Supplier updateSupplier(Supplier supplier) {
        // Retrieve the existing supplier from the database
        Supplier existingSupplier = supplierRepository.findById(Math.toIntExact(supplier.getId()))
                .orElseThrow(() -> new InvalidSupplierException("Supplier not found with ID: " + supplier.getId()));

        // Update the fields with the new data
        existingSupplier.setCompanyName(supplier.getCompanyName());
        existingSupplier.setPersonName(supplier.getPersonName());
        existingSupplier.setEmail(supplier.getEmail());
        existingSupplier.setPhoneNo(supplier.getPhoneNo());
        existingSupplier.setAddress(supplier.getAddress());
        existingSupplier.setActiveStatus(supplier.getActiveStatus());

        // Save and return the updated entity
        return supplierRepository.save(existingSupplier);
    }



    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    public PurchaseOrder issuePurchaseOrder(PurchaseOrder request) {
        Supplier supplier = supplierRepository.findById(Math.toIntExact(request.getSupplier().getId()))
                .orElseThrow(() -> new InvalidSupplierException("Supplier not found"));

        request.setSupplier(supplier);
        request.setOrderDate(Instant.from(LocalDateTime.now()));
        request.setStatus("PENDING");
        return orderRepository.save(request);
    }


}