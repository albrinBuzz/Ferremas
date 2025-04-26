package com.ferremas.repository;


import com.ferremas.model.Metodopago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetodopagoRepository extends JpaRepository<Metodopago, Integer> {
    // Puedes agregar métodos personalizados si necesitas, por ejemplo:
    Metodopago findByNombre(String nombre);
}
