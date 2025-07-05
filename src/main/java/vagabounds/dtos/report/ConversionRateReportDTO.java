package vagabounds.dtos.report;

public record ConversionRateReportDTO(
    Long jobId,
    String jobTitle,
    long totalApplications,
    long approvedCount,
    long rejectedCount,
    long autoRejectedCount,
    double approvedPercent,
    double rejectedPercent,
    double autoRejectedPercent
) {
}
