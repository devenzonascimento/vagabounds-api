package vagabounds.controllers;

import vagabounds.dtos.auth.*;
import vagabounds.dtos.auth.RegisterCompanyRequest;
import vagabounds.security.JwtUtil;
import vagabounds.security.AppUserDetailsService;
import vagabounds.services.AuthService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

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
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest req) {
        authManager.authenticate(
            new UsernamePasswordAuthenticationToken(req.email(), req.password())
        );

        UserDetails user = userDetailsService.loadUserByUsername(req.email());

        String token = jwtUtil.generateToken(user);

        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/register-company")
    public ResponseEntity<String> register(@RequestBody RegisterCompanyRequest request) {
        authService.registerCompany(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
