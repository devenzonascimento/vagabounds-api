package vagabounds.specifications;

import org.springframework.data.jpa.domain.Specification;
import vagabounds.models.Job;

import java.time.LocalDate;

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
}
