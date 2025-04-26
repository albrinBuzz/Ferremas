package com.ferremas.views;

import com.ferremas.model.*;
import com.ferremas.service.CategoriaService;
import com.ferremas.service.EmpleadoService;
import com.ferremas.service.InventarioService;
import com.ferremas.service.ProductoService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DialogFrameworkOptions;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named("bodegaBean")
@ViewScoped
public class BodegaBean implements Serializable {

    private List<Inventario> inventarios;
    private Inventario itemSeleccionado;
    private int nuevoStock;
    private boolean mostrarDialogo;
    private Sucursal sucursal;
    private List<Producto>productos;
    @Autowired
    private InventarioService inventarioService;
    @Autowired
    private ProductoService productoService;
    @Autowired
    private CategoriaService categoriaService;
    @Autowired
    private EmpleadoService empleadoService;
    @Autowired
    private HttpSession session;
    private Empleado empleado;
    private int stockProducto;
    private Producto productoSeleccionado;
    private List<Categoria>categorias;
    private String categoria;
    private List<Producto> productosDisponibles;
    // Inyección de otros beans o servicios necesarios

    @PostConstruct
    public void init() {
        // Llamada para cargar los datos del inventario al iniciar el bean
        Usuario usuario= (Usuario) session.getAttribute("usuario");
        categorias=categoriaService.findAll();
        empleado=empleadoService.findById(usuario.getRutUsuario()).get();
        sucursal=empleado.getSucursal();
        productos=new ArrayList<>();
        inventarios = cargarInventario();
        productosDisponibles = productoService.listarTodos();
        setProductos();
        setStock();

    }

    // Métodos getter y setter



    private void setStock(){
        inventarios.forEach(inventario -> inventario.setInventoryStatus(setStatus(inventario.getStock())));
    }

    private InventoryStatus setStatus(int stock){
        if (stock>=10){
            return InventoryStatus.INSTOCK;
        } else if (stock>0 && stock<=9) {
            return InventoryStatus.LOWSTOCK;
        }else {
            return InventoryStatus.OUTOFSTOCK;
        }

    }

    public void openNew() {
        this.productoSeleccionado = new Producto();
    }

    public boolean hasSelectedProducts(){

        return productos != null && !productos.isEmpty();
    }

    public void deleteSelectedProducts(){
        if (productos != null && !productos.isEmpty()) {
            //vueloService.(selectedVuelos);
            cargarInventario();
            setProductos();// Refrescar lista
        }
    }

    private List<Inventario> cargarInventario() {
        return inventarioService.buscarPorSucursal(sucursal.getIdSucursal());
    }
    private void setProductos(){
        inventarios.forEach(inventario -> productos.add(inventario.getProducto()));
    }

    public void saveProduct(){

        var categoria=categoriaService.findById(1);
        productoSeleccionado.setCategoria(categoria.get());
       var producto= productoService.guardar(productoSeleccionado);

        InventarioPK inventarioPK=new InventarioPK();
        inventarioPK.setIdProducto(producto.getIdProducto());
        inventarioPK.setIdSucursal(sucursal.getIdSucursal());
        Inventario inventario=new Inventario();
        inventario.setId(inventarioPK);
        inventario.setSucursal(sucursal);
        inventario.setProducto(producto);
        inventario.setStock(stockProducto);

        inventarioService.guardar(inventario);



    }

    public void onProductChosen(SelectEvent event) {
        Producto product = (Producto) event.getObject();
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Product Selected", "Name:" + product.getNombre());

        FacesContext.getCurrentInstance().addMessage(null, message);
    }


    public void seleccionarProducto(SelectEvent<Producto> event) {
        this.productoSeleccionado = event.getObject();
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Producto seleccionado", productoSeleccionado.getNombre()));
    }


    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getCategoria() {
        return categoria;
    }

    public List<Inventario> getInventarios() {
        return inventarios;
    }

    public Inventario getItemSeleccionado() {
        return itemSeleccionado;
    }

    public void setItemSeleccionado(Inventario itemSeleccionado) {
        this.itemSeleccionado = itemSeleccionado;
    }

    public void setNuevoStock(int nuevoStock) {
        this.nuevoStock = nuevoStock;
    }

    public int getNuevoStock() {
        return nuevoStock;
    }

    public Producto getProductoSeleccionado() {
        return productoSeleccionado;
    }

    public int getStockProducto() {
        return stockProducto;
    }

    public void setStockProducto(int stockProducto) {
        this.stockProducto = stockProducto;
    }

    public void setProductoSeleccionado(Producto productoSeleccionado) {
        this.productoSeleccionado = productoSeleccionado;
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    public Sucursal getSucursal() {
        return sucursal;
    }

    // Acción para editar el stock de un item
    public void editarStock(Inventario item) {
        // Aquí se realiza la lógica para seleccionar un producto y mostrar el dialogo de edición
        this.itemSeleccionado = item;
        this.nuevoStock = item.getStock();  // Por ejemplo, cargamos el stock actual del item
        this.mostrarDialogo = true;
    }

    public List<Producto> getProductosDisponibles() {
        return productosDisponibles;
    }
    public void setProductosDisponibles(List<Producto> productosDisponibles) {
        this.productosDisponibles = productosDisponibles;
    }

    public boolean isMostrarDialogo() {
        return mostrarDialogo;
    }

    public void setMostrarDialogo(boolean mostrarDialogo) {
        this.mostrarDialogo = mostrarDialogo;
    }

    // Acción para actualizar el stock del item
    public void actualizarStock() {
        if (nuevoStock < 0) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El stock no puede ser negativo"));
            return;
        }

        // Actualizamos el stock del item seleccionado
        itemSeleccionado.setStock(nuevoStock);

        // Llamada al servicio para actualizar el stock en la base de datos
        //InventarioService.actualizarStock(itemSeleccionado);

        // Cerrar el diálogo después de guardar
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Stock actualizado correctamente"));

        // Actualizamos el inventario para reflejar el cambio
        inventarios = cargarInventario();

        // Ocultar el diálogo
        mostrarDialogo = false;
    }
}
