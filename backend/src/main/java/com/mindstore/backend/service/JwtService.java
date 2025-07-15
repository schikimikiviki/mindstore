package com.mindstore.backend.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    /**
     *
     * @param token in string format
     * function: get the username, used in the jwtAuthenticationFilter
     * @return the claim (info about the jwt token)
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     *
     * @param token the JWT token that we extract the claim from
     * @param claimsResolver - a function that resolves the claim
     * @param <T> type of claim
     * @return the extracted claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     *
     * @param userDetails details to include in the token
     * @return the generated token
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     *
     * @param extraClaims additional claims to include
     * @param userDetails - the userDetails for the token
     * @return the generated token
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    /**
     *
     * @param extraClaims additional claims to include
     * @param userDetails the userDetails for the token
     * @param expiration expiration time in miliseconds
     * @return the generated token
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails, Long expiration) {
        long exp = expiration != null ? expiration : jwtExpiration;
        return buildToken(extraClaims, userDetails, exp);
    }

    /**
     * function: used for the /auth/check controller method
     * @return the remaining time until expiration
     */
    public long getExpirationTime() {
        return jwtExpiration;
    }

    /**
     *
     * @param extraClaims additional claims to include
     * @param userDetails the userDetails for the token
     * @param expiration expiration time in miliseconds
     * @return the compacted jwt token string
     */
    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     *
     * @param token - the token that needs validation
     * @param userDetails the userDetails that we expect the token to have
     * @return true if the token is valid & not expired --> else false
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     *
     * @param token the token we are checking
     * @return true if the token is expired --> else false
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     *
     * @param token the token we are checking
     * @return the expiration date
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     *
     * @param token the token we are checking
     * @return object containing all claims
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * function: get the encoded key
     * @return key object with base64-encoded key
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
