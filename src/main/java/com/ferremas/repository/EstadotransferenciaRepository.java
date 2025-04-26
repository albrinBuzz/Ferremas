package com.ferremas.repository;



import com.ferremas.model.Estadotransferencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadotransferenciaRepository extends JpaRepository<Estadotransferencia, Integer> {
}
