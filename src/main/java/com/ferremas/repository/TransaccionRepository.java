package com.ferremas.repository;


import com.ferremas.model.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion, Integer> {
    // Métodos adicionales si son necesarios
}
