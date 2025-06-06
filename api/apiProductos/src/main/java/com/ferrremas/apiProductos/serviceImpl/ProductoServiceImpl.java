package com.ferrremas.apiProductos.serviceImpl;


import com.ferrremas.apiProductos.model.Producto;
import com.ferrremas.apiProductos.repository.ProductoRepository;
import com.ferrremas.apiProductos.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    @Override
    public Optional<Producto> buscarPorId(Integer id) {
        return productoRepository.findById(id);
    }

    @Override
    public Producto guardar(Producto producto) {



        return productoRepository.save(producto);
    }

    @Override
    public Producto actualizar(Producto producto) {
        if (producto.getIdProducto() == null || !productoRepository.existsById(producto.getIdProducto())) {
            throw new IllegalArgumentException("No se puede actualizar un producto inexistente.");
        }
        return productoRepository.save(producto);
    }

    @Override
    public void eliminarPorId(Integer id) {
        if (!productoRepository.existsById(id)) {
            throw new IllegalArgumentException("No se puede eliminar un producto inexistente.");
        }
        productoRepository.deleteById(id);
    }

    @Override
    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }


}
