package com.ferremas.service;

import com.ferremas.model.Empleado;

import java.util.List;
import java.util.Optional;

public interface EmpleadoService {

    List<Empleado> findAll();

    Optional<Empleado> findById(String rutUsuario);

    Empleado save(Empleado employee);

    void deleteById(String rutUsuario);
}
