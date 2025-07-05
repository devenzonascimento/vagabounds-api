package vagabounds.dtos.report;

public record DecisionTimeReportDTO(
    Long jobId,
    String jobTitle,
    double averageDecisionHours  // média em horas
) {
}
