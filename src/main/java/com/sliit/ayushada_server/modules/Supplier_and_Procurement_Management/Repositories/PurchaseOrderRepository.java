package com.sliit.ayushada_server.modules.Supplier_and_Procurement_Management.Repositories;

import com.sliit.ayushada_server.Entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, String> {
    List<PurchaseOrder> findAllByOrderByOrderDateDesc();
}