package vagabounds.specifications;

import org.springframework.data.jpa.domain.Specification;
import vagabounds.enums.ApplicationStatus;
import vagabounds.enums.JobModality;
import vagabounds.enums.JobType;
import vagabounds.models.Application;

import java.time.LocalDate;

public class ApplicationSpecifications {

    public static Specification<Application> hasCandidateId(Long candidateId) {
        return (root, query, cb) ->
            candidateId == null ? null : cb.equal(root.get("candidate").get("id"), candidateId);
    }

    public static Specification<Application> hasStatus(ApplicationStatus status) {
        return (root, query, cb) ->
            status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<Application> hasAppliedAt(LocalDate date) {
        return (root, query, cb) ->
            date == null ? null : cb.equal(cb.function("DATE", LocalDate.class, root.get("appliedAt")), date);
    }

    public static Specification<Application> hasJobType(JobType jobType) {
        return (root, query, cb) ->
            jobType == null ? null : cb.equal(root.get("job").get("jobType"), jobType);
    }

    public static Specification<Application> hasJobModality(JobModality jobModality) {
        return (root, query, cb) ->
            jobModality == null ? null : cb.equal(root.get("job").get("jobModality"), jobModality);
    }
}