package com.ferremas.repository;



import com.ferremas.model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, String> {
    // Puedes agregar métodos personalizados si lo necesitas, por ejemplo:
    // List<Empleado> findBySucursalId(Integer idSucursal);
}
