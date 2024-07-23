package com.example.demo_mhdigital.data.user;

import lombok.Data;

@Data
public class TokenResponse {
    private String token;

    public TokenResponse() {}

    public TokenResponse(String token) {
        this.token = token;
    }
}
