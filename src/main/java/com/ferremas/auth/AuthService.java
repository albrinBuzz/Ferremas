package com.ferremas.auth;

import com.ferremas.config.security.jwt.JwtService;
import com.ferremas.model.CustomUserDetails;
import com.ferremas.model.Rol;
import com.ferremas.model.Usuario;
import com.ferremas.repository.UsuarioRepository;
import com.ferremas.service.UsuarioService;
import com.ferremas.serviceImpl.CustomUserDetailsService;
import com.ferremas.util.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {


    private JwtService jwtService;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private CustomUserDetailsService userDetailsService;


    public AuthService(JwtService jwtService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, CustomUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (BadCredentialsException ex) {
            // Puedes lanzar una excepción personalizada o devolver un error controlado
            throw new RuntimeException("Usuario o contraseña incorrectos");
        }

        UserDetails user = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtService.generateToken(user);
        Usuario usuario = ((CustomUserDetails) user).getUsuario();
        List<String> roles = usuario.getRoles().stream()
                .map(Rol::getNombre)
                .toList();

        return new AuthResponse(
                token,
                usuario.getNombreusuario(),
                usuario.getRutUsuario(),
                roles
        );


    }

    public AuthResponse register(RegisterRequest request) {

        /*Usuario usuario=new Usuario();
        usuario.setCorreo(request.getUsername());
        usuario.setContrasena(passwordEncoder.encode(request.getPassword()));
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstname(request.getFirstname())
                .lastname(request.lastname)
                .country(request.getCountry())
                .role(Role.USER)
                .build();

        userRepository.save(user);

        return AuthResponse.builder()
                .token(jwtService.getToken(user))
                .build();*/
        return null;


    }
}

