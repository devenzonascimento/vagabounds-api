package vagabounds.config;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import vagabounds.security.*;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.builders.*;
import org.springframework.security.config.annotation.method.configuration.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.http.*;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.web.*;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)  // habilita @PreAuthorize
public class SecurityConfig {

    private final AppUserDetailsService uds;
    private final JwtUtil jwtUtil;

    public SecurityConfig(AppUserDetailsService uds, JwtUtil jwtUtil) {
        this.uds = uds;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        var authFilter = new JwtAuthenticationFilter(jwtUtil);

        http
                .csrf().disable()
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(uds)
                .passwordEncoder(new BCryptPasswordEncoder())
                .and().build();
    }
}
