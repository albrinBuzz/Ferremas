package com.ferremas.service;


import com.ferremas.model.Transferencia;

import java.util.List;
import java.util.Optional;

public interface TransferenciaService {
    List<Transferencia> findAll();
    Optional<Transferencia> findById(Integer id);
    Transferencia save(Transferencia transferencia);
    void deleteById(Integer id);
    List<Transferencia> findByPedidoId(Integer idPedido);
    List<Transferencia>findBySucursal(Integer idSucursal);
}
