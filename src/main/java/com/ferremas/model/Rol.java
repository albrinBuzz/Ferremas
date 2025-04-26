package com.ferremas.model;

import java.io.Serializable;
import jakarta.persistence.*;
import java.util.List;
import java.util.Set;


/**
 * The persistent class for the rol database table.
 * 
 */
@Entity
@NamedQuery(name="Rol.findAll", query="SELECT r FROM Rol r")
public class Rol implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator = "rol_seq")
	@SequenceGenerator(name = "rol_seq",sequenceName = "rol_seq",allocationSize = 1)
	@Column(name="id_rol")
	private Integer idRol;

	private String descripcion;

	private String nombre;

	//bi-directional many-to-many association to Usuario
	@ManyToMany(mappedBy = "roles",fetch = FetchType.EAGER)
	private Set<Usuario> usuarios;


	public Rol() {
	}

	public Integer getIdRol() {
		return this.idRol;
	}

	public void setIdRol(Integer idRol) {
		this.idRol = idRol;
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

	public Set<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(Set<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	@Override
	public String toString() {
		return "Rol{" +
				"nombre='" + nombre + '\'' +
				'}';
	}
}