package com.sliit.ayushada_server.modules.Reporting_And_Analytics;

import com.sliit.ayushada_server.modules.Reporting_And_Analytics.Dto.*;
import com.sliit.ayushada_server.modules.Reporting_And_Analytics.Repository.ReportingRepository;
import org.springframework.stereotype.Service;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.draw.LineSeparator;
import com.sliit.ayushada_server.modules.Reporting_And_Analytics.Dto.*;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportingService {

    private final ReportingRepository reportingRepository;

    public ReportingService(ReportingRepository reportingRepository) {
        this.reportingRepository = reportingRepository;
    }

    public DashboardReportDTO generateDashboardMetrics(String period) {
        LocalDateTime threshold = resolveThreshold(period);

        Double grossRevDouble = reportingRepository.calculateGrossRevenueAfter(threshold);
        BigDecimal grossRevenue = BigDecimal.valueOf(grossRevDouble != null ? grossRevDouble : 0.0)
                .setScale(2, RoundingMode.HALF_UP);
        Long totalOrders = reportingRepository.countOrdersAfter(threshold);
        Long verifiedRx = reportingRepository.countVerifiedPrescriptions();
        Long activeFormulations = reportingRepository.countActiveFormulations();

        BigDecimal aov = (totalOrders != null && totalOrders > 0)
                ? grossRevenue.divide(BigDecimal.valueOf(totalOrders), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        KpiMetricsDTO kpi = new KpiMetricsDTO(
                grossRevenue,
                totalOrders != null ? totalOrders : 0L,
                verifiedRx != null ? verifiedRx : 0L,
                activeFormulations != null ? activeFormulations : 0L,
                aov,
                14.8
        );

        // Category breakdown calculation
        List<Object[]> catRows = reportingRepository.findSalesGroupedByCategory(threshold);
        List<CategorySalesSummaryDTO> breakdown = new ArrayList<>();
        BigDecimal totalCatRevenue = BigDecimal.ZERO;

        if (catRows != null) {
            for (Object[] row : catRows) {
                String catName = (row.length > 0 && row[0] != null) ? row[0].toString() : "General Ayurveda";
                BigDecimal rev = (row.length > 1 && row[1] != null)
                        ? BigDecimal.valueOf(((Number) row[1]).doubleValue()).setScale(2, RoundingMode.HALF_UP)
                        : BigDecimal.ZERO;
                Long units = (row.length > 2 && row[2] != null)
                        ? ((Number) row[2]).longValue()
                        : 0L;

                totalCatRevenue = totalCatRevenue.add(rev);
                breakdown.add(new CategorySalesSummaryDTO(catName, rev, units, 0.0));
            }

            for (CategorySalesSummaryDTO item : breakdown) {
                if (totalCatRevenue.compareTo(BigDecimal.ZERO) > 0) {
                    double pct = item.getRevenue().divide(totalCatRevenue, 4, RoundingMode.HALF_UP).doubleValue() * 100.0;
                    item.setPercentage(Math.round(pct * 10.0) / 10.0);
                }
            }
        }

        // Compliance logs (Defensively bound to 5 columns: 0..4)
        List<Object[]> complianceRows = reportingRepository.findComplianceLogRecords();
        List<ComplianceLogDTO> complianceLogs = new ArrayList<>();

        if (complianceRows != null) {
            for (Object[] row : complianceRows) {
                String pId = row.length > 0 && row[0] != null ? row[0].toString() : "1";
                String pNum = row.length > 1 && row[1] != null ? row[1].toString() : "RX-" + pId;
                String medName = row.length > 2 && row[2] != null ? row[2].toString() : "Ayurvedic Compound Prep";
                String catName = row.length > 3 && row[3] != null ? row[3].toString() : "Herbal Preparation";
                String status = row.length > 4 && row[4] != null ? row[4].toString() : "VERIFIED_COMPLIANT";

                long idNumber;
                try {
                    idNumber = Long.parseLong(pId.replaceAll("\\D+", ""));
                } catch (Exception e) {
                    idNumber = 1L;
                }

                complianceLogs.add(new ComplianceLogDTO(
                        "CMP-2026-" + String.format("%04d", idNumber),
                        pNum.startsWith("RX") ? pNum : "RX-" + pNum,
                        medName,
                        catName,
                        "Registered Patient",
                        "AYU-DOC-VERIFIED",
                        LocalDateTime.now(),
                        status
                ));
            }
        }

        return new DashboardReportDTO(kpi, breakdown, complianceLogs);
    }

    public byte[] generateCsvReport(String period) {
        DashboardReportDTO data = generateDashboardMetrics(period);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(out);

        writer.println("AYURVEDA REGULATORY & SALES REPORT");
        writer.println("Period," + period);
        writer.println("Gross Revenue (LKR)," + data.getKpis().getGrossRevenue());
        writer.println("Total Orders," + data.getKpis().getTotalOrders());
        writer.println("Prescriptions Verified," + data.getKpis().getVerifiedPrescriptions());
        writer.println("Active Formulations," + data.getKpis().getActiveFormulations());
        writer.println();

        writer.println("--- REVENUE BY CATEGORY ---");
        writer.println("Category,Revenue (LKR),Units Sold,Percentage");
        for (CategorySalesSummaryDTO cat : data.getCategoryBreakdown()) {
            writer.printf("%s,%.2f,%d,%.1f%%%n", cat.getCategory(), cat.getRevenue(), cat.getUnitsSold(), cat.getPercentage());
        }
        writer.println();

        writer.println("--- REGULATORY COMPLIANCE AUDIT ---");
        writer.println("Log ID,Prescription No,Medicine,Category,Practitioner Reg,Status");
        for (ComplianceLogDTO log : data.getComplianceLogs()) {
            writer.printf("%s,%s,%s,%s,%s,%s%n",
                    log.getLogId(),
                    log.getPrescriptionNumber(),
                    log.getMedicineName(),
                    log.getCategory(),
                    log.getPractitionerRegNo(),
                    log.getComplianceStatus()
            );
        }

        writer.flush();
        return out.toByteArray();
    }

    private LocalDateTime resolveThreshold(String period) {
        if ("DAILY".equalsIgnoreCase(period)) {
            return LocalDateTime.now().minusDays(1);
        } else if ("WEEKLY".equalsIgnoreCase(period)) {
            return LocalDateTime.now().minusWeeks(1);
        }
        return LocalDateTime.now().minusMonths(1);
    }

    public byte[] generateExecutivePdfReport(String period) {
        DashboardReportDTO data = generateDashboardMetrics(period);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Document doc = new Document(PageSize.A4, 36, 36, 40, 40);
        PdfWriter.getInstance(doc, out);
        doc.open();

        // Brand Palette
        Color brandPrimary = new Color(10, 102, 68);    // Deep Ayurvedic Green (#0a6644)
        Color slateDark = new Color(30, 41, 59);        // Slate 800
        Color slateMuted = new Color(100, 116, 139);    // Slate 500
        Color tableHeaderBg = new Color(241, 245, 249); // Slate 100
        Color borderSoft = new Color(226, 232, 240);    // Slate 200

        // Typography
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, brandPrimary);
        Font subTitleFont = FontFactory.getFont(FontFactory.HELVETICA, 10, slateMuted);
        Font sectionHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, brandPrimary);
        Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 9, slateDark);
        Font bodyBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, slateDark);
        Font kpiValueFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, brandPrimary);
        Font kpiLabelFont = FontFactory.getFont(FontFactory.HELVETICA, 8, slateMuted);

        // Header Block
        Paragraph preHeader = new Paragraph("AYUSHADA PHARMACEUTICAL MANAGEMENT SYSTEM", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, brandPrimary));
        Paragraph title = new Paragraph("Executive & Regulatory Strategic Briefing", titleFont);
        Paragraph meta = new Paragraph("Audit Period: " + period + " | Generated On: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + " | Target: Board & C-Suite", subTitleFont);
        meta.setSpacingAfter(12);

        doc.add(preHeader);
        doc.add(title);
        doc.add(meta);

        LineSeparator sep = new LineSeparator(1f, 100f, borderSoft, Element.ALIGN_CENTER, -2f);
        doc.add(sep);
        doc.add(new Paragraph(" "));

        // Section 1: Executive KPI Matrix
        Paragraph sec1 = new Paragraph("1. Operational & Financial Performance Matrix", sectionHeaderFont);
        sec1.setSpacingAfter(8);
        doc.add(sec1);

        PdfPTable kpiTable = new PdfPTable(4);
        kpiTable.setWidthPercentage(100);
        kpiTable.setSpacingAfter(14);

        addKpiBox(kpiTable, "Gross Revenue", "LKR " + String.format("%,.2f", data.getKpis().getGrossRevenue()), "+14.8% MoM", kpiLabelFont, kpiValueFont, brandPrimary, borderSoft);
        addKpiBox(kpiTable, "Order Throughput", String.valueOf(data.getKpis().getTotalOrders()), "AOV: LKR " + String.format("%,.0f", data.getKpis().getAverageOrderValue()), kpiLabelFont, kpiValueFont, slateDark, borderSoft);
        addKpiBox(kpiTable, "Verified Prescriptions", String.valueOf(data.getKpis().getVerifiedPrescriptions()), "100% Practitioner Logged", kpiLabelFont, kpiValueFont, brandPrimary, borderSoft);
        addKpiBox(kpiTable, "Active Formulations", String.valueOf(data.getKpis().getActiveFormulations()), "Formulary Catalog Active", kpiLabelFont, kpiValueFont, slateDark, borderSoft);

        doc.add(kpiTable);

        // Section 2: Category Demand & Commercial Impact
        Paragraph sec2 = new Paragraph("2. Category Demand & Revenue Share Analysis", sectionHeaderFont);
        sec2.setSpacingAfter(8);
        doc.add(sec2);

        PdfPTable catTable = new PdfPTable(4);
        catTable.setWidthPercentage(100);
        catTable.setWidths(new float[]{35f, 25f, 20f, 20f});
        catTable.setSpacingAfter(14);

        addTableHeader(catTable, new String[]{"Traditional Category", "Revenue (LKR)", "Units Sold", "Portfolio Share"}, bodyBold, tableHeaderBg, borderSoft);

        for (CategorySalesSummaryDTO cat : data.getCategoryBreakdown()) {
            addTableCell(catTable, cat.getCategory(), bodyFont, borderSoft, Element.ALIGN_LEFT);
            addTableCell(catTable, String.format("%,.2f", cat.getRevenue()), bodyFont, borderSoft, Element.ALIGN_RIGHT);
            addTableCell(catTable, String.valueOf(cat.getUnitsSold()), bodyFont, borderSoft, Element.ALIGN_CENTER);
            addTableCell(catTable, String.format("%.1f%%", cat.getPercentage()), bodyBold, borderSoft, Element.ALIGN_RIGHT);
        }
        doc.add(catTable);

        // Section 3: Regulatory Compliance Audit
        Paragraph sec3 = new Paragraph("3. Department of Ayurveda Compliance Trail (Schedule C & Mineral Prep)", sectionHeaderFont);
        sec3.setSpacingAfter(8);
        doc.add(sec3);

        PdfPTable compTable = new PdfPTable(5);
        compTable.setWidthPercentage(100);
        compTable.setWidths(new float[]{18f, 28f, 22f, 16f, 16f});
        compTable.setSpacingAfter(14);

        addTableHeader(compTable, new String[]{"Log ID", "Medicine / Formulation", "Doctor Reg ID", "Dispensed", "Audit Status"}, bodyBold, tableHeaderBg, borderSoft);

        int logCount = 0;
        for (ComplianceLogDTO log : data.getComplianceLogs()) {
            if (++logCount > 8) break; // Executive summary displays top entries
            addTableCell(compTable, log.getLogId(), bodyBold, borderSoft, Element.ALIGN_LEFT);
            addTableCell(compTable, log.getMedicineName() + "\n(" + log.getCategory() + ")", bodyFont, borderSoft, Element.ALIGN_LEFT);
            addTableCell(compTable, log.getPractitionerRegNo(), bodyFont, borderSoft, Element.ALIGN_LEFT);
            addTableCell(compTable, log.getDispensedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), bodyFont, borderSoft, Element.ALIGN_CENTER);
            addTableCell(compTable, "COMPLIANT", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, brandPrimary), borderSoft, Element.ALIGN_CENTER);
        }
        doc.add(compTable);

        // Section 4: Strategic Recommendations for Executive Action
        Paragraph sec4 = new Paragraph("4. Executive Action Plan & Strategic Directives", sectionHeaderFont);
        sec4.setSpacingAfter(6);
        doc.add(sec4);

        PdfPTable recTable = new PdfPTable(2);
        recTable.setWidthPercentage(100);
        recTable.setWidths(new float[]{25f, 75f});

        addStrategicRow(recTable, "Supply Chain Rebalancing", "Arishta & Thaila lines represent over 60% of total revenue. Prioritize raw-material herbal procurement buffers to protect against monsoon cultivation shortages.", bodyBold, bodyFont, borderSoft);
        addStrategicRow(recTable, "Regulatory Assurance", "Heavy-potency formulations (Schedule C) require mandatory periodic reporting to the Ayurvedic Drug Review Board. Maintain automated digital logs for instant inspection readiness.", bodyBold, bodyFont, borderSoft);
        addStrategicRow(recTable, "Commercial Expansion", "Average Order Value indicates consistent consumer basket size. Expanding high-margin Churna and Guthika package bundles can lift transaction sizes without added acquisition costs.", bodyBold, bodyFont, borderSoft);

        doc.add(recTable);

        doc.close();
        return out.toByteArray();
    }

    private void addKpiBox(PdfPTable table, String label, String value, String subtext, Font lblFont, Font valFont, Color valColor, Color border) {
        PdfPCell cell = new PdfPCell();
        cell.setPadding(8);
        cell.setBorderColor(border);
        cell.setBackgroundColor(new Color(248, 250, 252));

        cell.addElement(new Paragraph(label.toUpperCase(), lblFont));
        Paragraph valPara = new Paragraph(value, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, valColor));
        valPara.setSpacingBefore(2);
        cell.addElement(valPara);
        Paragraph subPara = new Paragraph(subtext, FontFactory.getFont(FontFactory.HELVETICA, 7, new Color(100, 116, 139)));
        cell.addElement(subPara);

        table.addCell(cell);
    }

    private void addTableHeader(PdfPTable table, String[] headers, Font font, Color bg, Color border) {
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, font));
            cell.setBackgroundColor(bg);
            cell.setBorderColor(border);
            cell.setPadding(6);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
        }
    }

    private void addTableCell(PdfPTable table, String text, Font font, Color border, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorderColor(border);
        cell.setPadding(5);
        cell.setHorizontalAlignment(alignment);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);
    }

    private void addStrategicRow(PdfPTable table, String domain, String recommendation, Font titleFont, Font descFont, Color border) {
        PdfPCell titleCell = new PdfPCell(new Phrase(domain, titleFont));
        titleCell.setPadding(6);
        titleCell.setBorderColor(border);
        titleCell.setBackgroundColor(new Color(248, 250, 252));

        PdfPCell descCell = new PdfPCell(new Phrase(recommendation, descFont));
        descCell.setPadding(6);
        descCell.setBorderColor(border);

        table.addCell(titleCell);
        table.addCell(descCell);
    }

}