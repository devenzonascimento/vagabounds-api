package vagabounds.dtos.candidate;

import vagabounds.enums.CandidateEducation;
import vagabounds.models.Candidate;

public record CandidateDTO(
    Long id,
    String name,
    String email,
    String address,
    CandidateEducation education,
    String course,
    Integer semester,
    Integer graduationYear
) {
    public static CandidateDTO fromCandidate(Candidate candidate) {
        return new CandidateDTO(
            candidate.getId(),
            candidate.getName(),
            candidate.getAccount().getEmail(),
            candidate.getAddress(),
            candidate.getEducation(),
            candidate.getCourse(),
            candidate.getSemester(),
            candidate.getGraduationYear()
        );
    }
}
