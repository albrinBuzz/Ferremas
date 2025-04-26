package com.ferremas.repository;


import com.ferremas.model.Inventario;
import com.ferremas.model.InventarioPK;
import com.ferremas.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, InventarioPK> {
    List<Inventario> findByProductoIdProducto(Integer idProducto);
    List<Inventario> findBySucursalIdSucursal(Integer idSucursal);
    @Query("SELECT i FROM Inventario i WHERE i.producto IN :productos")
    List<Inventario> buscarPorProductoIn(List<Producto> productos);
}
