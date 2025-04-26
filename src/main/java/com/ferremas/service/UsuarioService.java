package com.ferremas.service;


import com.ferremas.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService{

	public Usuario save(Usuario us);

	public Optional<Usuario> findByRut(String id);
	
	public void update(Usuario us);

	public void delete(Integer id);
	
	public List<Usuario> findAll();

	public void create(Usuario us);

	public Usuario findByCorreo(String email);


}
