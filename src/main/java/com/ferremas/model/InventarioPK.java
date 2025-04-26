package com.ferremas.model;

import java.io.Serializable;
import jakarta.persistence.*;

/**
 * The primary key class for the inventario database table.
 * 
 */
@Embeddable
public class InventarioPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="id_producto", insertable=false, updatable=false)
	private Integer idProducto;

	@Column(name="id_sucursal", insertable=false, updatable=false)
	private Integer idSucursal;

	public InventarioPK() {
	}
	public Integer getIdProducto() {
		return this.idProducto;
	}
	public void setIdProducto(Integer idProducto) {
		this.idProducto = idProducto;
	}
	public Integer getIdSucursal() {
		return this.idSucursal;
	}
	public void setIdSucursal(Integer idSucursal) {
		this.idSucursal = idSucursal;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof InventarioPK)) {
			return false;
		}
		InventarioPK castOther = (InventarioPK)other;
		return 
			this.idProducto.equals(castOther.idProducto)
			&& this.idSucursal.equals(castOther.idSucursal);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.idProducto.hashCode();
		hash = hash * prime + this.idSucursal.hashCode();
		
		return hash;
	}
}