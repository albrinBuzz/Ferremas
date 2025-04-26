package com.ferremas.serviceImpl;

import com.ferremas.model.Estadopedido;
import com.ferremas.repository.EstadopedidoRepository;
import com.ferremas.service.EstadopedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EstadopedidoServiceImpl implements EstadopedidoService {

    @Autowired
    private EstadopedidoRepository estadopedidoRepository;

    @Override
    public List<Estadopedido> findAll() {
        return estadopedidoRepository.findAll();
    }

    @Override
    public Optional<Estadopedido> findById(Integer id) {
        return estadopedidoRepository.findById(id);
    }

    @Override
    public Estadopedido save(Estadopedido estadopedido) {
        return estadopedidoRepository.save(estadopedido);
    }

    @Override
    public void deleteById(Integer id) {
        estadopedidoRepository.deleteById(id);
    }
}
