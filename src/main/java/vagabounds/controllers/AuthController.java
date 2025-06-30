package vagabounds.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vagabounds.dtos.auth.AuthRequest;
import vagabounds.dtos.auth.AuthResponse;
import vagabounds.dtos.auth.RegisterCandidateRequest;
import vagabounds.dtos.auth.RegisterCompanyRequest;
import vagabounds.security.AppUserDetails;
import vagabounds.security.AppUserDetailsService;
import vagabounds.security.JwtUtil;
import vagabounds.services.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authManager;

    @Autowired
    AppUserDetailsService userDetailsService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthRequest req) {
        authManager.authenticate(
            new UsernamePasswordAuthenticationToken(req.email(), req.password())
        );

        AppUserDetails user = userDetailsService.loadUserByUsername(req.email());

        String token = jwtUtil.generateToken(user);

        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/register-company")
    public ResponseEntity<String> registerCompany(@RequestBody @Valid RegisterCompanyRequest request) {
        authService.registerCompany(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/register-candidate")
    public ResponseEntity<String> registerCandidate(@RequestBody @Valid RegisterCandidateRequest request) {
        authService.registerCandidate(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
