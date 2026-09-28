package com.sliit.ayushada_server.modules.inventory_management;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface StockRepository extends JpaRepository {
    List findByQuantityLessThan(Integer threshold);
    List findByExpDateBefore(LocalDate date);
}