package com.sliit.ayushada_server.modules.Medicine_Catalog_and_Formulation_Management;

import com.sliit.ayushada_server.Entity.Category;
import com.sliit.ayushada_server.Entity.Medicine;
import com.sliit.ayushada_server.Entity.Stock;
import com.sliit.ayushada_server.modules.Medicine_Catalog_and_Formulation_Management.Dto.MedicineSaveRequest;
import com.sliit.ayushada_server.modules.Medicine_Catalog_and_Formulation_Management.Repository.CategoryRepository;
import com.sliit.ayushada_server.modules.Medicine_Catalog_and_Formulation_Management.Repository.MedicineRepository;
import com.sliit.ayushada_server.modules.Warehouse_and_Stock_Control.Repository.StockRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MedicineCatalogServiceTest {
    @Mock
    private MedicineRepository medicineRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private MedicineCatalogService service;

    @Test
    void updateMedicineAddsStockWhenRequestedTotalIncreases() {
        Medicine medicine = medicine();
        List<Stock> stocks = new ArrayList<>(List.of(stock(medicine, 20)));
        stubUpdate(medicine, stocks);
        MedicineSaveRequest request = request(25);

        var result = service.updateMedicine(1L, request);

        ArgumentCaptor<Stock> adjustmentCaptor = ArgumentCaptor.forClass(Stock.class);
        verify(stockRepository).save(adjustmentCaptor.capture());
        assertThat(adjustmentCaptor.getValue().getQuantity()).isEqualTo(5);
        assertThat(adjustmentCaptor.getValue().getNote()).isEqualTo("Catalog stock adjustment");
        assertThat(result.getStock()).isEqualTo(25);
    }

    @Test
    void updateMedicineReducesExistingBatchesWhenRequestedTotalDecreases() {
        Medicine medicine = medicine();
        Stock expiringSooner = stock(medicine, 10);
        expiringSooner.setExpDate(LocalDate.now().plusMonths(1));
        Stock expiringLater = stock(medicine, 10);
        expiringLater.setExpDate(LocalDate.now().plusMonths(2));
        List<Stock> stocks = new ArrayList<>(List.of(expiringSooner, expiringLater));
        stubUpdate(medicine, stocks);

        var result = service.updateMedicine(1L, request(13));

        assertThat(expiringSooner.getQuantity()).isEqualTo(3);
        assertThat(expiringLater.getQuantity()).isEqualTo(10);
        assertThat(result.getStock()).isEqualTo(13);
    }

    private void stubUpdate(Medicine medicine, List<Stock> stocks) {
        when(medicineRepository.findById(1L)).thenReturn(Optional.of(medicine));
        when(categoryRepository.findAll()).thenReturn(List.of(medicine.getCategory()));
        when(medicineRepository.save(any(Medicine.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(stockRepository.findAllByOrderByExpDateAsc()).thenReturn(stocks);
        when(stockRepository.save(any(Stock.class))).thenAnswer(invocation -> {
            Stock saved = invocation.getArgument(0);
            if (!stocks.contains(saved)) {
                stocks.add(saved);
            }
            return saved;
        });
    }

    private Medicine medicine() {
        Category category = new Category();
        category.setCategoryId(1L);
        category.setName("Arishta");

        Medicine medicine = new Medicine();
        medicine.setMedicineId(1L);
        medicine.setName("Test Medicine");
        medicine.setPrice(new BigDecimal("100.00"));
        medicine.setImageUrl("🏺");
        medicine.setPrescriptionRequired(false);
        medicine.setCategory(category);
        return medicine;
    }

    private Stock stock(Medicine medicine, int quantity) {
        Stock stock = new Stock();
        stock.setQuantity(quantity);
        stock.setMedicine(medicine);
        stock.setExpDate(LocalDate.now().plusMonths(1));
        return stock;
    }

    private MedicineSaveRequest request(int initialStock) {
        MedicineSaveRequest request = new MedicineSaveRequest();
        request.setName("Test Medicine");
        request.setCategory("Arishta");
        request.setPrice(new BigDecimal("100.00"));
        request.setImageUrl("🏺");
        request.setInitialStock(initialStock);
        return request;
    }
}
