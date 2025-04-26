package com.ferremas.model;

import java.io.Serializable;
import jakarta.persistence.*;


/**
 * The persistent class for the detallepedido database table.
 * 
 */
@Entity
@NamedQuery(name="Detallepedido.findAll", query="SELECT d FROM Detallepedido d")
public class Detallepedido implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator = "detallepedido_seq")
	@SequenceGenerator(name = "detallepedido_seq",sequenceName = "detallepedido_seq",allocationSize = 1)
	@Column(name="id_detallepedido")
	private Integer idDetallepedido;

	private Integer cantidad;

	//bi-directional many-to-one association to Pedido
	@ManyToOne
	@JoinColumn(name="id_pedido")
	private Pedido pedido;

	//bi-directional many-to-one association to Producto
	@ManyToOne
	@JoinColumn(name="id_producto")
	private Producto producto;

	public Detallepedido() {
	}

	public Detallepedido(int i, Integer precio, Producto product) {
		this.cantidad=i;
		this.producto=product;
	}

	public Integer getIdDetallepedido() {
		return this.idDetallepedido;
	}

	public void setIdDetallepedido(Integer idDetallepedido) {
		this.idDetallepedido = idDetallepedido;
	}

	public Integer getCantidad() {
		return this.cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public Pedido getPedido() {
		return this.pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public Producto getProducto() {
		return this.producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}


	@Override
	public String toString() {
		return "Detallepedido{" +
				"idDetallepedido=" + idDetallepedido +
				", cantidad=" + cantidad +
				", pedido=" + pedido +
				", producto=" + producto +
				'}';
	}
}