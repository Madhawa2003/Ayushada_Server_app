package com.sliit.ayushada_server.modules.Supplier_and_Procurement_Management;

import com.sliit.ayushada_server.Entity.Stock;
import com.sliit.ayushada_server.modules.Warehouse_and_Stock_Control.Dto.StockAdjustRequest;
import com.sliit.ayushada_server.modules.Warehouse_and_Stock_Control.StockManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory_and_Stock_Management")
@CrossOrigin(origins = "http://localhost:4200")
public class StockManagementController {

    @Autowired
    private StockManagementService stockService;

    @GetMapping("/batches")
    public List<Stock> getAllBatches() {
        return stockService.getAllBatches();
    }

    @PostMapping("/batches")
    public Stock addBatch(@RequestBody Stock stock) {
        return stockService.addBatch(stock);
    }

    @PutMapping("/batches/{id}/reconcile")
    public Stock reconcileStock(@PathVariable Long id, @RequestBody StockAdjustRequest req) {
        return stockService.reconcileStock(id, req);
    }

    @PutMapping("/batches/{id}/write-off")
    public Stock writeOffStock(@PathVariable Long id, @RequestBody StockAdjustRequest req) {
        return stockService.writeOffStock(id, req);
    }
}