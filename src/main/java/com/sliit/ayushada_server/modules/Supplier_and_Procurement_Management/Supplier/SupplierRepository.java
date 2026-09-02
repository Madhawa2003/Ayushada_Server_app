package com.sliit.ayushada_server.modules.Supplier_and_Procurement_Management.Supplier;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Integer> {
    boolean existsByEmail(String email);
}
