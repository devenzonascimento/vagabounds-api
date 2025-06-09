package vagabounds.controllers;

import vagabounds.dtos.auth.*;
import vagabounds.security.AppUserDetailsService;
import vagabounds.security.JwtUtil;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final AppUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager am, AppUserDetailsService uds, JwtUtil jwtUtil) {
        this.authManager = am;
        this.userDetailsService = uds;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest req) {
        authManager.authenticate(
            new UsernamePasswordAuthenticationToken(req.email(), req.password())
        );

        UserDetails user = userDetailsService.loadUserByUsername(req.email());

        String token = jwtUtil.generateToken(user);

        return ResponseEntity.ok(new AuthResponse(token));
    }
}
