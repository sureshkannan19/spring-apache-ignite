package com.sk.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sk.constants.FilterConstants;
import com.sk.constants.TokenProperties;
import com.sk.model.UsersModel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Service
@RequiredArgsConstructor
public class JWTokenService {

    private final TokenProperties tokenProperties;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public UsersModel getUserDetailsFromToken(String token) throws JsonProcessingException {
        Claims claims = extractAllClaims(token);
        Map userClaims = claims.get(FilterConstants.USER_CLAIMS, Map.class);
        return objectMapper.readValue(objectMapper.writeValueAsString(userClaims), UsersModel.class);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token).getPayload();
    }

    public String generateToken(UsersModel usersModel) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(FilterConstants.USER_CLAIMS, usersModel);
        return createToken(claims);
    }

    private String createToken(Map<String, Object> claims) {
        JwtBuilder jwtBuilder = Jwts.builder().claims(claims)
                .subject(FilterConstants.SUBJECT)
                .issuer(tokenProperties.getIssuer())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .audience().add("Public").and()
                .signWith(getSecretKey(), Jwts.SIG.HS256);
        return jwtBuilder.compact();
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(tokenProperties.getSecretKey()));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Date isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
