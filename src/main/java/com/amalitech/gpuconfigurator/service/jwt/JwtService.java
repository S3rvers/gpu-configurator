package com.amalitech.gpuconfigurator.service.jwt;

import com.amalitech.gpuconfigurator.model.UserSession;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

public interface JwtService {

    String extractEmail(String jwtToken);

    <T> T extractSingleClaim(String jwtToken, Function<Claims, T> claimsResolver);

    String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);

    String generateToken(UserDetails userDetails);

    String generateTokenForUserSession(UserSession userSession);

    String extractSubject(String jwtToken);

    boolean isTokenValid(String jwtToken, UserDetails userDetails);

    Date extractExpiration(String jwtToken);
}
