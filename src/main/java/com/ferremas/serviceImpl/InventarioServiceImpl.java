package com.ferremas.serviceImpl;


import com.ferremas.model.Inventario;
import com.ferremas.model.InventarioPK;
import com.ferremas.model.Producto;
import com.ferremas.repository.InventarioRepository;
import com.ferremas.service.InventarioService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class InventarioServiceImpl implements InventarioService {

    private final InventarioRepository inventarioRepository;


    @PersistenceContext
    private EntityManager em;

    @Autowired
    public InventarioServiceImpl(InventarioRepository inventarioRepository) {
        this.inventarioRepository = inventarioRepository;
    }

    @Override
    public List<Inventario> listarTodos() {
        return inventarioRepository.findAll();
    }

    @Override
    public Optional<Inventario> buscarPorId(InventarioPK id) {
        return inventarioRepository.findById(id);
    }

    @Override
    @Transactional
    public Inventario guardar(Inventario inventario) {
        StoredProcedureQuery query = em.createStoredProcedureQuery("sp_save_invt");

        query.registerStoredProcedureParameter("p_id_producto", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_id_sucursal", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_stock", Integer.class, ParameterMode.IN);

        query.setParameter("p_id_producto", inventario.getProducto().getIdProducto());
        query.setParameter("p_id_sucursal", inventario.getSucursal().getIdSucursal());
        query.setParameter("p_stock", inventario.getStock());

        query.execute(); // Transacción controlada por Spring

        return inventario;
    }


    @Override
    public Inventario actualizar(Inventario inventario) {
        return inventarioRepository.save(inventario);
    }

    @Override
    public void eliminarPorId(InventarioPK id) {
        inventarioRepository.deleteById(id);
    }

    @Override
    public List<Inventario> buscarPorProducto(Integer idProducto) {
        return inventarioRepository.findByProductoIdProducto(idProducto);
    }

    @Override
    public List<Inventario> buscarPorProductoIn(List<Producto> productos) {
        return inventarioRepository.buscarPorProductoIn(productos);
    }


    @Override
    public List<Inventario> buscarPorSucursal(Integer idSucursal) {
        return inventarioRepository.findBySucursalIdSucursal(idSucursal);
    }
}
