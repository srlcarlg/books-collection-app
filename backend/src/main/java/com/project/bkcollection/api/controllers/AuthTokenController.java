package com.project.bkcollection.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.bkcollection.api.controllers.doc.AuthTokenDOC.DOC_AuthTokenController;
import com.project.bkcollection.api.controllers.doc.AuthTokenDOC.DOC_Authenticate;
import com.project.bkcollection.api.controllers.doc.AuthTokenDOC.DOC_Logout;
import com.project.bkcollection.api.controllers.doc.AuthTokenDOC.DOC_reAutheticate;
import com.project.bkcollection.api.dtos.request.RefreshTokenRequest;
import com.project.bkcollection.api.dtos.request.TokenRequest;
import com.project.bkcollection.api.dtos.response.TokenResponse;
import com.project.bkcollection.api.services.AuthTokenService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@DOC_AuthTokenController
public class AuthTokenController {

    @Autowired
    private AuthTokenService service;
    
    @DOC_Authenticate
    @PostMapping("/token")
    public TokenResponse authenticate(@RequestBody @Valid TokenRequest tokenRequest) {
        return service.authenticate(tokenRequest);
    }
    
    @DOC_reAutheticate
    @PostMapping("/refresh")
    public TokenResponse reAuthenticate(@RequestBody @Valid RefreshTokenRequest refrehRequest) {
        return service.reAuthenticate(refrehRequest);
    }
    
    @DOC_Logout
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody @Valid RefreshTokenRequest refrehRequest) {
        service.logout(refrehRequest);
        return new ResponseEntity<>(HttpStatus.RESET_CONTENT);
    }

}
