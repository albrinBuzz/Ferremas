package com.ferremas.service;

import com.ferremas.model.Metodopago;

import java.util.List;

public interface MetodopagoService {
    Metodopago guardar(Metodopago metodo);
    List<Metodopago> listarTodos();
    Metodopago obtenerPorId(Integer id);
    void eliminar(Integer id);
    Metodopago obtenerPorNombre(String nombre);
}
