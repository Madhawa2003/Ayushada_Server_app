package com.sliit.ayushada_server.modules.Reporting_And_Analytics;


import com.sliit.ayushada_server.modules.Reporting_And_Analytics.Dto.DashboardReportDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class ReportingController {

    private final ReportingService reportingService;

    public ReportingController(ReportingService reportingService) {
        this.reportingService = reportingService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardReportDTO> getDashboardData(@RequestParam(defaultValue = "MONTHLY") String period) {
        return ResponseEntity.ok(reportingService.generateDashboardMetrics(period));
    }

    @GetMapping("/export/csv")
    public ResponseEntity<byte[]> exportCsv(@RequestParam(defaultValue = "MONTHLY") String period) {
        byte[] csvData = reportingService.generateCsvReport(period);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ayurveda_analytics_" + period.toLowerCase() + ".csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csvData);
    }

    @GetMapping("/export/pdf")
    public ResponseEntity<byte[]> exportPdf(@RequestParam(defaultValue = "MONTHLY") String period) {
        byte[] pdfData = reportingService.generateExecutivePdfReport(period);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ayurveda_executive_briefing_" + period.toLowerCase() + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfData);
    }
}