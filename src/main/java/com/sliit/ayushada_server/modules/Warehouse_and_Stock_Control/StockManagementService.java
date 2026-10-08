package com.sliit.ayushada_server.modules.Warehouse_and_Stock_Control;

import com.sliit.ayushada_server.Entity.Medicine;
import com.sliit.ayushada_server.Entity.Stock;
import com.sliit.ayushada_server.modules.Medicine_Catalog_and_Formulation_Management.Repository.MedicineRepository;
import com.sliit.ayushada_server.modules.Warehouse_and_Stock_Control.Dto.StockAdjustRequest;
import com.sliit.ayushada_server.modules.Warehouse_and_Stock_Control.Dto.StockBatchDTO;
import com.sliit.ayushada_server.modules.Warehouse_and_Stock_Control.Exeption.InvalidStockException;
import com.sliit.ayushada_server.modules.Warehouse_and_Stock_Control.Repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class StockManagementService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private MedicineRepository medicineRepository;

    // 1. Get all batches mapped to frontend structure with status
    public List<StockBatchDTO> getAllBatches() {
        List<Stock> stocks = stockRepository.findAllByOrderByExpDateAsc();
        List<StockBatchDTO> dtoList = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (Stock s : stocks) {
            String medName = "Unknown";
            String catName = "General";
            Long medId = 0L;

            if (s.getMedicine() != null) {
                medName = s.getMedicine().getName();
                medId = s.getMedicine().getMedicineId();
                if (s.getMedicine().getCategory() != null) {
                    catName = s.getMedicine().getCategory().getName();
                } else if (s.getMedicine().getType() != null) {
                    catName = s.getMedicine().getType();
                }
            }

            // Determine status
            String status = "HEALTHY";
            if (s.getNote() != null && s.getNote().startsWith("WRITTEN_OFF")) {
                status = "WRITTEN_OFF";
            } else if (s.getExpDate() != null) {
                long daysToExpiry = ChronoUnit.DAYS.between(today, s.getExpDate());
                if (daysToExpiry <= 0) {
                    status = "EXPIRED";
                } else if (daysToExpiry <= 45) {
                    status = "NEAR_EXPIRY";
                } else if (s.getQuantity() != null && s.getQuantity() <= 10) {
                    status = "LOW_STOCK";
                }
            }

            String recDate = (s.getMfgDate() != null) ? s.getMfgDate().toString() : today.toString();
            String expDate = (s.getExpDate() != null) ? s.getExpDate().toString() : today.plusYears(1).toString();
            String location = (s.getWarehouseLocation() != null) ? s.getWarehouseLocation() : "Bay A - Shelf 01";
            int qty = (s.getQuantity() != null) ? s.getQuantity() : 0;

            dtoList.add(new StockBatchDTO(
                    s.getStockId(),
                    s.getBatchNumber(),
                    medId,
                    medName,
                    catName,
                    qty,
                    recDate,
                    expDate,
                    location,
                    status
            ));
        }
        return dtoList;
    }

    // 2. Add New Batch
    public StockBatchDTO addBatch(Long medicineId, String batchNumber, int quantity, String expDateStr, String location) {
        Medicine med = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new InvalidStockException("Medicine not found with ID: " + medicineId));

        LocalDate expDate = LocalDate.parse(expDateStr);

        Stock stock = new Stock();
        stock.setBatchNumber(batchNumber);
        stock.setQuantity(quantity);
        stock.setMfgDate(LocalDate.now());
        stock.setExpDate(expDate);
        stock.setWarehouseLocation(location);
        stock.setNote("Manual intake batch");
        stock.setMedicine(med);

        Stock saved = stockRepository.save(stock);

        String catName = (med.getCategory() != null) ? med.getCategory().getName() : "General";

        return new StockBatchDTO(
                saved.getStockId(),
                saved.getBatchNumber(),
                med.getMedicineId(),
                med.getName(),
                catName,
                saved.getQuantity(),
                saved.getMfgDate().toString(),
                saved.getExpDate().toString(),
                saved.getWarehouseLocation(),
                "HEALTHY"
        );
    }

    // 3. Reconcile Stock Count
    public StockBatchDTO reconcileStock(Long id, StockAdjustRequest req) {
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new InvalidStockException("Stock batch not found with ID: " + id));

        stock.setQuantity(req.getQuantity());
        stock.setNote("RECONCILED: " + req.getReason());
        Stock saved = stockRepository.save(stock);

        return convertSingleStock(saved);
    }

    // 4. Write-off Batch
    public StockBatchDTO writeOffStock(Long id, StockAdjustRequest req) {
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new InvalidStockException("Stock batch not found with ID: " + id));

        stock.setQuantity(0);
        stock.setNote("WRITTEN_OFF: " + req.getReason());
        Stock saved = stockRepository.save(stock);

        StockBatchDTO dto = convertSingleStock(saved);
        dto.setStatus("WRITTEN_OFF");
        return dto;
    }

    private StockBatchDTO convertSingleStock(Stock s) {
        String medName = (s.getMedicine() != null) ? s.getMedicine().getName() : "Unknown";
        String catName = (s.getMedicine() != null && s.getMedicine().getCategory() != null)
                ? s.getMedicine().getCategory().getName()
                : "General";
        Long medId = (s.getMedicine() != null) ? s.getMedicine().getMedicineId() : 0L;

        return new StockBatchDTO(
                s.getStockId(),
                s.getBatchNumber(),
                medId,
                medName,
                catName,
                s.getQuantity(),
                s.getMfgDate().toString(),
                s.getExpDate().toString(),
                s.getWarehouseLocation(),
                "HEALTHY"
        );
    }

    public void deleteStock(Long id) {
        stockRepository.deleteById(id);
    }
}