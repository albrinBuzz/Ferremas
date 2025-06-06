package com.ferrremas.apiProductos.repository;



import com.ferrremas.apiProductos.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    // Puedes agregar métodos personalizados si lo necesitas
    // Ejemplo: List<Categoria> findByNombreContaining(String nombre);
}
