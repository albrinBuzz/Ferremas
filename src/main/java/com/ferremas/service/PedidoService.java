package com.ferremas.service;



import com.ferremas.model.Pedido;
import com.ferremas.model.Sucursal;

import java.util.List;

public interface PedidoService {
    Pedido guardar(Pedido pedido);
    List<Pedido> listarTodos();
    Pedido obtenerPorId(Integer id);
    void eliminar(Integer id);
    public List<Pedido> obtenerPedidosPorSucursal(int idSucursal);
}
