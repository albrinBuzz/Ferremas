package com.ferremas.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.primefaces.model.charts.bar.BarChartModel;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ReporteService {

    @PersistenceContext
    private EntityManager em;

    public BarChartModel obtenerVentasPorSucursal(Date inicio, Date fin) {
        List<Object[]> resultados = em.createQuery(
                        "SELECT s.nombre, SUM(p.total) FROM Pedido p JOIN p.sucursal s WHERE p.fecha BETWEEN :inicio AND :fin GROUP BY s.nombre", Object[].class)
                .setParameter("inicio", inicio)
                .setParameter("fin", fin)
                .getResultList();

        BarChartModel model = new BarChartModel();
        /*ChartSeries series = new ChartSeries();
        series.setLabel("Ventas");

        for (Object[] fila : resultados) {
            series.set((String) fila[0], ((Number) fila[1]).intValue());
        }

        model.addSeries(series);*/
        return model;
    }

    // Otros métodos similares: obtenerVentasPorMetodoPago, obtenerProductosMasVendidos, etc.
}
