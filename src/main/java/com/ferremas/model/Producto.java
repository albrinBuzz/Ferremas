package com.ferremas.model;

import java.io.Serializable;
import jakarta.persistence.*;
import java.util.List;


/**
 * The persistent class for the producto database table.
 * 
 */
@Entity
@NamedQuery(name="Producto.findAll", query="SELECT p FROM Producto p")
public class Producto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator = "producto_seq")
	@SequenceGenerator(name = "producto_seq",sequenceName = "producto_seq",allocationSize = 1)
	@Column(name="id_producto")
	private Integer idProducto;

	private String descripcion;

	private String marca;

	private String nombre;

	private Integer precio;

	private String imagen;

	//bi-directional many-to-one association to Detallepedido
	@OneToMany(mappedBy="producto")
	private List<Detallepedido> detallepedidos;

	//bi-directional many-to-one association to Inventario
	@OneToMany(mappedBy="producto")
	private List<Inventario> inventarios;

	//bi-directional many-to-one association to Categoria
	@ManyToOne
	@JoinColumn(name="id_categoria")
	private Categoria categoria;

	public Producto() {
	}

	public Integer getIdProducto() {
		return this.idProducto;
	}

	public void setIdProducto(Integer idProducto) {
		this.idProducto = idProducto;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getMarca() {
		return this.marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getPrecio() {
		return this.precio;
	}

	public void setPrecio(Integer precio) {
		this.precio = precio;
	}

	public String getImagen() {
		return this.imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}


	public List<Detallepedido> getDetallepedidos() {
		return this.detallepedidos;
	}

	public void setDetallepedidos(List<Detallepedido> detallepedidos) {
		this.detallepedidos = detallepedidos;
	}

	public Detallepedido addDetallepedido(Detallepedido detallepedido) {
		getDetallepedidos().add(detallepedido);
		detallepedido.setProducto(this);

		return detallepedido;
	}

	public Detallepedido removeDetallepedido(Detallepedido detallepedido) {
		getDetallepedidos().remove(detallepedido);
		detallepedido.setProducto(null);

		return detallepedido;
	}

	public List<Inventario> getInventarios() {
		return this.inventarios;
	}

	public void setInventarios(List<Inventario> inventarios) {
		this.inventarios = inventarios;
	}

	public Inventario addInventario(Inventario inventario) {
		getInventarios().add(inventario);
		inventario.setProducto(this);

		return inventario;
	}

	public Inventario removeInventario(Inventario inventario) {
		getInventarios().remove(inventario);
		inventario.setProducto(null);

		return inventario;
	}

	public Categoria getCategoria() {
		return this.categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}


	@Override
	public String toString() {
		return "Producto{" +
				"imagen='" + imagen + '\'' +
				", precio=" + precio +
				", nombre='" + nombre + '\'' +
				", marca='" + marca + '\'' +
				", descripcion='" + descripcion + '\'' +
				", idProducto=" + idProducto +
				'}';
	}
}