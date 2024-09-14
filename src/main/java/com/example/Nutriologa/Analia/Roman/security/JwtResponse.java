package com.example.Nutriologa.Analia.Roman.security;
public class JwtResponse {
    private String token;

    public JwtResponse(String token, String s) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}