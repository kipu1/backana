package com.example.Nutriologa.Analia.Roman.model;

public class JwtResponse {

    private String nombre;
    private String token;

    public JwtResponse(String nombre, String token) {
        this.nombre = nombre;
        this.token = token;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
