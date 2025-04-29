package com.ferremas.views;

import com.ferremas.model.Detallepedido;
import com.ferremas.model.Pedido;
import com.ferremas.service.PedidoService;
import com.ferremas.util.Logger;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

@Named("miPedidoBean")
@ViewScoped
public class MiPedidoBean implements Serializable {

    private Integer idPedido;
    private String rutCliente;
    private Pedido pedido;
    @Autowired
    private PedidoService pedidoService;
    // Constructor que inicializa el pedido (o puedes inyectarlo desde un servicio)
    /*public MiPedidoBean() {
        pedido = new Pedido(); // En este caso, crea un pedido vacío.
    }*/


    // Métodos adicionales según necesites, como obtener un pedido de la base de datos.
    public void cargarPedido() {
        Logger.logInfo("Rut cliente: "+rutCliente);

        Logger.logInfo("Id Pedido: "+idPedido);
        pedido=pedidoService.buscarIdRut(rutCliente,idPedido);

        if (pedido==null){
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Pedido nulo", "El pedido no fue encontrado"));
        }
        //Logger.logInfo(pedido.toString());
        // Aquí deberías cargar el pedido desde la base de datos según el idPedido.
        // Por ejemplo:
        // this.pedido = pedidoService.obtenerPedido(idPedido);
    }

    // Método para mostrar detalles del pedido en la vista JSF
    public void imprimirDetalles() {
        for (Detallepedido detalle : pedido.getDetallepedidos()) {
            System.out.println(detalle);
        }
    }

    public Integer getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Integer idPedido) {
        this.idPedido = idPedido;
    }

    public String getRutCliente() {
        return rutCliente;
    }

    public void setRutCliente(String rutCliente) {
        this.rutCliente = rutCliente;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }


}