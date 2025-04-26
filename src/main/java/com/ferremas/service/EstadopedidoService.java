package com.ferremas.service;


import com.ferremas.model.Estadopedido;
import com.ferremas.repository.EstadopedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface EstadopedidoService {


    public List<Estadopedido> findAll();

    public Optional<Estadopedido> findById(Integer id);

    public Estadopedido save(Estadopedido estadopedido);

    public void deleteById(Integer id);

}
