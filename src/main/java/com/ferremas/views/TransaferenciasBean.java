package com.ferremas.views;

import com.ferremas.model.*;
import com.ferremas.service.*;
import com.ferremas.util.Logger;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import org.primefaces.model.file.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Named("transferenciasBean")
@ViewScoped
public class TransaferenciasBean implements Serializable {


    private List<Transferencia> transferencias;
    private Transferencia transferenciaSeleccionada;
    private List<Pedido>pedidos;
    private List<Estadopedido> estados;
    private List<Estadotransferencia>estadosTransa;
    private Estadopedido estadoSeleccionado;
    private Estadotransferencia estdtransaSeleccionado;
    private Integer estadoSeleccionadoID;
    private Integer estadoTransaID;
    private Pedido pedidoSeleccionado;
    @Autowired
    private TransferenciaService transferenciaService;

    @Autowired
    private EstadotransferenciaService estadotransferenciaService;
    @Autowired
    private EmpleadoService empleadoService;
    @Autowired
    private EstadopedidoService estadopedidoService;
    @Autowired
    private PedidoService pedidoService;
    @Inject
    private CarritoBean carritoBean;
    @Autowired
    private HttpSession session;

    @PostConstruct
    public void init(){
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario != null) {
            var empleado = empleadoService.findById(usuario.getRutUsuario()).orElse(null);

            assert empleado != null;
            //transferencias=transferenciaService.findBySucursal(empleado.getSucursal().getIdSucursal());
            var pedidos=pedidoService.obtenerPedidosPorSucursal(empleado.getSucursal().getIdSucursal());

            this.pedidos= pedidos.stream()
                    .filter(pedido -> !pedido.getTransferencias().isEmpty())
                    .toList();

        }



    }

    public void editarEstado(Pedido pedido) {
        this.pedidoSeleccionado = pedido;
        this.estadoSeleccionado = pedido.getEstadopedido();
        //this.estadoSeleccionadoID = pedido.getEstadopedido().getIdEstadopedido(); // importante
        this.estados = estadopedidoService.findAll(); // 🔥 cargar estados aquí
        Logger.logInfo("➡️ Método editarEstado ejecutado para pedido: " + pedidoSeleccionado.getIdPedido());
    }


    public void editarEstadoTransa(Transferencia transferencia) {
        this.transferenciaSeleccionada=transferencia;
        this.estdtransaSeleccionado=transferencia.getEstadotransferencia();
        //this.estadoSeleccionadoID = pedido.getEstadopedido().getIdEstadopedido(); // importante
        this.estadosTransa=estadotransferenciaService.findAll();
        Logger.logInfo("➡️ Método editarEstadoTransa ejecutado para transa: " + transferencia.getIdtransferencia());
    }

    public void guardarEstadoEstadoPedio() {

        if (estadoSeleccionadoID == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debe seleccionar un estado válido."));
            return;
        }
        estadoSeleccionado=estadopedidoService.findById(estadoSeleccionadoID).get();
        if (pedidoSeleccionado != null && estadoSeleccionado != null) {
            pedidoSeleccionado.setEstadopedido(estadoSeleccionado);

            pedidoService.guardar(pedidoSeleccionado);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Estado actualizado", "El pedido fue actualizado correctamente"));

        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo actualizar el estado"));
        }

    }

    public void guardarEstadoEstadoTransa(){
        if (estadoTransaID == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debe seleccionar un estado válido."));
            return;
        }

        estdtransaSeleccionado=estadotransferenciaService.findById(estadoTransaID).get();

        if (transferenciaSeleccionada != null) {
            transferenciaSeleccionada.setEstadotransferencia(estdtransaSeleccionado);

            transferenciaService.save(transferenciaSeleccionada);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Transaferencia actualizada", "La transaferencia fue actualizado correctamente"));

        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo actualizar el estado"));
        }

    }

    public void verComprobante(Transferencia trn) {

        this.transferenciaSeleccionada=trn;
    }

    public List<Transferencia> getTransferencias() {
        return transferencias;
    }

    public void setTransferencias(List<Transferencia> transferencias) {
        this.transferencias = transferencias;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    public Transferencia getTransferenciaSeleccionada() {
        return transferenciaSeleccionada;
    }

    public void setTransferenciaSeleccionada(Transferencia transferenciaSeleccionada) {
        this.transferenciaSeleccionada = transferenciaSeleccionada;
    }


    public Pedido getPedidoSeleccionado() {
        return pedidoSeleccionado;
    }

    public void setPedidoSeleccionado(Pedido pedidoSeleccionado) {
        this.pedidoSeleccionado = pedidoSeleccionado;
    }

    public Estadopedido getEstadoSeleccionado() {
        return estadoSeleccionado;
    }

    public void setEstadoSeleccionado(Estadopedido estadoSeleccionado) {
        this.estadoSeleccionado = estadoSeleccionado;
    }

    public Integer getEstadoSeleccionadoID() {
        return estadoSeleccionadoID;
    }

    public void setEstadoSeleccionadoID(Integer estadoSeleccionadoID) {
        this.estadoSeleccionadoID = estadoSeleccionadoID;
    }

    public List<Estadopedido> getEstados() {
        if (estados == null || estados.isEmpty()) {
            Logger.logInfo("Cargando estados de pedido...");
            estados = estadopedidoService.findAll();
        }
        return estados;
    }

    public List<Estadotransferencia> getEstadosTransa() {
        return estadosTransa;
    }

    public void setEstadoTransaID(Integer estadoTransaID) {
        this.estadoTransaID = estadoTransaID;
    }

    public Integer getEstadoTransaID() {
        return estadoTransaID;
    }

    public void setEstados(List<Estadopedido> estados) {
        this.estados = estados;
    }



}

