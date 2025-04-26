package com.ferremas.model;

import java.io.Serializable;
import jakarta.persistence.*;
import java.util.List;


/**
 * The persistent class for the metodopago database table.
 * 
 */
@Entity
@NamedQuery(name="Metodopago.findAll", query="SELECT m FROM Metodopago m")
public class Metodopago implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator = "metodo_pago_seq")
	@SequenceGenerator(name = "metodo_pago_seq",sequenceName = "metodo_pago_seq",allocationSize = 1)
	@Column(name="id_metodo_pago")
	private Integer idMetodoPago;

	private String descripcion;

	private String nombre;

	//bi-directional many-to-one association to Transaccion
	@OneToMany(mappedBy="metodopago")
	private List<Transaccion> transaccions;

	public Metodopago() {
	}

	public Integer getIdMetodoPago() {
		return this.idMetodoPago;
	}

	public void setIdMetodoPago(Integer idMetodoPago) {
		this.idMetodoPago = idMetodoPago;
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

	public List<Transaccion> getTransaccions() {
		return this.transaccions;
	}

	public void setTransaccions(List<Transaccion> transaccions) {
		this.transaccions = transaccions;
	}

	public Transaccion addTransaccion(Transaccion transaccion) {
		getTransaccions().add(transaccion);
		transaccion.setMetodopago(this);

		return transaccion;
	}

	public Transaccion removeTransaccion(Transaccion transaccion) {
		getTransaccions().remove(transaccion);
		transaccion.setMetodopago(null);

		return transaccion;
	}

}