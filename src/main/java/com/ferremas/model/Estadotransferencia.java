package com.ferremas.model;

import java.io.Serializable;
import jakarta.persistence.*;
import java.util.List;


/**
 * The persistent class for the estadotransferencia database table.
 * 
 */
@Entity
@NamedQuery(name="Estadotransferencia.findAll", query="SELECT e FROM Estadotransferencia e")
public class Estadotransferencia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator = "estado_trasnfe_seq")
	@SequenceGenerator(name = "estado_trasnfe_seq",sequenceName = "estado_trasnfe_seq",allocationSize = 1)
	private Integer idestadotrnsf;

	private String descripcion;

	private String nombre;

	//bi-directional many-to-one association to Transferencia
	@OneToMany(mappedBy="estadotransferencia")
	private List<Transferencia> transferencias;

	public Estadotransferencia() {
	}

	public Integer getIdestadotrnsf() {
		return this.idestadotrnsf;
	}

	public void setIdestadotrnsf(Integer idestadotrnsf) {
		this.idestadotrnsf = idestadotrnsf;
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

	public List<Transferencia> getTransferencias() {
		return this.transferencias;
	}

	public void setTransferencias(List<Transferencia> transferencias) {
		this.transferencias = transferencias;
	}

	public Transferencia addTransferencia(Transferencia transferencia) {
		getTransferencias().add(transferencia);
		transferencia.setEstadotransferencia(this);

		return transferencia;
	}

	public Transferencia removeTransferencia(Transferencia transferencia) {
		getTransferencias().remove(transferencia);
		transferencia.setEstadotransferencia(null);

		return transferencia;
	}

}