package com.ferremas.views;

import com.ferremas.service.ReporteService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.pie.PieChartModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;


@Named("dashboardBean")
@ViewScoped
public class DashboardBean implements Serializable {

    private Date fechaInicio;
    private Date fechaFin;

    private BarChartModel ventasPorSucursal;
    private PieChartModel ventasPorMetodoPago;
    /*private List<ProductoReporte> productosMasVendidos;
    private List<InventarioReporte> inventario;
    private List<EstadoPedidoReporte> pedidosPorEstado;
    private List<ClienteReporte> clientesNuevosPorMes;
    private List<TransferenciaReporte> transferenciasPendientes;*/

    @Autowired
    private ReporteService reporteService;

    @PostConstruct
    public void init() {
        //fechaInicio = LocalDate.now().minusMonths(1);
        //fechaFin = LocalDate.now();
        generarInformes();
    }

    public void generarInformes() {
        ventasPorSucursal = reporteService.obtenerVentasPorSucursal(fechaInicio, fechaFin);
        /*ventasPorMetodoPago = reporteService.obtenerVentasPorMetodoPago(fechaInicio, fechaFin);
        productosMasVendidos = reporteService.obtenerProductosMasVendidos(fechaInicio, fechaFin);
        inventario = reporteService.obtenerInventarioActual();
        pedidosPorEstado = reporteService.obtenerPedidosPorEstado();
        clientesNuevosPorMes = reporteService.obtenerClientesPorMes();
        transferenciasPendientes = reporteService.obtenerTransferenciasPendientes();*/
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public BarChartModel getVentasPorSucursal() {
        return ventasPorSucursal;
    }

    public void setVentasPorSucursal(BarChartModel ventasPorSucursal) {
        this.ventasPorSucursal = ventasPorSucursal;
    }

    public PieChartModel getVentasPorMetodoPago() {
        return ventasPorMetodoPago;
    }

    public void setVentasPorMetodoPago(PieChartModel ventasPorMetodoPago) {
        this.ventasPorMetodoPago = ventasPorMetodoPago;
    }
    // Getters y Setters...
}
