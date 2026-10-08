// CategorySalesSummaryDTO.java
package com.sliit.ayushada_server.modules.Reporting_And_Analytics.Dto;

import java.math.BigDecimal;

public class CategorySalesSummaryDTO {
    private String category;
    private BigDecimal revenue;
    private Long unitsSold;
    private Double percentage;

    public CategorySalesSummaryDTO(String category, BigDecimal revenue, Long unitsSold, Double percentage) {
        this.category = category;
        this.revenue = revenue;
        this.unitsSold = unitsSold;
        this.percentage = percentage;
    }

    public String getCategory() { return category; }
    public BigDecimal getRevenue() { return revenue; }
    public Long getUnitsSold() { return unitsSold; }
    public Double getPercentage() { return percentage; }
    public void setPercentage(Double percentage) { this.percentage = percentage; }
}