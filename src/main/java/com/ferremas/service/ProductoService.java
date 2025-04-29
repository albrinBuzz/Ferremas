package com.ferremas.service;


import com.ferremas.Dto.ProductoDTO;
import com.ferremas.model.Producto;
import org.springframework.data.domain.Page;

import java.util.List;


import java.util.Optional;

public interface ProductoService {
    List<Producto> listarTodos();
    Optional<Producto> buscarPorId(Integer id);
    Producto guardar(Producto producto);
    Producto actualizar(Producto producto);
    void eliminarPorId(Integer id);

    public Page<ProductoDTO> getAllProductosPaged(int page, int size, String sortBy, String direction);

    List<Producto> buscarPorNombre(String nombre);
}
