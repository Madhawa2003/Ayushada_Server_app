package com.sliit.ayushada_server.modules.User_Management;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin/system")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminSystemController {

    @GetMapping("/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATIONS_MANAGER', 'INVENTORY_SUPERVISOR', 'PHARMACIST', 'FINANCE_OFFICER')")
    public ResponseEntity<Map<String, Object>> getSystemSyncStatus(Authentication authentication) {
        return ResponseEntity.ok(Map.of(
                "status", "SYNCHRONIZED",
                "timestamp", LocalDateTime.now(),
                "serverTimezone", "Asia/Colombo",
                "user", authentication.getName(),
                "authorities", authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList())
        ));
    }

    @GetMapping("/module-metrics")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERATIONS_MANAGER')")
    public ResponseEntity<Map<String, Object>> getAdministrativeMetrics() {
        return ResponseEntity.ok(Map.of(
                "activeSuppliers", 14,
                "pendingPrescriptions", 6,
                "criticalBatchesNearExpiry", 2,
                "dailyProcessedOrders", 48
        ));
    }
}