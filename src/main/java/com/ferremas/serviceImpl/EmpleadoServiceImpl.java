package com.ferremas.serviceImpl;


import com.ferremas.model.Empleado;
import com.ferremas.repository.EmpleadoRepository;
import com.ferremas.service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmpleadoServiceImpl implements EmpleadoService {


    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Override
    public List<Empleado> findAll() {
        return empleadoRepository.findAll();
    }

    @Override
    public Optional<Empleado> findById(String rutUsuario) {
        return empleadoRepository.findById(rutUsuario);
    }

    @Override
    public Empleado save(Empleado employee) {
        return empleadoRepository.save(employee);
    }

    @Override
    public void deleteById(String rutUsuario) {
        empleadoRepository.deleteById(rutUsuario);
    }
}
