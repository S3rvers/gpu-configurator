package com.amalitech.gpuconfigurator.service.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {
    @Value("${secret-key}")
    private String secret;

    @Value("${jwt-token-expiration}")
    private int tokenExpirationTime;

    public String extractEmail(String jwtToken) {
        return extractSingleClaim(jwtToken, Claims::getSubject);
    }

    public <T> T extractSingleClaim(String jwtToken, Function<Claims, T> claimsResolver) {
        try {
            final Claims claims = extractAllClaims(jwtToken);
            return claimsResolver.apply(claims);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid Jwt", e);
        }
    }

    private Claims extractAllClaims(String jwtToken) {
        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(jwtToken)
                    .getBody();
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid Jwt", e);
        }

    }

    /**
     * Generates a HMAC-SHA key for signing in using a secret.
     *
     * @param secret the secret used to generate the key, encoded in Base64
     * @return a {@code Key} object representing the HMAC-SHA key
     */
    private Key getSignInKey() {
        byte[] byteKey = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(byteKey);
    }


    /**
     * Generates a JSON Web Token (JWT) for authentication using extra claims and user details.
     *
     * @param extraClaims a map of additional claims to be included in the token
     * @param userDetails an object containing the user's username and other information
     * @return a string representation of the JWT
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername()) //getUsername() is a getter for the email in user model
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpirationTime))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Generates a JSON Web Token (JWT) for authentication using user details.
     * This method invokes {@link #generateToken(Map, UserDetails) generateToken} with an empty map as the first argument.
     *
     * @param userDetails an object containing the user's username and other information
     * @return a string representation of the JWT
     */

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public boolean isTokenValid(String jwtToken, UserDetails userDetails) {
        final String userEmail = extractEmail(jwtToken);
        return userEmail.equals(userDetails.getUsername()) && !isTokenExpired(jwtToken);
    }

    /**
     * Checks if a given JWT token is valid for a given user details.
     *
     * @param jwtToken    the JWT token to validate
     * @param userDetails the user details to match with the token
     * @return true if the token is valid, false otherwise
     */
    private boolean isTokenExpired(String jwtToken) {
        return extractExpiration(jwtToken).before(new Date());
    }

    /**
     * Checks if a JWT token is expired by comparing its expiration date with the current date.
     *
     * @param jwtToken the JWT token to check
     * @return true if the token is expired, false otherwise
     */
    public Date extractExpiration(String jwtToken) {
        return extractSingleClaim(jwtToken, Claims::getExpiration);
    }
}
