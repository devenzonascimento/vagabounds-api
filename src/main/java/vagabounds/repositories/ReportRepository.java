package vagabounds.repositories;

import vagabounds.dtos.report.CompanyPerformanceReportDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vagabounds.dtos.report.ConversionRateReportDTO;
import vagabounds.enums.ApplicationStatus;
import vagabounds.models.Application;

import java.time.LocalDateTime;
import java.util.List;

public interface ReportRepository extends JpaRepository<Application, Long> {
    @Query("""
      select new vagabounds.dtos.report.ConversionRateReportDTO(
         j.id,
         j.title,
         count(a),
         sum(case when a.status = 'APPROVED' then 1 else 0 end),
         sum(case when a.status = 'REJECTED' then 1 else 0 end),
         sum(case when a.status = 'AUTO_REJECTED' then 1 else 0 end),
         0.0, 0.0, 0.0
      )
      from Application a
      join a.job j
      group by j.id, j.title
    """)
    List<ConversionRateReportDTO> fetchRawConversion();

    @Query("""
      select new vagabounds.dtos.report.CompanyPerformanceReportDTO(
        c.id,
        c.name,
        sum(case when j.isOpen = true then 1 else 0 end),
        count(a),
        sum(case when a.status = 'APPROVED' then 1 else 0 end)
      )
      from Company c
      left join c.jobs j
      left join j.applications a
        on a.appliedAt between :from and :to
      group by c.id, c.name
    """)
    List<CompanyPerformanceReportDTO> fetchCompanyPerformance(
        @Param("from") LocalDateTime from,
        @Param("to")   LocalDateTime to
    );

    List<Application> findByStatusInAndAppliedAtBetween(
        List<ApplicationStatus> statuses,
        LocalDateTime start,
        LocalDateTime end
    );
}
