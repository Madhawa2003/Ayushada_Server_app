package com.sliit.ayushada_server.modules.inventory.controller;

import com.sliit.ayushada_server.Entity.Inventory;
import com.sliit.ayushada_server.modules.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(origins = "*")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping
    public List getAllBatches() {
        return inventoryService.getAllBatches();
    }

    @PostMapping
    public Inventory addBatch(@RequestBody Inventory inventory) {
        return inventoryService.saveBatch(inventory);
    }
}