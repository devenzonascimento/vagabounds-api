package vagabounds.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vagabounds.enums.CandidateEducation;

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
    @JoinColumn(name="account_id", unique=true)
    private Account account;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address", nullable = false)
    private String address;

    @Column (name = "education", nullable = false)
    private CandidateEducation education;

    @Column (name = "course")
    private String course;

    @Column (name = "semester")
    private Integer semester;

    @Column (name = "graduation_year")
    private Integer graduationYear;

    @Column (name = "resumeURL")
    private String resumeURL;

    // TODO: COLOCAR O RESTO DOS ATRIBUTOS E RELACIONAMENTOS
}
