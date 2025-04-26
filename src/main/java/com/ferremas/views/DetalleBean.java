package com.ferremas.views;

import com.ferremas.model.Inventario;
import com.ferremas.model.Producto;
import com.ferremas.service.InventarioService;
import com.ferremas.service.ProductoService;
import com.ferremas.util.Logger;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@ViewScoped
@Named("detalleBean")
public class DetalleBean {

    private Producto producto;
    private int productId;

    @Autowired
    ProductoService productoService;
    private List<Producto> cartItems;
    @Autowired
    private HttpSession session;

    @Autowired
    private InventarioService inventarioService;

    private List<Inventario>inventarios;

    @PostConstruct
    public void init() {


        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();

        // Obtener el vuelo seleccionado desde la sesión
        //producto= (Producto) externalContext.getSessionMap().get("productoSeleccionado");
        productId = Integer.parseInt(facesContext.getExternalContext().getRequestParameterMap().get("productId"));
        producto=productoService.buscarPorId(productId).get();
        inventarios=inventarioService.buscarPorProducto(productId);
        Logger.logInfo("ProductoDetalle: "+producto);
        cartItems = new ArrayList<>();

    }


    public void addToCart(Producto producto){
        Logger.logInfo("Producto-> :"+producto.toString());
    }


    public List<Inventario> getInventarios() {
        return inventarios;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}
