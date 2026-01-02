package com.ferremas.service;



import com.ferremas.model.Producto;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


import java.util.Optional;

public interface ProductoService {
    List<Producto> listarTodos();
    Optional<Producto> buscarPorId(Integer id);
    Producto guardar(Producto producto);
    Producto actualizar(Producto producto);
    void eliminarPorId(Integer id);
    public String guardarImagen(MultipartFile imagen) throws IOException;
    List<Producto> buscarPorNombre(String nombre);
}
