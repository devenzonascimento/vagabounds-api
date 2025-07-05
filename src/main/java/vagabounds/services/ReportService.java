package vagabounds.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vagabounds.dtos.report.CompanyPerformanceReportDTO;
import vagabounds.dtos.report.ConversionRateReportDTO;
import vagabounds.dtos.report.DecisionTimeReportDTO;
import vagabounds.enums.ApplicationStatus;
import vagabounds.models.Application;
import vagabounds.repositories.ReportRepository;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReportService {
    @Autowired
    ReportRepository reportRepository;

    public List<ConversionRateReportDTO> getConversionRatePerJob() {
        return reportRepository.fetchRawConversion().stream()
            .map(r -> {
                double total = r.totalApplications() > 0 ? r.totalApplications() : 1;
                double approvedPct = (r.approvedCount() * 100.0) / total;
                double rejectedPct = (r.rejectedCount() * 100.0) / total;
                double autoRejectedPct = (r.autoRejectedCount() * 100.0) / total;
                return new ConversionRateReportDTO(
                    r.jobId(),
                    r.jobTitle(),
                    r.totalApplications(),
                    r.approvedCount(),
                    r.rejectedCount(),
                    r.autoRejectedCount(),
                    approvedPct,
                    rejectedPct,
                    autoRejectedPct
                );
            })
            .collect(Collectors.toList());
    }

    public List<CompanyPerformanceReportDTO> getCompanyPerformance(LocalDate from, LocalDate to) {
        if (from == null) {
            from = LocalDate.MIN;
        }

        if (to == null) {
            to = LocalDate.MAX;
        }

        return reportRepository.fetchCompanyPerformance(from.atStartOfDay(), to.atTime(23, 59, 59));
    }

    public List<DecisionTimeReportDTO> getDecisionTimeReport(LocalDate fromDate, LocalDate toDate) {
        LocalDateTime start = Optional.ofNullable(fromDate)
            .map(d -> d.atStartOfDay())
            .orElse(LocalDate.MIN.atStartOfDay());
        LocalDateTime end = Optional.ofNullable(toDate)
            .map(d -> d.atTime(LocalTime.MAX))
            .orElse(LocalDate.MAX.atTime(LocalTime.MAX));

        List<Application> apps = reportRepository.findByStatusInAndAppliedAtBetween(
            List.of(ApplicationStatus.APPROVED, ApplicationStatus.REJECTED),
            start, end
        );

        return apps.stream()
            .collect(Collectors.groupingBy(a -> a.getJob().getId()))
            .entrySet().stream()
            .map(entry -> {
                Long jobId = entry.getKey();
                List<Application> list = entry.getValue();

                double avgHours = list.stream()
                    .mapToDouble(a -> {
                        Duration d = Duration.between(a.getAppliedAt(), a.getUpdatedAt());
                        return d.toMinutes() / 60.0;
                    })
                    .average().orElse(0.0);

                String title = list.get(0).getJob().getTitle();
                return new DecisionTimeReportDTO(jobId, title, avgHours);
            })
            .toList();
    }
}
