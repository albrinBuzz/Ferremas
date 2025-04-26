package com.ferremas.service;

import com.ferremas.model.Rol;

import java.util.List;
import java.util.Optional;

public interface RolService {

    List<Rol> findAll();
    Optional<Rol> findById(Integer id);
    Rol save(Rol rol);
    void deleteById(Integer id);
    Rol findByNombre(String nombre);
}
