package com.qadr.bankapi.controller;

import com.qadr.bankapi.errors.CustomException;
import com.qadr.bankapi.security.JWTUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;


    @PostMapping("/auth")
    public ResponseEntity<Map<String, String>> login (@RequestBody LoginRequest loginRequest){

        try{
            var authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
            Authentication auth = authenticationManager.authenticate(authenticationToken);
            User user = (User) auth.getPrincipal();
            Map<String, String> tokens = new HashMap<>();
            tokens.put("access_token", JWTUtil.createAccessToken(user, "/auth"));
            tokens.put("refresh_token", JWTUtil.createRefreshToken(user));
            return new ResponseEntity<>(tokens, HttpStatus.OK);
        } catch (Exception e) {
            throw new CustomException("Incorrect username or password", HttpStatus.BAD_REQUEST);
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class LoginRequest{
        String username;
        String password;
    }

}
