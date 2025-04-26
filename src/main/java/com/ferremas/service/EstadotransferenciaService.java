package com.ferremas.service;

import com.ferremas.model.Estadotransferencia;

import java.util.List;
import java.util.Optional;

public interface EstadotransferenciaService {
    List<Estadotransferencia> findAll();
    Optional<Estadotransferencia> findById(Integer id);
    Estadotransferencia save(Estadotransferencia estado);
    void deleteById(Integer id);
}
