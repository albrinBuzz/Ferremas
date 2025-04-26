package com.ferremas.serviceImpl;

import com.ferremas.model.Rol;
import com.ferremas.repository.RolRepository;
import com.ferremas.service.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RolServiceImpl implements RolService {

    private final RolRepository rolRepository;

    @Autowired
    public RolServiceImpl(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @Override
    public List<Rol> findAll() {
        return rolRepository.findAll();
    }

    @Override
    public Optional<Rol> findById(Integer id) {
        return rolRepository.findById(id);
    }

    @Override
    public Rol save(Rol rol) {
        return rolRepository.save(rol);
    }

    @Override
    public void deleteById(Integer id) {
        rolRepository.deleteById(id);
    }

    @Override
    public Rol findByNombre(String nombre) {
        return rolRepository.findByNombre(nombre);
    }
}
