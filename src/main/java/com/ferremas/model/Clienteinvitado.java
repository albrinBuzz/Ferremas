package com.ferremas.model;

import java.io.Serializable;
import jakarta.persistence.*;


/**
 * The persistent class for the clienteinvitado database table.
 * 
 */
@Entity
@NamedQuery(name="Clienteinvitado.findAll", query="SELECT c FROM Clienteinvitado c")
public class Clienteinvitado implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id

	private String rutcliente;

	private String correo;

	private String nombre;

	private String telefono;

	public Clienteinvitado() {
	}

	public String getRutcliente() {
		return this.rutcliente;
	}

	public void setRutcliente(String rutcliente) {
		this.rutcliente = rutcliente;
	}

	public String getCorreo() {
		return this.correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

}