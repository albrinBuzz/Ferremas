package com.ferremas.serviceImpl;


import com.ferremas.model.Metodopago;
import com.ferremas.repository.MetodopagoRepository;
import com.ferremas.service.MetodopagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MetodopagoServiceImpl implements MetodopagoService {

    @Autowired
    private MetodopagoRepository metodoPagoRepository;

    @Override
    public Metodopago guardar(Metodopago metodo) {
        return metodoPagoRepository.save(metodo);
    }

    @Override
    public List<Metodopago> listarTodos() {
        return metodoPagoRepository.findAll();
    }

    @Override
    public Metodopago obtenerPorId(Integer id) {
        return metodoPagoRepository.findById(id).orElse(null);
    }

    @Override
    public void eliminar(Integer id) {
        metodoPagoRepository.deleteById(id);
    }

    @Override
    public Metodopago obtenerPorNombre(String nombre) {
        return metodoPagoRepository.findByNombre(nombre);
    }
}
