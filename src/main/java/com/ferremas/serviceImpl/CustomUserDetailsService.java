package com.ferremas.serviceImpl;

import com.ferremas.model.CustomUserDetails;
import com.ferremas.model.Usuario;
import com.ferremas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByCorreo(username);

        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario o password invalido");
        }

        return new CustomUserDetails(usuario);
    }
}
