package com.sliit.ayushada_server.modules.Supplier_and_Procurement_Management;

import com.sliit.ayushada_server.Entity.PurchaseOrder;
import com.sliit.ayushada_server.Entity.PurchaseOrderItem;
import com.sliit.ayushada_server.Entity.Supplier;
import com.sliit.ayushada_server.modules.Supplier_and_Procurement_Management.Dto.PurchaseOrderCreateRequest;
import com.sliit.ayushada_server.modules.Supplier_and_Procurement_Management.Exeption.InvalidSupplierException;
import com.sliit.ayushada_server.modules.Supplier_and_Procurement_Management.Repositories.PurchaseOrderRepository;
import com.sliit.ayushada_server.modules.Supplier_and_Procurement_Management.Repositories.SupplierRepository;
import com.sliit.ayushada_server.modules.Warehouse_and_Stock_Control.Repository.StockRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SapmService {

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    public PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    public StockRepository stockRepository;

    @Autowired
    private PurchaseOrderRepository orderRepository;

    // --- Supplier Operations ---

    public Supplier addSupplier(Supplier supplier) {
        supplier.setActiveStatus(true);
        if (supplier.getRating() == null) {
            supplier.setRating(new BigDecimal("5.0"));
        }
        return supplierRepository.save(supplier);
    }

    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    public Supplier updateSupplier(Long id, Supplier supplier) {
        Supplier existing = supplierRepository.findById(id)
                .orElseThrow(() -> new InvalidSupplierException("Supplier not found with ID: " + id));

        existing.setCompanyName(supplier.getCompanyName());
        existing.setPersonName(supplier.getPersonName());
        existing.setEmail(supplier.getEmail());
        existing.setPhoneNo(supplier.getPhoneNo());
        existing.setAddress(supplier.getAddress());
        existing.setSuppliedHerbs(supplier.getSuppliedHerbs());
        if (supplier.getActiveStatus() != null) {
            existing.setActiveStatus(supplier.getActiveStatus());
        }

        return supplierRepository.save(existing);
    }

    public Supplier toggleSupplierStatus(Long id) {
        Supplier existing = supplierRepository.findById(id)
                .orElseThrow(() -> new InvalidSupplierException("Supplier not found with ID: " + id));
        existing.setActiveStatus(!existing.getActiveStatus());
        return supplierRepository.save(existing);
    }

    // --- Purchase Order Operations ---

    public List<PurchaseOrder> getAllOrders() {
        return orderRepository.findAllByOrderByOrderDateDesc();
    }

    public PurchaseOrder issuePurchaseOrder(PurchaseOrderCreateRequest req) {
        Supplier supplier = supplierRepository.findById(req.getSupplierId())
                .orElseThrow(() -> new InvalidSupplierException("Supplier not found with ID: " + req.getSupplierId()));

        PurchaseOrder po = new PurchaseOrder();
        po.setPoId((req.getPoId() != null && !req.getPoId().isEmpty())
                ? req.getPoId()
                : "PO-2026-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase());

        po.setSupplier(supplier);
        po.setOrderDate(LocalDate.now());
        po.setExpectedDate(LocalDate.parse(req.getExpectedDate()));
        po.setStatus("Sent");
        po.setNote(req.getNote());
        po.setTotalAmount(req.getTotalAmount() != null ? req.getTotalAmount() : BigDecimal.ZERO);

        List<PurchaseOrderItem> orderItems = new ArrayList<>();
        if (req.getItems() != null) {
            for (PurchaseOrderCreateRequest.ItemRequest it : req.getItems()) {
                PurchaseOrderItem item = new PurchaseOrderItem();
                item.setItemName(it.getMedicineOrHerbName());
                item.setQuantityOrdered(it.getQuantityOrdered());
                item.setUnit(it.getUnit() != null ? it.getUnit() : "kg");
                item.setUnitCost(it.getUnitCost());
                item.setPurchaseOrder(po);
                orderItems.add(item);
            }
        }
        po.setItems(orderItems);

        return orderRepository.save(po);
    }

    public PurchaseOrder markDelivered(String poId) {
        PurchaseOrder po = orderRepository.findById(poId)
                .orElseThrow(() -> new InvalidSupplierException("Purchase Order not found: " + poId));
        po.setStatus("Delivered");
        return orderRepository.save(po);
    }

    @Transactional
    public void deleteSupplier(Long id) {
        Optional<Supplier>sup = supplierRepository.findById(id);
        purchaseOrderRepository.deleteBySupplier(sup.get());
        stockRepository.deleteBySupplier(sup.get());
        supplierRepository.deleteById(id);
    }
}