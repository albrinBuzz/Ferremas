package com.ferremas.service;


import com.ferremas.model.Transaccion;

import java.util.List;

public interface TransaccionService {
    Transaccion guardar(Transaccion transaccion);
    List<Transaccion> listarTodos();
    Transaccion obtenerPorId(Integer id);
    void eliminar(Integer id);
}
