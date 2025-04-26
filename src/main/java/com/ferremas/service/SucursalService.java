package com.ferremas.service;

import com.ferremas.model.Sucursal;

import java.util.List;
import java.util.Optional;

public interface SucursalService {
    List<Sucursal> findAll();
    Optional<Sucursal> findById(Integer id);
    Sucursal save(Sucursal sucursal);
    void deleteById(Integer id);
}
