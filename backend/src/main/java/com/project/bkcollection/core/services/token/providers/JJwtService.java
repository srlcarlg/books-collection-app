package com.project.bkcollection.core.services.token.providers;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.project.bkcollection.core.services.token.adapters.TokenService;
import com.project.bkcollection.core.services.token.exceptions.TokenServiceException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JJwtService implements TokenService {

    @Value("${com.project.bkcollection.token.access.key}")
    private String rawAccessKey;

    @Value("${com.project.bkcollection.token.access.expiration}")
    private int accessExpiration;

    @Value("${com.project.bkcollection.token.refresh.key}")
    private String rawRefreshKey;

    @Value("${com.project.bkcollection.token.refresh.expiration}")
    private int refreshExpiration;
    
    private Key accessKey;
    private Key refreshKey;

	@Override
	public String createAccessToken(String subject) {
		accessKey = Keys.hmacShaKeyFor(rawAccessKey.getBytes());
		return createToken(accessKey, accessExpiration, subject);
	}
	@Override
	public String getSubjectAccessToken(String accessToken) {
		return getClaims(accessToken, accessKey)
			   .getSubject();
	}

	@Override
	public String createRefreshToken(String subject) {
		refreshKey = Keys.hmacShaKeyFor(rawRefreshKey.getBytes());
		return createToken(refreshKey, refreshExpiration, subject);
	}
	@Override
	public String getSubjectRefreshToken(String refreshToken) {
		return getClaims(refreshToken, refreshKey)
			   .getSubject();
	}
	
    private String createToken(Key signKey, int expiration, String subject) {
        HashMap<String, Object> claims = new HashMap<String, Object>();

        Instant actualTimestamp = Instant.now();
        Instant expirationTimestamp = actualTimestamp.plusSeconds(expiration);

        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(new Date(actualTimestamp.toEpochMilli()))
            .setExpiration(new Date(expirationTimestamp.toEpochMilli()))
            .signWith(signKey, SignatureAlgorithm.HS256)
            .compact();
    }
    
    private Claims getClaims(String token, Key signKey) {
        try {
            return tryGetClaims(token, signKey);
        } catch (JwtException exception) {
            throw new TokenServiceException(exception.getLocalizedMessage());
        } catch (IllegalArgumentException exception) {
            throw new TokenServiceException("Invalid Bearer token! " + exception.getLocalizedMessage());
        }
    }
    
    private Claims tryGetClaims(String token, Key signKey) {
        return Jwts.parserBuilder()
            .setSigningKey(signKey)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

}
