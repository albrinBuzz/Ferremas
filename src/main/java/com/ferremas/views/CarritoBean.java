package com.ferremas.views;

import com.ferremas.model.Detallepedido;
import com.ferremas.model.Pedido;
import com.ferremas.model.Producto;
import com.ferremas.util.Logger;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Named("carroBean")
@SessionScoped
//@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
//@Component("carroBean")
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)

public class CarritoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Pedido pedido;

    @Autowired
    private HttpSession session;

    // Inicialización de los productos en el carrito (recuperarlos de la sesión)
    @PostConstruct
    public void init() {


        /*if ( session.getAttribute("pedido")!=null){
            pedido = (Pedido) session.getAttribute("pedido");
        }else {
            pedido=new Pedido();
            session.setAttribute("pedido",pedido);
        }*/

        pedido=new Pedido();
        Logger.logInfo("Instanciando el carro y limpiando los datos previos");
    }

    // Método para agregar un producto al carrito
    public void agregarItem(Producto product) {
        Logger.logInfo("Producto a agregar: "+product);
        Detallepedido dt=new Detallepedido(1, product.getPrecio(), product);
        pedido.addDetallepedido(dt,1, product.getIdProducto());

    }

    // Método para eliminar un producto del carrito
    public void removeItemFromCart(Producto product) {
        //cartItems.remove(product);
        //this.pedido.e
        //session.setAttribute("listaProductos", cartItems);
    }

    public void decreamentarCantidad(Producto producto){
        Logger.logInfo("Decrementando la cantidad");
        this.pedido.decrementarCantidad(producto.getIdProducto());
    }

    public void incrementarCantidad(Producto producto){
        Logger.logInfo("Incrementando la cantidad");
        this.pedido.incrementarCantidad(producto.getIdProducto());
    }

    // Método para obtener la cantidad de artículos en el carrito
    public int getItemCount() {
        return pedido.getDetallepedidos().size();
    }

    // Método para obtener el total del carrito
    public double getTotal() {
        /*double total = 0.0;
        for (Producto product : cartItems) {
            total += product.getPrecio();
        }*/

        return    pedido.getTotal();
    }

    // Getters y setters
    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }
    public void resetCart() {
        // Limpiar el carrito
        this.pedido = new Pedido();

        Logger.logInfo("Carrito reiniciado");
    }

}
