package com.ferremas.service;


import com.ferremas.model.Producto;

import java.util.List;


import java.util.Optional;

public interface ProductoService {
    List<Producto> listarTodos();
    Optional<Producto> buscarPorId(Integer id);
    Producto guardar(Producto producto);
    Producto actualizar(Producto producto);
    void eliminarPorId(Integer id);

}
