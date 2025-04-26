package com.ferremas.model;

import java.io.Serializable;
import jakarta.persistence.*;


/**
 * The persistent class for the empleado database table.
 * 
 */
@Entity
@NamedQuery(name="Empleado.findAll", query="SELECT e FROM Empleado e")
public class Empleado implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="rut_usuario")
	private String rutUsuario;

	//bi-directional many-to-one association to Sucursal
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="id_sucursal")
	private Sucursal sucursal;

	//bi-directional one-to-one association to Usuario
	@OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
	@JoinColumn(name="rut_usuario")
	private Usuario usuario;

	public Empleado() {
	}

	public Empleado(String rutUsuario, Sucursal sucursal, Usuario usuario) {
		this.rutUsuario = rutUsuario;
		this.sucursal = sucursal;
		this.usuario = usuario;
	}

	public String getRutUsuario() {
		return this.rutUsuario;
	}

	public void setRutUsuario(String rutUsuario) {
		this.rutUsuario = rutUsuario;
	}

	public Sucursal getSucursal() {
		return this.sucursal;
	}

	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Override
	public String toString() {
		return "Empleado{" +
				"rutUsuario='" + rutUsuario + '\'' +
				", sucursal=" + sucursal +
				", usuario=" + usuario.toString() +
				'}';
	}
}