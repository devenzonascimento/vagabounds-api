package vagabounds.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    public static Long getAccountId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new IllegalStateException("No authenticated account found");
        }

        Object principal = auth.getPrincipal();
        if (principal instanceof AppUserDetails userDetails) {
            return userDetails.getId();
        }

        throw new IllegalStateException(
            "Unexpected principal type: " + (principal != null ? principal.getClass().getName() : "null")
        );
    }
}
