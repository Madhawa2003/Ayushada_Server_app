package com.sliit.ayushada_server.modules.Warehouse_and_Stock_Control;

import com.sliit.ayushada_server.modules.Warehouse_and_Stock_Control.Dto.StockAdjustRequest;
import com.sliit.ayushada_server.modules.Warehouse_and_Stock_Control.Dto.StockBatchDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inventory_and_Stock_Management")
@CrossOrigin(origins = "http://localhost:4200")
public class StockManagementController {

    @Autowired
    private StockManagementService stockService;


    @GetMapping("/batches")
    public List<StockBatchDTO> getAllBatches() {
        return stockService.getAllBatches();
    }

    @PostMapping("/batches")
    public StockBatchDTO addBatch(@RequestBody Map<String, Object> body) {
        Long medicineId = Long.valueOf(body.get("medicineId").toString());
        String batchNumber = body.get("batchNumber").toString();
        int quantity = Integer.parseInt(body.get("quantity").toString());
        String expDate = body.get("expiryDate").toString();
        String location = body.get("warehouseLocation").toString();

        return stockService.addBatch(medicineId, batchNumber, quantity, expDate, location);
    }


    @PutMapping("/batches/{id}/reconcile")
    public StockBatchDTO reconcileStock(@PathVariable Long id, @RequestBody StockAdjustRequest req) {
        return stockService.reconcileStock(id, req);
    }

    @DeleteMapping("/{id}")
    public void deleteStock(@PathVariable Long id) {
        stockService.deleteStock(id);

    }

    @PutMapping("/batches/{id}/write-off")
    public StockBatchDTO writeOffStock(@PathVariable Long id, @RequestBody StockAdjustRequest req) {
        return stockService.writeOffStock(id, req);
    }
}