package vagabounds.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vagabounds.models.Job;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {
    List<Job> findAllByCompanyId(Long companyId);

    @Query("SELECT j FROM Job j WHERE j.company.group.id = :groupId AND j.isDeleted = false")
    List<Job> findAllByGroupIdAndIsActiveTrue(@Param("groupId") Long groupId);

    @Query("SELECT j FROM Job j WHERE j.company.group.id = :groupId AND j.company.id = :companyId AND j.isDeleted = false")
    List<Job> findAllByGroupIdAndCompanyIdAndIsActiveTrue(@Param("groupId") Long groupId, @Param("companyId") Long companyId);
}