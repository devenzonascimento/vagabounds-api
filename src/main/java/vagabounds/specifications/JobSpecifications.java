package vagabounds.specifications;

import org.springframework.data.jpa.domain.Specification;
import vagabounds.enums.JobType;
import vagabounds.models.Candidate;
import vagabounds.models.Job;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class JobSpecifications {
    public static Specification<Job> belongsToCompany(Long companyId) {
        return (root, query, cb) -> cb.equal(root.get("company").get("id"), companyId);
    }

    public static Specification<Job> isOpen(Boolean open) {
        if (open == null) return null;
        return (root, query, cb) -> cb.equal(root.get("isOpen"), open);
    }

    public static Specification<Job> createdBetween(LocalDate from, LocalDate to) {
        if (from == null && to == null) return null;

        if (from != null && to != null) {
            return (root, query, cb) -> cb.between(root.get("createdAt"), from.atStartOfDay(), to.atTime(23,59,59));
        }

        if (from != null) {
            return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("createdAt"), from.atStartOfDay());
        }

        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("createdAt"), to.atTime(23,59,59));
    }

    public static Specification<Job> openAndEligible(Candidate c) {
        return (root, query, cb) -> {
            var predicates = cb.conjunction();
            // vaga aberta
            predicates.getExpressions().add(cb.isTrue(root.get("isOpen")));
            // não deletada
            predicates.getExpressions().add(cb.isFalse(root.get("isDeleted")));
            // prazo
            predicates.getExpressions().add(cb.greaterThanOrEqualTo(
                root.get("expiresAt"), LocalDateTime.now()
            ));
            // elegibilidade por educação
            switch (c.getEducation()) {
                case NONE -> predicates.getExpressions().add(cb.equal(root.get("jobType"), JobType.FULL_TIME));
                case ENROLLED -> predicates.getExpressions().add(cb.equal(root.get("jobType"), JobType.INTERNSHIP));
                case GRADUATED -> predicates.getExpressions().add(root.get("jobType").in(
                    JobType.TRAINEE, JobType.FULL_TIME
                ));
            }
            return predicates;
        };
    }
}
