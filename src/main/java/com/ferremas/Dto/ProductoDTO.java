package com.ferremas.Dto;

public class ProductoDTO {
    private String nombre;
    private String marca;
    private Integer precio;
    private Integer idCategoria; // Solo el id

    public ProductoDTO() {}

    public ProductoDTO(String nombre, String marca, Integer precio, Integer idCategoria) {
        this.nombre = nombre;
        this.marca = marca;
        this.precio = precio;
        this.idCategoria = idCategoria;
    }

    // Getters y setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public Integer getPrecio() { return precio; }
    public void setPrecio(Integer precio) { this.precio = precio; }

    public Integer getIdCategoria() { return idCategoria; }
    public void setIdCategoria(Integer idCategoria) { this.idCategoria = idCategoria; }
}
