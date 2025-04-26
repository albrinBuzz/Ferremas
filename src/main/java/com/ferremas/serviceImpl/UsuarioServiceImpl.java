package com.ferremas.serviceImpl;


import com.ferremas.model.Usuario;
import com.ferremas.repository.UsuarioRepository;
import com.ferremas.service.UsuarioService;
import com.ferremas.util.Logger;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {



	@Autowired
	private UsuarioRepository usuarioRepository;


	@Autowired
    private EntityManager em;

    private PasswordEncoder passwordEncoder=new BCryptPasswordEncoder() ;
	
	@Override
	public Usuario save(Usuario us) {
		if (!isBCryptEncoded(us.getContrasena())) {
			Logger.logInfo("Contraseña no encodeada");
			Logger.logInfo("Contraseña: "+us.getContrasena());
			us.setContrasena(passwordEncoder.encode(us.getContrasena()));
		}else {
			Logger.logInfo("Contraseña  encodeada: "+us.getContrasena());
		}

		return usuarioRepository.save(us);
	}

	public boolean isBCryptEncoded(String password) {
		return password != null && password.startsWith("$2a$");
	}



	@Override
	public void update(Usuario us) {
		if (!isBCryptEncoded(us.getContrasena())) {
			Logger.logInfo("Contraseña no encodeada");
			Logger.logInfo("Contraseña: "+us.getContrasena());
			us.setContrasena(passwordEncoder.encode(us.getContrasena()));
		}else {
			Logger.logInfo("Contraseña  encodeada: "+us.getContrasena());
		}

		usuarioRepository.save(us);
	}

	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Usuario> findAll() {
		// TODO Auto-generated method stub
		return usuarioRepository.findAll();
	}

	@Override
	public void create(Usuario us) {
		us.setContrasena(passwordEncoder.encode(us.getContrasena()));
		usuarioRepository.save(us);
		
	}

	@Override
	public Usuario findByCorreo(String email) {
		return usuarioRepository.findByCorreo(email);
	}


	@Override
	public Optional<Usuario> findByRut(String id) {
		// TODO Auto-generated method stub
		return usuarioRepository.findById(id);
	}



}
