package com.sliit.ayushada_server.modules.inventory_management;

import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class StockService {
    private final StockRepository repository;

    public StockService(StockRepository repository) {
        this.repository = repository;
    }

    public Stock saveBatch(Stock stock) {
        return repository.save(stock);
    }

    public List getAllBatches() {
        return repository.findAll();
    }

    public Stock updateBatch(Long id, Stock updatedStock) {
        Optional optionalStock = repository.findById(id);
        if (optionalStock.isPresent()) {
            Stock stock = optionalStock.get();
            stock.setBatchNumber(updatedStock.getBatchNumber());
            stock.setQuantity(updatedStock.getQuantity());
            stock.setMfgDate(updatedStock.getMfgDate());
            stock.setExpDate(updatedStock.getExpDate());
            stock.setMedicineId(updatedStock.getMedicineId());
            return repository.save(stock);
        }
        throw new RuntimeException("Stock batch not found with id: " + id);
    }

    public void deleteBatch(Long id) {
        repository.deleteById(id);
    }

    public List getLowStockWarnings(Integer threshold) {
        return repository.findByQuantityLessThan(threshold);
    }

    public List getExpiredStockWarnings(LocalDate date) {
        return repository.findByExpDateBefore(date);
    }
}