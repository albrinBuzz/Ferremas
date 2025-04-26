package com.ferremas.repository;


import com.ferremas.model.Estadopedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadopedidoRepository extends JpaRepository<Estadopedido, Integer> {
    // Métodos adicionales si es necesario
}
