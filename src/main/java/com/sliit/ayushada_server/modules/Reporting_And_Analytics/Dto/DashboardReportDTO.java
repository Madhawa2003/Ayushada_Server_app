// DashboardReportDTO.java
package com.sliit.ayushada_server.modules.Reporting_And_Analytics.Dto;

import java.util.List;

public class DashboardReportDTO {
    private KpiMetricsDTO kpis;
    private List<CategorySalesSummaryDTO> categoryBreakdown;
    private List<ComplianceLogDTO> complianceLogs;

    public DashboardReportDTO() {}

    public DashboardReportDTO(KpiMetricsDTO kpis,
                              List<CategorySalesSummaryDTO> categoryBreakdown,
                              List<ComplianceLogDTO> complianceLogs) {
        this.kpis = kpis;
        this.categoryBreakdown = categoryBreakdown;
        this.complianceLogs = complianceLogs;
    }

    public KpiMetricsDTO getKpis() { return kpis; }
    public void setKpis(KpiMetricsDTO kpis) { this.kpis = kpis; }
    public List<CategorySalesSummaryDTO> getCategoryBreakdown() { return categoryBreakdown; }
    public void setCategoryBreakdown(List<CategorySalesSummaryDTO> categoryBreakdown) { this.categoryBreakdown = categoryBreakdown; }
    public List<ComplianceLogDTO> getComplianceLogs() { return complianceLogs; }
    public void setComplianceLogs(List<ComplianceLogDTO> complianceLogs) { this.complianceLogs = complianceLogs; }
}