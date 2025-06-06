package com.ferrremas.apiProductos.model;


import java.io.Serializable;

public class ProductoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer idProducto;
    private String descripcion;
    private String marca;
    private String nombre;
    private Integer precio;
    private String imagen;
    private Integer idCategoria; // Se representa solo el ID de la categoría

    public ProductoDTO() {
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getPrecio() {
        return precio;
    }

    public void setPrecio(Integer precio) {
        this.precio = precio;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public Producto toEntity(){
        Producto producto = new Producto();
        producto.setIdProducto(getIdProducto());
        producto.setNombre(getNombre());
        producto.setDescripcion(getDescripcion());
        producto.setMarca(getMarca());
        producto.setPrecio(getPrecio());
        producto.setImagen(getImagen());
        return producto;
    }

    @Override
    public String toString() {
        return "ProductoDTO{" +
                "idProducto=" + idProducto +
                ", descripcion='" + descripcion + '\'' +
                ", marca='" + marca + '\'' +
                ", nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", imagen='" + imagen + '\'' +
                ", idCategoria=" + idCategoria +
                '}';
    }
}
