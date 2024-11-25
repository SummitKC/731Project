package org.cps731.project.team.cps731.pomodoro.security.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private final long expirationTime;
    private final Algorithm algorithm;

    @Autowired
    public JwtUtil(@Value("${jwt.secret}") String secretKey, @Value("${jwt.token.expiry.millis}") long expirationTime) {
        this.expirationTime = expirationTime;
        algorithm = Algorithm.HMAC256(secretKey);
    }

    /**
     * Generates JWT from a user's email
     * @param email The user's email
     * @return The JWT
     */
    public String generateToken(String email) {
        return JWT.create()
                .withSubject(email)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .sign(algorithm);
    }

    /**
     * Validates and decodes a given JWT.
     * @param token The JWT as a String.
     * @return The decoded JWT.
     */
    public DecodedJWT validateTokenAndGetDecoded(String token) {
        JWTVerifier verifier = JWT.require(algorithm)
                .build();
        return verifier.verify(token);
    }

    /**
     * Extracts the subject of a JWT.
     * @param token The JWT.
     * @return The subject (the security ID of the user).
     */
    public String extractSubject(String token) {
        return validateTokenAndGetDecoded(token).getSubject();
    }

    /**
     * Checks if the JWT is valid
     * @param token The JWT
     * @param email The user's email
     * @return Whether the token is valid.
     */
    public boolean isTokenValid(String token, String email) {
        try {
            JWTVerifier verifier = JWT.require(algorithm)
                    .withSubject(email)
                    .build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    /**
     * Checks if a JWT is expired.
     * @param token The JWT.
     * @return Whether the token is expired.
     */
    public boolean isTokenExpired(String token) {
        Date expiration = validateTokenAndGetDecoded(token).getExpiresAt();
        return expiration.before(new Date());
    }
}
