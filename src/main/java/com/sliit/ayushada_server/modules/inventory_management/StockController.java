package com.sliit.ayushada_server.modules.inventory_management;

import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
@CrossOrigin(origins = "http://localhost:4200")
public class StockController {
    private final StockService service;

    public StockController(StockService service) {
        this.service = service;
    }

    // CREATE: புதிய ரா ஹெர்ப் பேட்ச் பதிவு செய்ய
    @PostMapping("/batch")
    public Stock addStockBatch(@RequestBody Stock stock) {
        return service.saveBatch(stock);
    }

    // READ: அனைத்து ஸ்டாக்குளையும் பார்க்க
    @GetMapping("/batches")
    public List getAllBatches() {
        return service.getAllBatches();
    }

    // UPDATE: ஸ்டாக் அளவை அல்லது விவரங்களை மாற்ற
    @PutMapping("/batch/{id}")
    public Stock updateStockBatch(@PathVariable Long id, @RequestBody Stock stock) {
        return service.updateBatch(id, stock);
    }

    // DELETE: கெட்டுப்போன அல்லது வீணான பேட்ச்களை நீக்க / ரைட்-ஆஃப் செய்ய
    @DeleteMapping("/batch/{id}")
    public void deleteStockBatch(@PathVariable Long id) {
        service.deleteBatch(id);
    }

    // READ (Warning): குறைந்த ஸ்டாக் உள்ளவற்றைக் கண்டறிய
    @GetMapping("/warnings/low-stock")
    public List getLowStockWarnings(@RequestParam Integer threshold) {
        return service.getLowStockWarnings(threshold);
    }

    // READ (Warning): காலாவதியான பேட்ச்களைக் கண்டறிய
    @GetMapping("/warnings/expired")
    public List getExpiredStockWarnings(@RequestParam String date) {
        return service.getExpiredStockWarnings(LocalDate.parse(date));
    }
}