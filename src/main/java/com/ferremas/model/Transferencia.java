package com.ferremas.model;

import java.io.Serializable;
import jakarta.persistence.*;
import java.util.Date;


/**
 * The persistent class for the transferencia database table.
 * 
 */
@Entity
@NamedQuery(name="Transferencia.findAll", query="SELECT t FROM Transferencia t")
public class Transferencia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator = "transferencia_seq")
	@SequenceGenerator(name = "transferencia_seq",sequenceName = "transferencia_seq",allocationSize = 1)
	private Integer idtransferencia;

	private String comentario;

	private String comprobante;

	@Temporal(TemporalType.DATE)
	private Date fecha;

	private Integer monto;

	//bi-directional many-to-one association to Estadotransferencia
	@ManyToOne
	@JoinColumn(name="idestadotrnsf")
	private Estadotransferencia estadotransferencia;

	//bi-directional many-to-one association to Pedido
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "id_pedido")
	private Pedido pedido;

	public Transferencia() {
	}

	public Integer getIdtransferencia() {
		return this.idtransferencia;
	}

	public void setIdtransferencia(Integer idtransferencia) {
		this.idtransferencia = idtransferencia;
	}

	public String getComentario() {
		return this.comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public String getComprobante() {
		return this.comprobante;
	}

	public void setComprobante(String comprobante) {
		this.comprobante = comprobante;
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

	public Estadotransferencia getEstadotransferencia() {
		return this.estadotransferencia;
	}

	public void setEstadotransferencia(Estadotransferencia estadotransferencia) {
		this.estadotransferencia = estadotransferencia;
	}

	public Pedido getPedido() {
		return this.pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

}