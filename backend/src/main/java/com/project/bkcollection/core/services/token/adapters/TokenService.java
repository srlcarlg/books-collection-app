package com.project.bkcollection.core.services.token.adapters;

public interface TokenService {

    String createAccessToken(String subject);
    String getSubjectAccessToken(String accessToken);

    String createRefreshToken(String subject);
    String getSubjectRefreshToken(String refreshToken);

}

