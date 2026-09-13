package com.sliit.ayushada_server.modules.Supplier_and_Procurement_Management.Repositories;

import com.sliit.ayushada_server.Entity.Product;
import com.sliit.ayushada_server.Entity.SupplierLogs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierLogsRepository extends JpaRepository<SupplierLogs, Integer> {
}
