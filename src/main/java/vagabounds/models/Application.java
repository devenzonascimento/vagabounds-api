package vagabounds.models;

import jakarta.persistence.*;

@Entity
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    @JoinColumn(name = "job_title")
    private String jobTitle;

    @ManyToOne
    @JoinColumn(name = "candidate_email")
    private Candidate candidate;

    @JoinColumn(name = "candidate_name")
    private String candidateName;




}
