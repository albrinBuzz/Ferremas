package com.ferremas.repository;



import com.ferremas.model.Inventario;
import com.ferremas.model.Pedido;
import com.ferremas.model.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    @Query("select p from Pedido p where p.rutcliente=:rut and p.idPedido=:idPedido")
    Pedido buscarIdRut(String rut, Integer idPedido);
}
