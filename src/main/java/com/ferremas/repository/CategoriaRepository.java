package com.ferremas.repository;


import com.ferremas.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    // Puedes agregar métodos personalizados si lo necesitas
    // Ejemplo: List<Categoria> findByNombreContaining(String nombre);
}
