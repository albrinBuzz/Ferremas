package com.ferremas.serviceImpl;

import com.ferremas.model.Transferencia;
import com.ferremas.repository.TransferenciaRepository;
import com.ferremas.service.TransferenciaService;
import com.ferremas.util.Logger;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TransferenciaServiceImpl implements TransferenciaService {


    @PersistenceContext
    private EntityManager em;


    @Autowired
    private TransferenciaRepository transferenciaRepository;

    @Override
    public List<Transferencia> findAll() {
        return transferenciaRepository.findAll();
    }

    @Override
    public Optional<Transferencia> findById(Integer id) {
        return transferenciaRepository.findById(id);
    }

    @Override
    public Transferencia save(Transferencia transferencia) {
        return transferenciaRepository.save(transferencia);
    }

    @Override
    public void deleteById(Integer id) {
        transferenciaRepository.deleteById(id);
    }

    @Override
    public List<Transferencia> findByPedidoId(Integer idPedido) {
        return transferenciaRepository.findByPedidoIdPedido(idPedido);
    }

    @Transactional
    @Override
    public List<Transferencia> findBySucursal(Integer idSucursal) {
        List<Transferencia> transferencias;
        Query query = em.createNativeQuery(
                "SELECT * FROM fn_transPorSucursal(:p_id_sucursal)",
                Transferencia.class);
        query.setParameter("p_id_sucursal", idSucursal);


        transferencias= query.getResultList();

        return transferencias;
    }

}
