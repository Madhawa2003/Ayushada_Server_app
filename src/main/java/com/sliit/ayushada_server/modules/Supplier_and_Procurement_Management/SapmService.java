package com.sliit.ayushada_server.modules.Supplier_and_Procurement_Management;

import com.sliit.ayushada_server.Entity.Product;
import com.sliit.ayushada_server.Entity.Supplier;
import com.sliit.ayushada_server.Entity.SupplierLogs;
import com.sliit.ayushada_server.modules.Supplier_and_Procurement_Management.Dto.SupplieGetDto;
import com.sliit.ayushada_server.modules.Supplier_and_Procurement_Management.Dto.SupplierCreateDto;
import com.sliit.ayushada_server.modules.Supplier_and_Procurement_Management.Dto.SupplierOrderRequest;
import com.sliit.ayushada_server.modules.Supplier_and_Procurement_Management.Exeption.InvalidSupplierException;
import com.sliit.ayushada_server.modules.Supplier_and_Procurement_Management.Exeption.ResourceNotFoundException;
import com.sliit.ayushada_server.modules.Supplier_and_Procurement_Management.Mappers.SupplierMapper;
import com.sliit.ayushada_server.modules.Supplier_and_Procurement_Management.Repositories.ProductRepository;
import com.sliit.ayushada_server.modules.Supplier_and_Procurement_Management.Repositories.SupplierLogsRepository;
import com.sliit.ayushada_server.modules.Supplier_and_Procurement_Management.Repositories.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class SapmService {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    SupplierLogsRepository supplierLogsRepository;


    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    public SapmService(SupplierRepository supplierRepository, SupplierMapper supplierMapper) {
        this.supplierRepository = supplierRepository;
        this.supplierMapper = supplierMapper;
    }

    @Transactional
    public SupplieGetDto addSupplier(SupplierCreateDto supplierDto) {
        if (supplierDto == null) {
            throw new InvalidSupplierException("Supplier details must not be null.");
        }
        if (supplierDto.getName() == null || supplierDto.getName().trim().isEmpty()) {
            throw new InvalidSupplierException("Supplier name is required.");
        }

        Supplier supplier = supplierMapper.toEntity(supplierDto);
        Supplier savedSupplier = supplierRepository.save(supplier);
        return supplierMapper.toGetDto(savedSupplier);
    }

    public SupplieGetDto getSupplier(int supplierId) {
        return supplierRepository.findById(supplierId)
                .map(supplierMapper::toGetDto)
                .orElseThrow(() -> new InvalidSupplierException("Supplier with ID " + supplierId + " not found."));
    }

    public List<SupplieGetDto> getAllSuppliers() {
        return supplierRepository.findAll()
                .stream()
                .map(supplierMapper::toGetDto)
                .collect(Collectors.toList());
    }

    public SupplierLogs addSupplierOrder(SupplierOrderRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + request.getProductId()));

        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new InvalidSupplierException("Supplier not found with ID: " + request.getSupplierId()));

        SupplierLogs newOrderLog = new SupplierLogs();
        newOrderLog.setProduct(product);
        newOrderLog.setSupplier(supplier);

        // Wrapped the long value in java.sql.Date to resolve the type mismatch
        newOrderLog.setDate(new java.sql.Date(System.currentTimeMillis()));

        return supplierLogsRepository.save(newOrderLog);
    }


}