package com.bridgelabz.employeepayroll.utility;

import com.bridgelabz.employeepayroll.model.User;
import com.bridgelabz.employeepayroll.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;
//import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.DoubleStream;

@Component
public class JwtUtility {

    @Autowired
    UserRepository userRepository;

    private static final String SECRET_KEY = "SecretKeyIsMyNameIsPranavAggarwal";

    public String generateToken(String email) {

        return Jwts.builder().setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 100000 * 60 * 1))
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256).compact();
    }

    public String extractEmail(String token) {
        try {
            System.out.println(token);
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            System.out.println("getting email:> " + claims);
            return claims.getSubject();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean validateToken(String token, String userEmail) {
        final String email = extractEmail(token);
        boolean isTokenPresent = true;
        User user = userRepository.findByEmail(userEmail).orElse(null);

        if (user != null && user.getToken() == null) {
            isTokenPresent = false;
        }

        final boolean valid = Jwts.parserBuilder().setSigningKey(SECRET_KEY.getBytes())
                .build()
                .isSigned(token);

        return valid;
    }
}
