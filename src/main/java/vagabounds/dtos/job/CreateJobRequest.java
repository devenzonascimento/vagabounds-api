package vagabounds.dtos.job;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import vagabounds.enums.JobModality;
import vagabounds.enums.JobType;

import java.time.LocalDateTime;
import java.util.List;

// TODO: Colocar as validações necessárias
public record CreateJobRequest(
        @NotBlank
        String title,

        @NotBlank
        String description,

        @NotNull
        JobType jobType,

        @NotNull
        JobModality jobModality,

        List<String> requirements,
        List<String> desiredSkills,

        @NotNull
        LocalDateTime expiresAt
) {







}
