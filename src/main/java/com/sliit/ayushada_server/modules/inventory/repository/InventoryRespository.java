package com.sliit.ayushada_server.modules.inventory.repository;

import com.sliit.ayushada_server.Entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository {
    
}