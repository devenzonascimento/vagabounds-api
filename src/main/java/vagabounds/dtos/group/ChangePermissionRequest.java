package vagabounds.dtos.group;

import jakarta.validation.constraints.NotNull;
import vagabounds.enums.GroupPermission;

public record ChangePermissionRequest(
        @NotNull(message = "Company ID is required")
        Long companyId,

        @NotNull(message = "Permission is required")
        GroupPermission permission
) {
}