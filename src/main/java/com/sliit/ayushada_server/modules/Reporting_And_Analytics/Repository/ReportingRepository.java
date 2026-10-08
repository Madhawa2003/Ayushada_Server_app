package com.sliit.ayushada_server.modules.Reporting_And_Analytics.Repository;

import com.sliit.ayushada_server.Entity.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReportingRepository extends JpaRepository<CustomerOrder, Long> {

    // Count orders after threshold
    @Query("SELECT COUNT(o) FROM CustomerOrder o WHERE (:startDate IS NULL OR o.orderDate >= :startDate)")
    Long countOrdersAfter(@Param("startDate") LocalDateTime startDate);

    // Calculate gross revenue via CustomerOrder -> items (using OrderItem entity attributes)
    @Query("SELECT COALESCE(SUM(oi.quantity * oi.unitPrice), 0.0) " +
            "FROM CustomerOrder o " +
            "JOIN o.items oi " +
            "WHERE (:startDate IS NULL OR o.orderDate >= :startDate)")
    Double calculateGrossRevenueAfter(@Param("startDate") LocalDateTime startDate);

    // Verified prescriptions count
    @Query("SELECT COUNT(p) FROM Prescription p WHERE UPPER(p.status) = 'VERIFIED' OR UPPER(p.status) = 'APPROVED'")
    Long countVerifiedPrescriptions();

    // Active medicine formulations count
    @Query("SELECT COUNT(m) FROM Medicine m")
    Long countActiveFormulations();

    // Group sales by category using entity relations: CustomerOrder -> items -> medicine -> category
    @Query("SELECT c.name, COALESCE(SUM(oi.quantity * oi.unitPrice), 0.0), COALESCE(SUM(oi.quantity), 0L) " +
            "FROM CustomerOrder o " +
            "JOIN o.items oi " +
            "JOIN oi.medicine m " +
            "JOIN m.category c " +
            "WHERE (:startDate IS NULL OR o.orderDate >= :startDate) " +
            "GROUP BY c.name")
    List<Object[]> findSalesGroupedByCategory(@Param("startDate") LocalDateTime startDate);

    // Prescription audit log records using entity relations
    @Query("SELECT p.prescriptionId, p.prescriptionNumber, m.name, c.name, p.status " +
            "FROM Prescription p " +
            "LEFT JOIN p.items pi " +
            "LEFT JOIN pi.medicine m " +
            "LEFT JOIN m.category c " +
            "ORDER BY p.prescriptionId DESC")
    List<Object[]> findComplianceLogRecords();
}