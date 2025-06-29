package vagabounds.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vagabounds.enums.CandidateEducation;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "candidates")
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "account_id", nullable = false, unique = true)
    private Account account;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address", nullable = false)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "education", nullable = false)
    private CandidateEducation education;

    @Column(name = "course")
    private String course;

    @Column(name = "semester")
    private Integer semester;

    @Column(name = "graduation_year")
    private Integer graduationYear;

    @Column(name = "resume_url")
    private String resumeUrl;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // TODO: COLOCAR O RESTO DOS ATRIBUTOS E RELACIONAMENTOS

    /*
    * Construtor para registrar um novo candidato
    * */
    public Candidate(
        Account account,
        String name,
        String address,
        CandidateEducation education,
        String course,
        Integer semester,
        Integer graduationYear,
        String resumeUrl
    ) {
        this.account = account;
        this.name = name;
        this.address = address;
        this.education = education;
        this.course = course;
        this.semester = semester;
        this.graduationYear = graduationYear;
        this.resumeUrl = resumeUrl;
        this.isDeleted = false;
        this.createdAt = LocalDateTime.now();
    }
}
