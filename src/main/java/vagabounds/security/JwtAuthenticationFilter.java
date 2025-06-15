package vagabounds.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final AppUserDetailsService uds;
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(AppUserDetailsService uds, JwtUtil jwtUtil) {
        this.uds = uds;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest req,
        HttpServletResponse res, FilterChain chain
    ) throws ServletException, IOException {
        String header = req.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            try {
                var auth = jwtUtil.parseToken(header.substring(7));

                Object principal = auth.getPrincipal();

                if (principal instanceof AppUserDetails) {
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }

                if (principal instanceof String username) {
                    AppUserDetails userDetails = uds.loadUserByUsername(username);

                    UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                    );

                    SecurityContextHolder.getContext().setAuthentication(newAuth);
                }
            } catch (JwtException e) {
                res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
                return;
            }
        }

        chain.doFilter(req, res);
    }
}
