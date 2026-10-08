package com.sliit.ayushada_server.modules.Supplier_and_Procurement_Management.Repositories;

import com.sliit.ayushada_server.Entity.PurchaseOrder;
import com.sliit.ayushada_server.Entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, String> {
    List<PurchaseOrder> findAllByOrderByOrderDateDesc();

    void deleteBySupplier(Supplier supplier);
}