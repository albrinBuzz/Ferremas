package com.ferremas.views;

import com.ferremas.model.Categoria;
import com.ferremas.model.Producto;
import com.ferremas.service.CategoriaService;
import com.ferremas.service.ProductoService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Named("tiendaBean")
@ViewScoped
public class TiendaBean {
    // Lista de categorías disponibles (puede ser un mock-up)
    private List<String> categories = Arrays.asList("Herramientas", "Pinturas", "Accesorios", "Seguridad");
    private String searchQuery;
    // Lista de categorías seleccionadas por el usuario
    private List<String> selectedCategories;
    @Autowired
    private ProductoService productoService;  // Suponiendo que tienes un servicio que maneja los productos.
    @Autowired
    private CategoriaService categoriaService;


    private List<Producto> productos;
    private List<Categoria>listaCategorias;

    private String filtroCategoria;
    private Integer precioMin;
    private Integer precioMax;
    private boolean soloDisponibles;
    // todos los productos
    private List<Producto> productosFiltrados;  // productos con filtros aplicados

    // Filtro de precio
    private int[] selectedPrice = {0, 200}; // Rango de precio por defecto


    @PostConstruct
    public void init() {
        productos = productoService.listarTodos();  // Cargar todos los productos al inicio
        productosFiltrados=productoService.listarTodos();  // Cargar todos los productos al inicio
        listaCategorias=categoriaService.findAll();
    }


    public void verDetalle(Producto producto){

        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("productoSeleccionado", producto);


        try {

            externalContext.redirect(externalContext.getRequestContextPath()
                    + "/home/detalle.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void aplicarFiltros() {
        productosFiltrados = productos.stream()
                .filter(p -> searchQuery == null || p.getNombre().toLowerCase().contains(searchQuery.toLowerCase()))
                .filter(p -> filtroCategoria == null || filtroCategoria.isEmpty() || p.getCategoria().getIdCategoria().toString().equals(filtroCategoria))
                .filter(p -> precioMin == null || p.getPrecio() >= precioMin)
                .filter(p -> precioMax == null || p.getPrecio() <= precioMax)
                //.filter(p -> !soloDisponibles || p.getStock() > 0)
                .collect(Collectors.toList());
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<String> getSelectedCategories() {
        return selectedCategories;
    }

    public void setSelectedCategories(List<String> selectedCategories) {
        this.selectedCategories = selectedCategories;
    }

    public int[] getSelectedPrice() {
        return selectedPrice;
    }

    public void setSelectedPrice(int[] selectedPrice) {
        this.selectedPrice = selectedPrice;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public List<Categoria> getListaCategorias() {
        return listaCategorias;
    }

    public String getFiltroCategoria() {
        return filtroCategoria;
    }

    public void setFiltroCategoria(String filtroCategoria) {
        this.filtroCategoria = filtroCategoria;
    }

    public Integer getPrecioMin() {
        return precioMin;
    }

    public void setPrecioMin(Integer precioMin) {
        this.precioMin = precioMin;
    }

    public Integer getPrecioMax() {
        return precioMax;
    }

    public void setPrecioMax(Integer precioMax) {
        this.precioMax = precioMax;
    }

    public boolean isSoloDisponibles() {
        return soloDisponibles;
    }

    public void setSoloDisponibles(boolean soloDisponibles) {
        this.soloDisponibles = soloDisponibles;
    }

    public List<Producto> getProductosFiltrados() {
        return productosFiltrados;
    }

    public void setProductosFiltrados(List<Producto> productosFiltrados) {
        this.productosFiltrados = productosFiltrados;
    }


    public void addToCart(Producto producto){

    }

    // Método para filtrar productos según las categorías y el rango de precio
    public List<String> filterProducts() {
        // Aquí puedes implementar la lógica de filtrado de productos
        // De acuerdo a las categorías seleccionadas y el rango de precio
        return categories; // Esto es solo un ejemplo, deberías filtrar productos reales.
    }
}
