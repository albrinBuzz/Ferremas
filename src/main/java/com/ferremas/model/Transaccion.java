package com.ferremas.model;

import java.io.Serializable;
import jakarta.persistence.*;
import java.util.Date;


/**
 * The persistent class for the transaccion database table.
 * 
 */
@Entity
@NamedQuery(name="Transaccion.findAll", query="SELECT t FROM Transaccion t")
public class Transaccion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator = "transaccion_seq")
	@SequenceGenerator(name = "transaccion_seq",sequenceName = "transaccion_seq",allocationSize = 1)
	@Column(name="id_transaccion")
	private Integer idTransaccion;

	@Temporal(TemporalType.DATE)
	private Date fecha;

	private Integer monto;

	//bi-directional many-to-one association to Metodopago
	@ManyToOne
	@JoinColumn(name="id_metodo_pago")
	private Metodopago metodopago;

	//bi-directional many-to-one association to Pedido
	@ManyToOne
	@JoinColumn(name="id_pedido")
	private Pedido pedido;

	public Transaccion() {
	}

	public Integer getIdTransaccion() {
		return this.idTransaccion;
	}

	public void setIdTransaccion(Integer idTransaccion) {
		this.idTransaccion = idTransaccion;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Integer getMonto() {
		return this.monto;
	}

	public void setMonto(Integer monto) {
		this.monto = monto;
	}

	public Metodopago getMetodopago() {
		return this.metodopago;
	}

	public void setMetodopago(Metodopago metodopago) {
		this.metodopago = metodopago;
	}

	public Pedido getPedido() {
		return this.pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

}