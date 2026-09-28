package com.sliit.ayushada_server.modules.Medicine_Catalog_and_Formulation_Management;

import com.sliit.ayushada_server.Entity.Category;
import com.sliit.ayushada_server.Entity.Medicine;
import com.sliit.ayushada_server.Entity.Stock;
import com.sliit.ayushada_server.modules.Medicine_Catalog_and_Formulation_Management.Dto.AdminMedicineViewDTO;
import com.sliit.ayushada_server.modules.Medicine_Catalog_and_Formulation_Management.Dto.MedicineSaveRequest;
import com.sliit.ayushada_server.modules.Medicine_Catalog_and_Formulation_Management.Exeption.InvalidMedicineException;
import com.sliit.ayushada_server.modules.Medicine_Catalog_and_Formulation_Management.Repository.CategoryRepository;
import com.sliit.ayushada_server.modules.Medicine_Catalog_and_Formulation_Management.Repository.MedicineRepository;
import com.sliit.ayushada_server.modules.Warehouse_and_Stock_Control.Repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class MedicineCatalogService {

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private StockRepository stockRepository;


    public List<Medicine> getStorefrontCatalog(Long categoryId) {
        if (categoryId != null && categoryId > 0) {
            return medicineRepository.findByCategoryCategoryIdAndIsArchivedFalse(categoryId);
        }
        return medicineRepository.findByIsArchivedFalse();
    }

    // 1. Get all active medicines with calculated stock counts
    public List<AdminMedicineViewDTO> getAdminMedicines() {
        List<Medicine> medicines = medicineRepository.findByIsArchivedFalse();
        List<Stock> allStocks = stockRepository.findAll();
        List<AdminMedicineViewDTO> list = new ArrayList<>();

        for (Medicine m : medicines) {
            int totalStock = 0;
            for (Stock s : allStocks) {
                if (s.getMedicine() != null && s.getMedicine().getMedicineId().equals(m.getMedicineId())) {
                    if (s.getQuantity() != null) {
                        totalStock += s.getQuantity();
                    }
                }
            }

            String catName = (m.getCategory() != null) ? m.getCategory().getName() : "General";
            String img = (m.getImageUrl() != null && !m.getImageUrl().isEmpty()) ? m.getImageUrl() : "🏺";
            boolean rx = Boolean.TRUE.equals(m.getPrescriptionRequired());

            list.add(new AdminMedicineViewDTO(m.getMedicineId(), m.getName(), catName, m.getPrice(), img, rx, totalStock));
        }
        return list;
    }

    // 2. Fetch all category names
    public List<String> getAllCategoryNames() {
        List<Category> categories = categoryRepository.findAll();
        List<String> names = new ArrayList<>();
        for (Category c : categories) {
            names.add(c.getName());
        }
        return names;
    }

    // 3. Add Medicine with initial stock
    public AdminMedicineViewDTO createMedicine(MedicineSaveRequest req) {
        Category category = findOrCreateCategory(req.getCategory());

        Medicine med = new Medicine();
        med.setName(req.getName().trim());
        med.setPrice(req.getPrice());
        med.setType(req.getCategory());
        med.setIntake("As directed by physician");
        med.setInstructions("Store in a cool dry place");
        med.setDescription(req.getName());
        med.setImageUrl((req.getImageUrl() != null) ? req.getImageUrl() : "🏺");
        med.setPrescriptionRequired(req.isPrescriptionRequired());
        med.setIsArchived(false);
        med.setCategory(category);

        Medicine saved = medicineRepository.save(med);

        if (req.getInitialStock() > 0) {
            Stock batch = new Stock();
            batch.setBatchNumber("BATCH-" + System.currentTimeMillis());
            batch.setQuantity(req.getInitialStock());
            batch.setMfgDate(LocalDate.now());
            batch.setExpDate(LocalDate.now().plusYears(1));
            batch.setWarehouseLocation("Bay A - Shelf 01");
            batch.setNote("Initial batch");
            batch.setMedicine(saved);
            stockRepository.save(batch);
        }

        return new AdminMedicineViewDTO(saved.getMedicineId(), saved.getName(), category.getName(), saved.getPrice(), saved.getImageUrl(), saved.getPrescriptionRequired(), req.getInitialStock());
    }

    // 4. Update existing medicine
    public AdminMedicineViewDTO updateMedicine(Long id, MedicineSaveRequest req) {
        Medicine med = medicineRepository.findById(id)
                .orElseThrow(() -> new InvalidMedicineException("Medicine not found with ID: " + id));

        Category category = findOrCreateCategory(req.getCategory());

        med.setName(req.getName().trim());
        med.setPrice(req.getPrice());
        med.setImageUrl((req.getImageUrl() != null) ? req.getImageUrl() : "🏺");
        med.setPrescriptionRequired(req.isPrescriptionRequired());
        med.setCategory(category);

        Medicine saved = medicineRepository.save(med);

        int stock = calculateStock(saved.getMedicineId());
        return new AdminMedicineViewDTO(saved.getMedicineId(), saved.getName(), category.getName(), saved.getPrice(), saved.getImageUrl(), saved.getPrescriptionRequired(), stock);
    }

    // 5. Restock quick batch
    public int restockMedicine(Long id, int quantity) {
        Medicine med = medicineRepository.findById(id)
                .orElseThrow(() -> new InvalidMedicineException("Medicine not found with ID: " + id));

        Stock batch = new Stock();
        batch.setBatchNumber("BATCH-" + System.currentTimeMillis());
        batch.setQuantity(quantity);
        batch.setMfgDate(LocalDate.now());
        batch.setExpDate(LocalDate.now().plusMonths(18));
        batch.setWarehouseLocation("Bay A - Shelf 02");
        batch.setNote("Restock batch");
        batch.setMedicine(med);
        stockRepository.save(batch);

        return calculateStock(id);
    }

    // 6. Soft-delete archive
    public void archiveMedicine(Long id) {
        Medicine med = medicineRepository.findById(id)
                .orElseThrow(() -> new InvalidMedicineException("Medicine not found with ID: " + id));
        med.setIsArchived(true);
        medicineRepository.save(med);
    }

    // Helper: Find or create category using simple loops
    private Category findOrCreateCategory(String categoryName) {
        List<Category> allCategories = categoryRepository.findAll();
        for (Category c : allCategories) {
            if (c.getName().equalsIgnoreCase(categoryName)) {
                return c;
            }
        }
        Category newCat = new Category();
        newCat.setName(categoryName);
        newCat.setDescription(categoryName + " category");
        return categoryRepository.save(newCat);
    }

    // Helper: Sum current stock
    private int calculateStock(Long medicineId) {
        List<Stock> stocks = stockRepository.findAll();
        int sum = 0;
        for (Stock s : stocks) {
            if (s.getMedicine() != null && s.getMedicine().getMedicineId().equals(medicineId)) {
                if (s.getQuantity() != null) {
                    sum += s.getQuantity();
                }
            }
        }
        return sum;
    }


}