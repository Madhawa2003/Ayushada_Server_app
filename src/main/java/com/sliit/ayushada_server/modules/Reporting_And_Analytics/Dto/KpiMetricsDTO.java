// KpiMetricsDTO.java
package com.sliit.ayushada_server.modules.Reporting_And_Analytics.Dto;

import java.math.BigDecimal;

public class KpiMetricsDTO {
    private BigDecimal grossRevenue;
    private Long totalOrders;
    private Long verifiedPrescriptions;
    private Long activeFormulations;
    private BigDecimal averageOrderValue;
    private Double revenueGrowthPercent;

    public KpiMetricsDTO(BigDecimal grossRevenue, Long totalOrders, Long verifiedPrescriptions,
                         Long activeFormulations, BigDecimal averageOrderValue, Double revenueGrowthPercent) {
        this.grossRevenue = grossRevenue;
        this.totalOrders = totalOrders;
        this.verifiedPrescriptions = verifiedPrescriptions;
        this.activeFormulations = activeFormulations;
        this.averageOrderValue = averageOrderValue;
        this.revenueGrowthPercent = revenueGrowthPercent;
    }

    public BigDecimal getGrossRevenue() { return grossRevenue; }
    public Long getTotalOrders() { return totalOrders; }
    public Long getVerifiedPrescriptions() { return verifiedPrescriptions; }
    public Long getActiveFormulations() { return activeFormulations; }
    public BigDecimal getAverageOrderValue() { return averageOrderValue; }
    public Double getRevenueGrowthPercent() { return revenueGrowthPercent; }
}