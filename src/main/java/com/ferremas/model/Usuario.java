package com.ferremas.model;

import java.io.Serializable;

import com.ferremas.util.Logger;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.ferremas.model.Cliente;



/**
 * The persistent class for the usuario database table.
 * 
 */
@Entity
@NamedQuery(name="Usuario.findAll", query="SELECT u FROM Usuario u")
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="rut_usuario")
	private String rutUsuario;

	private String contrasena;

	@Column(unique = true)
	private String correo;

	private String nombreusuario;

	//bi-directional one-to-one association to Cliente
	@OneToOne(mappedBy="usuario",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	private Cliente cliente;

	//bi-directional one-to-one association to Empleado
	@OneToOne(mappedBy="usuario",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	private Empleado empleado;

	//bi-directional many-to-many association to Rol
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name="rolusuario",
			joinColumns = {@JoinColumn(name="rut_usuario")},
			inverseJoinColumns = {@JoinColumn(name="id_rol")}
	)
	private Set<Rol> roles = new HashSet<>();

	public Usuario() {
	}

	public String getRutUsuario() {
		return this.rutUsuario;
	}

	public void setRutUsuario(String rutUsuario) {
		this.rutUsuario = rutUsuario;
	}

	public String getContrasena() {
		return this.contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	public String getCorreo() {
		return this.correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getNombreusuario() {
		return this.nombreusuario;
	}

	public void setNombreusuario(String nombreusuario) {
		this.nombreusuario = nombreusuario;
	}

	public Cliente getCliente() {
		return this.cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Empleado getEmpleado() {
		return this.empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}

	public Set<Rol> getRoles() {
		return roles;
	}

	public void setRoles(Set<Rol> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "Usuario{" +
				"rutUsuario='" + rutUsuario + '\'' +
				", contrasena='" + contrasena + '\'' +
				", correo='" + correo + '\'' +
				", nombreusuario='" + nombreusuario + '\'' +
				", roles=" + getRolesAsString() +
				'}';
	}

	private String getRolesAsString() {
		return roles.stream()
				.map(Rol::getNombre)
				.reduce((a, b) -> a + ", " + b)
				.orElse("Sin roles");
	}
}