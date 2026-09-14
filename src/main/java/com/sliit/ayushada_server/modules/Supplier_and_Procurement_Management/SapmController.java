package com.sliit.ayushada_server.modules.Supplier_and_Procurement_Management;
import com.sliit.ayushada_server.Entity.PurchaseOrder;
import com.sliit.ayushada_server.Entity.Supplier;
import com.sliit.ayushada_server.modules.Supplier_and_Procurement_Management.Dto.SupplieGetDto;
import com.sliit.ayushada_server.modules.Supplier_and_Procurement_Management.Dto.SupplierCreateDto;
import com.sliit.ayushada_server.modules.Supplier_and_Procurement_Management.Dto.SupplierOrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/supplier_and_Procurement_Management")
public class SapmController {
    @Autowired
    private SapmService sapmService;

    @PostMapping("/suppliers")
    public Supplier addSupplier(@RequestBody Supplier supplier) {
        return sapmService.addSupplier(supplier);
    }

    @GetMapping("/suppliers")
    public List<Supplier> getSuppliers() {
        return sapmService.getAllSuppliers();
    }

    @PostMapping("/orders")
    public PurchaseOrder createOrder(@RequestBody PurchaseOrder order) {
        return sapmService.issuePurchaseOrder(order);
    }

    @PutMapping("/suppliers/{id}")
    public ResponseEntity<Supplier> updateSupplier(@PathVariable Long id, @RequestBody Supplier supplier) {
        // Ensure the ID in the path matches the body
        supplier.setId(id);
        Supplier updatedSupplier = sapmService.updateSupplier(supplier);
        return ResponseEntity.ok(updatedSupplier);
    }
}
