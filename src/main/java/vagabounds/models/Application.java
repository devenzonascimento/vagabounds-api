package vagabounds.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vagabounds.enums.CandidateEducation;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "applications")
public class Application {
    private static final double MIN_PERCENTAGE_TO_MATCH = 80.0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    @Column(name = "candidate_presentation")
    private String candidatePresentation;

    @ElementCollection
    @CollectionTable(name = "application_candidate_skills", joinColumns = @JoinColumn(name = "application_id"))
    @Column(name = "application_candidate_skills")
    private Set<String> candidateSkills = new HashSet<>();

    @Column(name = "applied_at", nullable = false)
    private LocalDateTime appliedAt;

    @Column(name = "resume_url")
    private String resumeUrl;

    /**
     * Construtor para criar a aplicação de vaga.
     */
    public Application(Job job, Candidate candidate, String candidatePresentation, Set<String> candidateSkills) {
        if (!job.getIsOpen()) {
            throw new RuntimeException("OOPS! The job you are applying is already closed.");
        }

        canApplyTo(job, candidate, candidateSkills);

        this.job = job;
        this.candidate = candidate;
        this.candidatePresentation = candidatePresentation;
        this.candidateSkills = candidateSkills;
        this.appliedAt = LocalDateTime.now();
    }

    public static void canApplyTo(Job job, Candidate candidate, Set<String> candidateSkills) {
        switch (job.getJobType()) {
            case INTERNSHIP -> {
                if (!candidate.getEducation().equals(CandidateEducation.ENROLLED)) {
                    throw new RuntimeException("Sorry, you must be a student to apply.");
                }

                if (candidate.getCourse().isBlank()) {
                    throw new RuntimeException("It is required to inform the course name.");
                }

                if (candidate.getSemester() == null) {
                    throw new RuntimeException("It is required to inform the semester.");
                }
            }
            case TRAINEE -> {
                if (!candidate.getEducation().equals(CandidateEducation.GRADUATED)) {
                    throw new RuntimeException("Sorry, you must have a graduate to apply.");
                }

                if (candidate.getCourse().isBlank()) {
                    throw new RuntimeException("It is required to inform the course name.");
                }

                if (candidate.getGraduationYear() == null) {
                    throw new RuntimeException("It is required to inform the graduation year.");
                }
            }
        }

        var requirementsMatched = job.getRequirements()
            .stream()
            .filter(r -> candidateSkills.stream().anyMatch(s -> s.equals(r)))
            .toList();

        double percentage = (double) requirementsMatched.size() / job.getRequirements().size() * 100;

        if (percentage < MIN_PERCENTAGE_TO_MATCH) {
            throw new RuntimeException("Sorry, your skills are not compatible enough with this job.");
        }
    }
}
