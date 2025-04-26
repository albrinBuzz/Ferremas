package com.ferremas.repository;

import com.ferremas.model.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SucursalRepository extends JpaRepository<Sucursal, Integer> {
    // Puedes agregar consultas personalizadas si lo necesitas
}
