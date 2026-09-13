package com.sliit.ayushada_server.modules.Supplier_and_Procurement_Management;
import com.sliit.ayushada_server.Entity.SupplierLogs;
import com.sliit.ayushada_server.modules.Supplier_and_Procurement_Management.Dto.SupplieGetDto;
import com.sliit.ayushada_server.modules.Supplier_and_Procurement_Management.Dto.SupplierCreateDto;
import com.sliit.ayushada_server.modules.Supplier_and_Procurement_Management.Dto.SupplierOrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/supplier_and_Procurement_Management")
public class SapmController {
    @Autowired
    private SapmService sapmService;

    @PostMapping("/add-new")
    public SupplieGetDto addnew(@RequestBody SupplierCreateDto supplier) {
        return sapmService.addSupplier(supplier);
    }

    @GetMapping()
    public List<SupplieGetDto> getAllSuppliers() {
        return sapmService.getAllSuppliers();
    }

    @GetMapping("/{id}")
    public SupplieGetDto get(@PathVariable("id") int id) {
        return sapmService.getSupplier(id);
    }

    @PostMapping("/new-order")
    public SupplierLogs neworder(@RequestBody SupplierOrderRequest supplierOrderRequest) {
        return sapmService.addSupplierOrder(supplierOrderRequest);
    }
}
