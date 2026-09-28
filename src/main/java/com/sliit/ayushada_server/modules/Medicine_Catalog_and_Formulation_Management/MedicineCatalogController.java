package com.sliit.ayushada_server.modules.Medicine_Catalog_and_Formulation_Management;

import com.sliit.ayushada_server.Entity.Medicine;
import com.sliit.ayushada_server.modules.Medicine_Catalog_and_Formulation_Management.Dto.AdminMedicineViewDTO;
import com.sliit.ayushada_server.modules.Medicine_Catalog_and_Formulation_Management.Dto.MedicineSaveRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/medicine_catalog_management")
@CrossOrigin(origins = "http://localhost:4200")
public class MedicineCatalogController {

    @Autowired
    private MedicineCatalogService catalogService;

    // 1. Storefront Endpoint - accepts categoryId as a String to catch "null", empty, or valid numbers
    @GetMapping("/storefront")
    public List<Medicine> getCatalog(@RequestParam(name = "categoryId", required = false) String categoryIdStr) {
        Long categoryId = parseLongSafely(categoryIdStr);
        return catalogService.getStorefrontCatalog(categoryId);
    }

    // 2. Admin List Endpoint
    @GetMapping("/admin")
    public List<AdminMedicineViewDTO> getAdminMedicines() {
        return catalogService.getAdminMedicines();
    }

    // 3. Admin Categories List
    @GetMapping("/admin/categories")
    public List<String> getCategories() {
        return catalogService.getAllCategoryNames();
    }

    // 4. Create Medicine
    @PostMapping("/admin")
    public AdminMedicineViewDTO createMedicine(@RequestBody MedicineSaveRequest req) {
        return catalogService.createMedicine(req);
    }

    // 5. Update Medicine - safely validates ID
    @PutMapping("/admin/{id}")
    public ResponseEntity<?> updateMedicine(
            @PathVariable("id") String idStr,
            @RequestBody MedicineSaveRequest req) {
        Long id = parseLongSafely(idStr);
        if (id == null) {
            return ResponseEntity.badRequest().body("Invalid Medicine ID parameter provided.");
        }
        return ResponseEntity.ok(catalogService.updateMedicine(id, req));
    }

    // 6. Restock Endpoint - safely validates ID
    @PostMapping("/admin/{id}/restock")
    public ResponseEntity<?> restock(
            @PathVariable("id") String idStr,
            @RequestBody Map<String, Integer> body) {
        Long id = parseLongSafely(idStr);
        if (id == null) {
            return ResponseEntity.badRequest().body("Invalid Medicine ID parameter provided.");
        }
        Integer qty = body.get("quantity");
        if (qty == null || qty <= 0) {
            qty = 1;
        }
        int newTotalStock = catalogService.restockMedicine(id, qty);
        Map<String, Object> response = new HashMap<>();
        response.put("id", id);
        response.put("stock", newTotalStock);
        return ResponseEntity.ok(response);
    }

    // 7. Delete / Archive Endpoint
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<?> archiveMedicine(@PathVariable("id") String idStr) {
        Long id = parseLongSafely(idStr);
        if (id == null) {
            return ResponseEntity.badRequest().body("Invalid Medicine ID parameter provided.");
        }
        catalogService.archiveMedicine(id);
        return ResponseEntity.noContent().build();
    }

    // Helper: Safely converts String input to Long, treating null, "null", and "" as null
    private Long parseLongSafely(String val) {
        if (val == null || val.trim().isEmpty() || "null".equalsIgnoreCase(val.trim()) || "undefined".equalsIgnoreCase(val.trim())) {
            return null;
        }
        try {
            return Long.parseLong(val.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}