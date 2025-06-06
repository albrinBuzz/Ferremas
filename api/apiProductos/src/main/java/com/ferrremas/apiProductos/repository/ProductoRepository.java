package com.ferrremas.apiProductos.repository;


import com.ferrremas.apiProductos.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    List<Producto> findByNombreContaining(String nombre);

    // Buscar productos por precio mayor o igual a un valor
    List<Producto> findByPrecioGreaterThanEqual(Integer precio);
    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    // Buscar productos por categoria
    List<Producto> findByCategoriaNombre(String categoriaNombre);
}