package vagabounds.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vagabounds.enums.ApplicationStatus;
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
    private static final double MIN_PERCENTAGE_TO_MATCH = 50.0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ApplicationStatus status;

    @Column(name = "decision_reason")
    private String decisionReason;

    @Column(name = "candidate_presentation")
    private String candidatePresentation;

    @ElementCollection
    @CollectionTable(name = "application_candidate_skills", joinColumns = @JoinColumn(name = "application_id"))
    @Column(name = "application_candidate_skills")
    private Set<String> candidateSkills = new HashSet<>();

    @Column(name = "applied_at", nullable = false)
    private LocalDateTime appliedAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "resume_url")
    private String resumeUrl;

    /**
     * Construtor para criar a aplicação de vaga.
     */
    public Application(Job job, Candidate candidate, String candidatePresentation, Set<String> candidateSkills) {
        if (!job.getIsOpen()) {
            throw new RuntimeException("OOPS! The job you are applying is already closed.");
        }

        var errorMessage = validateJobApplication(job, candidate, candidateSkills);

        if (!errorMessage.isBlank()) {
            this.status = ApplicationStatus.AUTO_REJECTED;
            this.decisionReason = errorMessage;
        } else {
            this.status = ApplicationStatus.APPLIED;
        }

        this.job = job;
        this.candidate = candidate;
        this.candidatePresentation = candidatePresentation;
        this.candidateSkills = candidateSkills;

        var now = LocalDateTime.now();
        this.appliedAt = now;
        this.updatedAt = now;
    }

    /**
     * Construtor para criar a aplicação de vaga usando o curriculo.
     */
    public Application(Job job, Candidate candidate) {
        if (!job.getIsOpen()) {
            throw new RuntimeException("OOPS! The job you are applying is already closed.");
        }

        var errorMessage = validateJobApplication(job, candidate, null);

        if (!errorMessage.isBlank()) {
            this.status = ApplicationStatus.AUTO_REJECTED;
            this.decisionReason = errorMessage;
        } else {
            this.status = ApplicationStatus.APPLIED;
        }

        this.job = job;
        this.candidate = candidate;

        var now = LocalDateTime.now();
        this.appliedAt = now;
        this.updatedAt = now;
    }

    public void approve(String reason) {
        if (status != ApplicationStatus.APPLIED) {
            throw new RuntimeException("A decision has already been made for this job application.");
        }

        this.status = ApplicationStatus.APPROVED;
        this.decisionReason = reason;
        this.updatedAt = LocalDateTime.now();
    }

    public void reject(String reason) {
        if (status != ApplicationStatus.APPLIED) {
            throw new RuntimeException("A decision has already been made for this job application.");
        }

        this.status = ApplicationStatus.REJECTED;
        this.decisionReason = reason;
        this.updatedAt = LocalDateTime.now();
    }

    private static String validateJobApplication(Job job, Candidate candidate, Set<String> candidateSkills) {
        switch (job.getJobType()) {
            case INTERNSHIP -> {
                if (!candidate.getEducation().equals(CandidateEducation.ENROLLED)) {
                    return "Desculpe, você precisa ser um estudante para se candidatar.";
                }

                if (candidate.getCourse().isBlank()) {
                    return "É necessário informar o nome do curso.";
                }

                if (candidate.getSemester() == null) {
                    return "É necessário informar o semestre.";
                }
            }
            case TRAINEE -> {
                if (!candidate.getEducation().equals(CandidateEducation.GRADUATED)) {
                    return "Desculpe, você precisa ser graduado para se candidatar.";
                }

                if (candidate.getCourse().isBlank()) {
                    return "É necessário informar o nome do curso.";
                }

                if (candidate.getGraduationYear() == null) {
                    return "É obrigatório informar o ano de formatura.";
                }
            }
        }

        if (candidateSkills == null) {
            return "";
        }

        var requirementsMatched = job.getRequirements()
            .stream()
            .filter(r -> candidateSkills.stream().anyMatch(s -> s.equals(r)))
            .toList();

        double percentage = (double) requirementsMatched.size() / job.getRequirements().size() * 100;

        if (percentage < MIN_PERCENTAGE_TO_MATCH) {
            return "Sorry, your skills are not compatible enough with this job.";
        }

        return "";
    }
}
