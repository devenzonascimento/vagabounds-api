package vagabounds.models;

import jakarta.persistence.*;
import lombok.*;
import vagabounds.enums.JobModality;
import vagabounds.enums.JobType;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "jobs")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "job_type", nullable = false)
    private JobType jobType;

    @Enumerated(EnumType.STRING)
    @Column(name = "job_modality", nullable = false)
    private JobModality jobModality;

    @ElementCollection
    @CollectionTable(name = "job_requirements", joinColumns = @JoinColumn(name = "job_id"))
    @Column(name = "requirement")
    private Set<String> requirements = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "job_desired_skills", joinColumns = @JoinColumn(name = "job_id"))
    @Column(name = "desired_skill")
    private Set<String> desiredSkills = new HashSet<>();

    @Column(name = "is_open", nullable = false)
    private Boolean isOpen = true;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "closed_at")
    private LocalDateTime closedAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Application> applications = new HashSet<>();

    public Job(
        Company company,
        String title,
        String description,
        JobType jobType,
        JobModality jobModality,
        List<String> requirements,
        List<String> desiredSkills,
        LocalDateTime expiresAt
    ) {
        this.company = company;
        this.title = title;
        this.description = description;
        this.jobType = jobType;
        this.jobModality = jobModality;
        this.requirements = new HashSet<>(requirements);
        this.desiredSkills = new HashSet<>(desiredSkills);
        this.isOpen = true;
        this.isDeleted = false;
        this.createdAt = LocalDateTime.now();
        this.closedAt = null;
        this.expiresAt = expiresAt;
    }

    public void update(
        String title,
        String description,
        JobType jobType,
        JobModality jobModality,
        List<String> requirements,
        List<String> desiredSkills
    ) {
        if (title != null) {
            setTitle(title);
        }

        if (description != null) {
            setDescription(description);
        }

        if (jobType != null) {
            setJobType(jobType);
        }

        if (jobModality != null) {
            setJobModality(jobModality);
        }

        if (requirements != null) {
            setRequirements(new HashSet<>(requirements));
        }

        if (desiredSkills != null) {
            setDesiredSkills(new HashSet<>(desiredSkills));
        }
    }

    public void extendExpiresAt(
        LocalDateTime newExpiresAt
    ) {
        if (!isOpen || closedAt != null) {
            throw new RuntimeException("The job is already closed.");
        }

        if (newExpiresAt == null || newExpiresAt.isBefore(expiresAt) || newExpiresAt.isEqual(expiresAt)) {
            throw new RuntimeException("Invalid date to extends the deadline.");
        }

        setExpiresAt(newExpiresAt);
    }

    public void closeManually() {
        this.closedAt = LocalDateTime.now();
        this.isOpen = false;
    }
}
