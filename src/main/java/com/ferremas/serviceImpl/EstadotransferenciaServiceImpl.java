package com.ferremas.serviceImpl;


import com.ferremas.model.Estadotransferencia;
import com.ferremas.repository.EstadotransferenciaRepository;
import com.ferremas.service.EstadotransferenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EstadotransferenciaServiceImpl implements EstadotransferenciaService {

    @Autowired
    private EstadotransferenciaRepository estadotransferenciaRepository;

    @Override
    public List<Estadotransferencia> findAll() {
        return estadotransferenciaRepository.findAll();
    }

    @Override
    public Optional<Estadotransferencia> findById(Integer id) {
        return estadotransferenciaRepository.findById(id);
    }

    @Override
    public Estadotransferencia save(Estadotransferencia estado) {
        return estadotransferenciaRepository.save(estado);
    }

    @Override
    public void deleteById(Integer id) {
        estadotransferenciaRepository.deleteById(id);
    }
}
