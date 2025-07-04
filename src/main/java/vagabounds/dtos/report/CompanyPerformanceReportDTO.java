package vagabounds.dtos.report;

public record CompanyPerformanceReportDTO(
    Long companyId,
    String companyName,
    long openJobsCount,
    long totalApplications,
    long approvedApplications
) {
}
