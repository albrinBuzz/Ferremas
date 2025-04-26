package com.ferremas.serviceImpl;

import com.ferremas.model.Sucursal;
import com.ferremas.repository.SucursalRepository;
import com.ferremas.service.SucursalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SucursalServiceImpl implements SucursalService {

    private final SucursalRepository sucursalRepository;

    @Autowired
    public SucursalServiceImpl(SucursalRepository sucursalRepository) {
        this.sucursalRepository = sucursalRepository;
    }

    @Override
    public List<Sucursal> findAll() {
        return sucursalRepository.findAll();
    }

    @Override
    public Optional<Sucursal> findById(Integer id) {
        return sucursalRepository.findById(id);
    }

    @Override
    public Sucursal save(Sucursal sucursal) {
        return sucursalRepository.save(sucursal);
    }

    @Override
    public void deleteById(Integer id) {
        sucursalRepository.deleteById(id);
    }
}
