package com.ferremas.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;


public class CustomUserDetails implements UserDetails {

    private final Usuario usuario;

    public CustomUserDetails(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return usuario.getRoles().stream()
                .map(rol -> new SimpleGrantedAuthority("ROLE_" +rol.getNombre().toUpperCase()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return usuario.getContrasena();
    }

    @Override
    public String getUsername() {
        return usuario.getCorreo(); // o usuario.getRutUsuario() si es el identificador principal
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Puedes ajustar según lógica de tu sistema
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Ajustar si hay bloqueo de usuarios
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Si tienes vencimiento de contraseña
    }

    @Override
    public boolean isEnabled() {
        return true; // O usa usuario.getActivo() si tienes ese campo
    }

    public Usuario getUsuario() {
        return usuario;
    }

}
