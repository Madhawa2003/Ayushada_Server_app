package com.sliit.ayushada_server.modules.Supplier_and_Procurement_Management;

import com.sliit.ayushada_server.Entity.PurchaseOrder;
import com.sliit.ayushada_server.Entity.Supplier;
import com.sliit.ayushada_server.modules.Supplier_and_Procurement_Management.Dto.PurchaseOrderCreateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/supplier_and_Procurement_Management")
@CrossOrigin(origins = "http://localhost:4200")
public class SapmController {

    @Autowired
    private SapmService sapmService;

    // 1. Supplier endpoints
    @PostMapping("/suppliers")
    public Supplier addSupplier(@RequestBody Supplier supplier) {
        return sapmService.addSupplier(supplier);
    }

    @GetMapping("/suppliers")
    public List<Supplier> getSuppliers() {
        return sapmService.getAllSuppliers();
    }

    @PutMapping("/suppliers/{id}")
    public Supplier updateSupplier(@PathVariable Long id, @RequestBody Supplier supplier) {
        return sapmService.updateSupplier(id, supplier);
    }

    @PatchMapping("/suppliers/{id}/toggle-status")
    public Supplier toggleSupplierStatus(@PathVariable Long id) {
        return sapmService.toggleSupplierStatus(id);
    }

    // 2. Purchase Order endpoints
    @GetMapping("/orders")
    public List<PurchaseOrder> getOrders() {
        return sapmService.getAllOrders();
    }

    @PostMapping("/orders")
    public PurchaseOrder createOrder(@RequestBody PurchaseOrderCreateRequest req) {
        return sapmService.issuePurchaseOrder(req);
    }

    @PutMapping("/orders/{poId}/delivered")
    public PurchaseOrder markDelivered(@PathVariable String poId) {
        return sapmService.markDelivered(poId);
    }

    @DeleteMapping("/{id}")
    public void deleteSup(@PathVariable Long id) {
        sapmService.deleteSupplier(id);
    }
}