package com.ferremas.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;


/**
 * The persistent class for the inventario database table.
 * 
 */
@Entity
@NamedQuery(name="Inventario.findAll", query="SELECT i FROM Inventario i")
public class Inventario implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private InventarioPK id;

	private Integer stock;

	//bi-directional many-to-one association to Producto
	@ManyToOne
	@JoinColumn(name="id_producto")
	private Producto producto;

	//bi-directional many-to-one association to Sucursal
	@ManyToOne
	@JoinColumn(name="id_sucursal")
	private Sucursal sucursal;

	@Transient
	@JsonIgnore // Ignorado por Jackson (REST API)
	private InventoryStatus inventoryStatus;

	public Inventario() {
	}

	public InventoryStatus getInventoryStatus() {
		return inventoryStatus;
	}

	public void setInventoryStatus(InventoryStatus inventoryStatus) {
		this.inventoryStatus = inventoryStatus;
	}


	public InventarioPK getId() {
		return this.id;
	}

	public void setId(InventarioPK id) {
		this.id = id;
	}

	public Integer getStock() {
		return this.stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Producto getProducto() {
		return this.producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public Sucursal getSucursal() {
		return this.sucursal;
	}

	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}


	@Override
	public String toString() {
		return "Inventario{" +
				"stock=" + stock +
				", producto=" + producto.toString() +
				", sucursal=" + sucursal.toString() +
				'}';
	}
}