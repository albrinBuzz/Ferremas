package com.ferremas.serviceImpl;

import com.ferremas.model.Transaccion;
import com.ferremas.repository.TransaccionRepository;
import com.ferremas.service.TransaccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransaccionServiceImpl implements TransaccionService {

    @Autowired
    private TransaccionRepository transaccionRepository;

    @Override
    public Transaccion guardar(Transaccion transaccion) {
        return transaccionRepository.save(transaccion);
    }

    @Override
    public List<Transaccion> listarTodos() {
        return transaccionRepository.findAll();
    }

    @Override
    public Transaccion obtenerPorId(Integer id) {
        return transaccionRepository.findById(id).orElse(null);
    }

    @Override
    public void eliminar(Integer id) {
        transaccionRepository.deleteById(id);
    }
}
