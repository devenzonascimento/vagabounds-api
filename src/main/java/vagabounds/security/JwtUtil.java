package vagabounds.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    @Value("${jwt.secret}") private String secret;
    @Value("${jwt.expiration}") private long expirationMs;

    public String generateToken(UserDetails user) {
        var now = new Date();
        var exp = new Date(now.getTime() + expirationMs);
        var roles = user.getAuthorities().stream()
                .map(a -> a.getAuthority()).collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("roles", roles)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public UsernamePasswordAuthenticationToken parseToken(String token) {
        var claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
        var username = claims.getSubject();
        var roles = ((List<?>)claims.get("roles")).stream()
                .map(r -> new SimpleGrantedAuthority((String)r))
                .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(username, null, roles);
    }
}
