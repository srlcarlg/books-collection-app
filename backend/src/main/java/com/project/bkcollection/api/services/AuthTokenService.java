package com.project.bkcollection.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.project.bkcollection.api.dtos.request.RefreshTokenRequest;
import com.project.bkcollection.api.dtos.request.TokenRequest;
import com.project.bkcollection.api.dtos.response.TokenResponse;
import com.project.bkcollection.core.services.token.TokenBlackListService;
import com.project.bkcollection.core.services.token.adapters.TokenService;

@Service
public class AuthTokenService {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenBlackListService tokenBlackListService;

    public TokenResponse authenticate(TokenRequest tokenRequest) {
    	String email = tokenRequest.getEmail();
    	String password = tokenRequest.getPassword();

        var authentication = new UsernamePasswordAuthenticationToken(email, password);
        authenticationManager.authenticate(authentication);

        String access = tokenService.createAccessToken(email);
        String refresh = tokenService.createRefreshToken(email);

        return new TokenResponse(access, refresh);
    }

    public TokenResponse reAuthenticate(RefreshTokenRequest refrehRequest) {
    	String token = refrehRequest.getRefresh();
        tokenBlackListService.verifyToken(token);

        String email = tokenService.getSubjectRefreshToken(token);

        userDetailsService.loadUserByUsername(email);

        String access = tokenService.createAccessToken(email);
        String refresh = tokenService.createRefreshToken(email);

        tokenBlackListService.addTokenInBlackList(token);

        return new TokenResponse(access, refresh);
    }

    public void logout(RefreshTokenRequest refrehRequest) {
        String token = refrehRequest.getRefresh();
        tokenBlackListService.addTokenInBlackList(token);
    }

}
