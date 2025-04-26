package com.ferremas.serviceImpl;


import com.ferremas.model.Pedido;
import com.ferremas.model.Sucursal;
import com.ferremas.repository.PedidoRepository;
import com.ferremas.service.PedidoService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoServiceImpl implements PedidoService {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Override
    public Pedido guardar(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    @Override
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    @Override
    public Pedido obtenerPorId(Integer id) {
        return pedidoRepository.findById(id).orElse(null);
    }

    @Override
    public void eliminar(Integer id) {
        pedidoRepository.deleteById(id);
    }

    public List<Pedido> obtenerPedidosPorSucursal(int idSucursal) {
        return em.createQuery("SELECT p FROM Pedido p WHERE p.sucursal.idSucursal = :idSucursal", Pedido.class)
                .setParameter("idSucursal", idSucursal)
                .getResultList();
    }
}
