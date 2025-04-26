package com.ferremas.model;

import java.io.Serializable;
import jakarta.persistence.*;
import java.util.List;


/**
 * The persistent class for the estadopedido database table.
 * 
 */
@Entity
@NamedQuery(name="Estadopedido.findAll", query="SELECT e FROM Estadopedido e")
public class Estadopedido implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator = "estadopedido_seq")
	@SequenceGenerator(name = "estadopedido_seq",sequenceName = "estadopedido_seq",allocationSize = 1)
	@Column(name="id_estadopedido")
	private Integer idEstadopedido;

	private String descripcion;

	private String nombre;

	//bi-directional many-to-one association to Pedido
	@OneToMany(mappedBy="estadopedido")
	private List<Pedido> pedidos;

	public Estadopedido() {
	}

	public Integer getIdEstadopedido() {
		return this.idEstadopedido;
	}

	public void setIdEstadopedido(Integer idEstadopedido) {
		this.idEstadopedido = idEstadopedido;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Pedido> getPedidos() {
		return this.pedidos;
	}

	public void setPedidos(List<Pedido> pedidos) {
		this.pedidos = pedidos;
	}

	public Pedido addPedido(Pedido pedido) {
		getPedidos().add(pedido);
		pedido.setEstadopedido(this);

		return pedido;
	}

	public Pedido removePedido(Pedido pedido) {
		getPedidos().remove(pedido);
		pedido.setEstadopedido(null);

		return pedido;
	}

	@Override
	public String toString() {
		return "Estadopedido{" +
				"descripcion='" + descripcion + '\'' +
				", nombre='" + nombre + '\'' +
				'}';
	}
}