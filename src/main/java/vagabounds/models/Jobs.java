/*
    * `id: Long`
    * `companyId: Long` (FK para Company)
    * `title: String`
    * `description: String`
    * `jobType: Enum {INTERNSHIP, TRAINEE, FULL_TIME, PART_TIME}`
    * `requirements: List<String>`
    * `desiredSkills: List<String>`
    * `deadline: LocalDate` (data limite de inscrição)
    * **Regras específicas:**

        * **INTERNSHIP:** exige `semester` (int) e `course` (String)
        * **TRAINEE:** exige `graduationYear` (int)
    * Empresas podem **prorrogar** o `deadline` antes de expirar.
    * 
    * 
*/

package vagabounds.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import vagabounds.utils.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "jobs")
public class Jobs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "job_type", nullable = false)
    private JobType jobType;

    @ElementCollection
    @CollectionTable(name = "job_requirements", joinColumns = @JoinColumn(name = "job_id"))
    @Column(name = "requirement")
    private Set<String> requirements = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "job_desired_skills", joinColumns = @JoinColumn(name = "job_id"))
    @Column(name = "skill")
    private Set<String> desiredSkills = new HashSet<>();

    @Column(name = "deadline", nullable = false)
    private LocalDate deadline;

}
