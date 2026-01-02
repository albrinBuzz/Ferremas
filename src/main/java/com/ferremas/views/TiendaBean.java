package com.ferremas.views;

import com.ferremas.model.Categoria;
import com.ferremas.model.Producto;
import com.ferremas.service.CategoriaService;
import com.ferremas.service.ProductoService;
import com.ferremas.util.Logger;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import org.primefaces.model.ResponsiveOption;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Named("tiendaBean")
@ViewScoped
public class TiendaBean implements java.io.Serializable {

    private String searchQuery;
    private String filtroCategoria;
    private Integer precioMin = 0;
    private Integer precioMax = 1000000; // Valor por defecto alto
    private String orden = "desc"; // price_asc, price_desc, desc
    private String vista = "grid";

    @Autowired
    private ProductoService productoService;
    @Autowired
    private CategoriaService categoriaService;

    private List<Producto> productos;
    private List<Categoria> listaCategorias;
    private List<Producto> productosFiltrados;
    private double valorDolar;

    private List<org.primefaces.model.ResponsiveOption> responsiveOptions;

    @PostConstruct
    public void init() {
        productos = productoService.listarTodos();  // Cargar todos los productos al inicio
        productosFiltrados=productoService.listarTodos();  // Cargar todos los productos al inicio
        listaCategorias=categoriaService.findAll();
        responsiveOptions = new ArrayList<>();

        responsiveOptions.add(new org.primefaces.model.ResponsiveOption("1024px", 4, 1));
        responsiveOptions.add(new org.primefaces.model.ResponsiveOption("768px", 2, 1));
        responsiveOptions.add(new org.primefaces.model.ResponsiveOption("560px", 1, 1));


        // Inicializamos con todos los productos
        aplicarFiltros();

        CompletableFuture.runAsync(this::cargarValorDolar);
    }

    /**
     * Lógica de filtrado y ordenamiento combinada
     */
    public void aplicarFiltros() {
        // 1. Filtrado
        Logger.logInfo("Aplicando Filtros");
        List<Producto> streamFiltrado = productos.stream()
                .filter(p -> searchQuery == null || searchQuery.isEmpty() ||
                        p.getNombre().toLowerCase().contains(searchQuery.toLowerCase()))
                .filter(p -> filtroCategoria == null || filtroCategoria.isEmpty() ||
                        p.getCategoria().getIdCategoria().toString().equals(filtroCategoria))
                .filter(p -> p.getPrecio() >= (precioMin != null ? precioMin : 0))
                .filter(p -> p.getPrecio() <= (precioMax != null ? precioMax : Integer.MAX_VALUE))
                .collect(Collectors.toList());

        // 2. Ordenamiento
        if ("price_asc".equals(orden)) {
            streamFiltrado.sort(Comparator.comparing(Producto::getPrecio));
        } else if ("price_desc".equals(orden)) {
            streamFiltrado.sort(Comparator.comparing(Producto::getPrecio).reversed());
        } else {
            // Orden por defecto (por ID o relevancia simulada)
            streamFiltrado.sort(Comparator.comparing(Producto::getIdProducto).reversed());
        }

        this.productosFiltrados = streamFiltrado;
    }

    // --- Métodos de utilidad ---

    public void cargarValorDolar() {
        try {
            URL url = new URL("https://mindicador.cl/api");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);

            try (InputStreamReader reader = new InputStreamReader(conn.getInputStream())) {
                JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
                this.valorDolar = json.getAsJsonObject("dolar").get("valor").getAsDouble();
            }
        } catch (Exception e) {
            this.valorDolar = 950.0; // Fallback en caso de error de API
            System.err.println("Error API Dólar, usando fallback: " + this.valorDolar);
        }
    }

    // Getters y Setters necesarios para la nueva UI
    public String getOrden() { return orden; }
    public void setOrden(String orden) { this.orden = orden; }

    public Integer getPrecioMin() { return precioMin; }
    public void setPrecioMin(Integer precioMin) { this.precioMin = precioMin; }

    public Integer getPrecioMax() { return precioMax; }
    public void setPrecioMax(Integer precioMax) { this.precioMax = precioMax; }

    public String getSearchQuery() { return searchQuery; }
    public void setSearchQuery(String searchQuery) { this.searchQuery = searchQuery; }

    public String getFiltroCategoria() { return filtroCategoria; }
    public void setFiltroCategoria(String filtroCategoria) { this.filtroCategoria = filtroCategoria; }

    public List<Producto> getProductosFiltrados() { return productosFiltrados; }
    public List<Categoria> getListaCategorias() { return listaCategorias; }
    public double getValorDolar() { return valorDolar; }

    public void checkValorDolar() { /* Update trigger */ }

    public List<Producto> getProductos() {
        return productos;
    }

    public List<ResponsiveOption> getResponsiveOptions() {
        return responsiveOptions;
    }

    public void setVista(String nuevaVista) {
        this.vista = nuevaVista;
        System.out.println("Cambiando vista a: " + nuevaVista);
    }

    public String getVista() {
        return vista;
    }

    // Método opcional para ajustar columnas según la vista
    public int getColumnas() {
        return "list".equals(this.vista) ? 1 : 3;
    }
}