package vagabounds.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vagabounds.dtos.report.CompanyPerformanceReportDTO;
import vagabounds.dtos.report.ConversionRateReportDTO;
import vagabounds.dtos.report.DecisionTimeReportDTO;
import vagabounds.services.ReportService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {
    @Autowired
    ReportService reportService;

    @GetMapping("/conversion-rate")
    @PreAuthorize("hasRole('COMPANY') or hasRole('ADMIN')")
    public ResponseEntity<List<ConversionRateReportDTO>> conversionRatePerJob() {
        List<ConversionRateReportDTO> report = reportService.getConversionRatePerJob();

        return ResponseEntity.ok(report);
    }

    @GetMapping("/company-performance")
    @PreAuthorize("hasRole('COMPANY') or hasRole('ADMIN')")
    public ResponseEntity<List<CompanyPerformanceReportDTO>> companyPerformance(
        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate from,

        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate to
    ) {
        var report = reportService.getCompanyPerformance(from, to);

        return ResponseEntity.ok(report);
    }

    @GetMapping("/decision-time")
    @PreAuthorize("hasRole('COMPANY') or hasRole('ADMIN')")
    public ResponseEntity<List<DecisionTimeReportDTO>> decisionTime(
        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate from,

        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate to
    ) {
        return ResponseEntity.ok(reportService.getDecisionTimeReport(from, to));
    }
}
