package com.ferremas.auth;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class AuthResponse {
    private String token;
    private String nombreUsuario;
    private String rutUsuario;
    private List<String> roles;

    public AuthResponse(String token, String nombreUsuario, String rutUsuario, List<String> roles) {
        this.token = token;
        this.nombreUsuario = nombreUsuario;
        this.rutUsuario = rutUsuario;
        this.roles = roles;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getRutUsuario() {
        return rutUsuario;
    }

    public void setRutUsuario(String rutUsuario) {
        this.rutUsuario = rutUsuario;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}