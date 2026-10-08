package com.sliit.ayushada_server.modules.Warehouse_and_Stock_Control.Repository;

import com.sliit.ayushada_server.Entity.Stock;
import com.sliit.ayushada_server.Entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Long> {
    List<Stock> findAllByOrderByExpDateAsc();

    void deleteBySupplier(Supplier supplier);
}