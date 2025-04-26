package com.ferremas.service;



import com.ferremas.model.Inventario;
import com.ferremas.model.InventarioPK;
import com.ferremas.model.Producto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InventarioService {
    List<Inventario> listarTodos();
    Optional<Inventario> buscarPorId(InventarioPK id);
    Inventario guardar(Inventario inventario);
    Inventario actualizar(Inventario inventario);
    void eliminarPorId(InventarioPK id);

    List<Inventario> buscarPorProducto(Integer idProducto);
    List<Inventario> buscarPorProductoIn(List<Producto> productos);
    List<Inventario> buscarPorSucursal(Integer idSucursal);

}
