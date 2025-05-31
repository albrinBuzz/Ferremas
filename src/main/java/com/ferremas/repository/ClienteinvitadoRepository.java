package com.ferremas.repository;


import com.ferremas.model.Clienteinvitado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteinvitadoRepository extends JpaRepository<Clienteinvitado, String> {
    // Aquí puedes agregar métodos personalizados si lo necesitas
}
