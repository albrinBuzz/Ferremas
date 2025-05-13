package com.ferremas.views;

import com.ferremas.model.*;
import com.ferremas.service.*;
import com.ferremas.util.Logger;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.Serializable;
import java.time.temporal.ValueRange;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Named("pedidoBean")
@ViewScoped
public class PedidoBean implements Serializable {

    private List<Pedido> pedidos;
    private List<Pedido>pedidosHist;
    private List<Pedido> filteredPedidos;
    private Pedido pedidoSeleccionado;
    private List<Detallepedido> detallesPedido;

    private Sucursal sucursal;
    private Estadopedido estadoFiltro;
    private Estadopedido estadoSeleccionado;
    private Integer estadoSeleccionadoID;
    private List<Estadopedido> estados;

    @Autowired
    private PedidoService pedidoService;
    @Autowired
    private ClienteinvitadoService clienteinvitadoService;
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EmpleadoService empleadoService;
    @Autowired
    private EstadopedidoService estadopedidoService;
    @Autowired
    private HttpSession session;

    @PostConstruct
    public void init() {
        // Obtener usuario desde la sesión
        Optional<Usuario> usuarioOpt = Optional.ofNullable((Usuario) session.getAttribute("usuario"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var roles = authentication.getAuthorities().stream().toList();

        usuarioOpt.ifPresent(usuario -> {
            var empleado = empleadoService.findById(usuario.getRutUsuario()).orElse(null);
            if (empleado != null) {
                sucursal = empleado.getSucursal();
            }
            pedidosHist = pedidoService.obtenerPorCliente(usuario.getRutUsuario());
        });

        if (sucursal != null) {
            pedidos = pedidoService.obtenerPedidosPorSucursal(sucursal.getIdSucursal());
        } else {
            usuarioOpt.ifPresent(usuario -> pedidosHist = pedidoService.obtenerPorCliente(usuario.getRutUsuario()));
        }

        estados = estadopedidoService.findAll();
    }


    // === ACCIONES ===

    public void editarEstado(Pedido pedido) {
        this.pedidoSeleccionado = pedido;
        this.estadoSeleccionado = pedido.getEstadopedido();
        Logger.logInfo("➡️ Método editarEstado ejecutado para pedido: " + pedidoSeleccionado.getIdPedido());
    }

    public void verDetallesPedido(Pedido pedido) {
        Logger.logInfo("Viendo los datalles del pedido");
        this.pedidoSeleccionado = pedido;
        //this.detallesPedido = pedidoService.obtenerDetallesPorPedido(pedido.getIdPedido()); // Debes tener este método en PedidoService
        this.detallesPedido = pedido.getDetallepedidos();
    }

    public void eliminarPedido(Pedido pedido){
        pedidoService.eliminar(pedido.getIdPedido());
    }


    public String getCorreoCliente(String rut) {

        // Variable para almacenar los correos
        StringBuilder correos = new StringBuilder();

        // Buscar cliente por RUT (Usuario)
        var cliente = usuarioService.findByRut(rut);
        cliente.ifPresent(usuario -> correos.append(usuario.getCorreo()));

        // Buscar cliente invitado por RUT
        var clienteInvitado = clienteinvitadoService.obtenerClientePorRut(rut);
        clienteInvitado.ifPresent(invitado -> correos.append(invitado.getCorreo()));

        // Retornar los correos concatenados
        return correos.toString();
    }


    public void guardarEstado() {
        Logger.logInfo("✅ guardarEstado ejecutado");
        Logger.logInfo("Pedido seleccionado: " + pedidoSeleccionado.getEstadopedido().getNombre());
        estadoSeleccionado=estadopedidoService.findById(estadoSeleccionadoID).get();
        Logger.logInfo("Estado seleccionado: " + estadoSeleccionado);



        if (pedidoSeleccionado != null && estadoSeleccionado != null) {
            pedidoSeleccionado.setEstadopedido(estadoSeleccionado);
            pedidoService.guardar(pedidoSeleccionado); // asegúrate que este método guarda en BD
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Estado actualizado", "El pedido fue actualizado correctamente"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo actualizar el estado"));
        }
    }

    public String obtenerEstiloFila(Pedido pedido) {
        if (pedido != null && pedido.getEstadopedido() != null) {
            String estado = pedido.getEstadopedido().getNombre();

            if ("Pendiente".equals(estado)) {
                return "estado-pendiente";
            } else if ("En Proceso".equals(estado)) {
                return "estado-en-proceso";
            } else if ("Completado".equals(estado)) {
                return "estado-completado";
            }
        }
        return "";
    }


    // === GETTERS & SETTERS ===


    public Pedido getPedidoSeleccionado() {
        return pedidoSeleccionado;
    }

    public void setPedidoSeleccionado(Pedido pedidoSeleccionado) {
        this.pedidoSeleccionado = pedidoSeleccionado;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    public List<Pedido> getFilteredPedidos() {
        return filteredPedidos;
    }

    public void setFilteredPedidos(List<Pedido> filteredPedidos) {
        this.filteredPedidos = filteredPedidos;
    }

    public List<Detallepedido> getDetallesPedido() {
        return detallesPedido;
    }

    public void setDetallesPedido(List<Detallepedido> detallesPedido) {
        this.detallesPedido = detallesPedido;
    }

    public Sucursal getSucursal() {
        return sucursal;
    }

    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
    }


    public void setEstadoFiltro(Estadopedido estadoFiltro) {
        this.estadoFiltro = estadoFiltro;
    }

    public Estadopedido getEstadoFiltro() {
        return estadoFiltro;
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
        return estados;
    }

    public void setEstados(List<Estadopedido> estados) {
        this.estados = estados;
    }


    public List<Pedido> getPedidosHist() {
        return pedidosHist;
    }
}
