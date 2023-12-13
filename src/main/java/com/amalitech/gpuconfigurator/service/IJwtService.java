package com.amalitech.gpuconfigurator.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

public interface IJwtService {

    String extractEmail(String jwtToken);

    <T> T extractSingleClaim(String jwtToken, Function<Claims, T> claimsResolver);

    String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);

    String generateToken(UserDetails userDetails);

    boolean isTokenValid(String jwtToken, UserDetails userDetails);

    Date extractExpiration(String jwtToken);
}
