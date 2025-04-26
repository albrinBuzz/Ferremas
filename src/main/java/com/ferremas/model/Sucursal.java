package com.ferremas.model;

import java.io.Serializable;
import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;


/**
 * The persistent class for the sucursal database table.
 * 
 */
@Entity
@NamedQuery(name="Sucursal.findAll", query="SELECT s FROM Sucursal s")
public class Sucursal implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator = "sucursal_seq")
	@SequenceGenerator(name = "sucursal_seq",sequenceName = "sucursal_seq",allocationSize = 1)
	@Column(name="id_sucursal")
	private Integer idSucursal;

	private String direccion;

	private String nombre;

	private String telefono;

	//bi-directional many-to-one association to Empleado
	@OneToMany(mappedBy="sucursal",fetch = FetchType.LAZY)
	private List<Empleado> empleados;

	//bi-directional many-to-one association to Inventario
	@OneToMany(mappedBy="sucursal")
	private List<Inventario> inventarios;

	//bi-directional many-to-one association to Pedido
	@OneToMany(mappedBy="sucursal")
	private List<Pedido> pedidos;

	//bi-directional many-to-one association to Ciudad
	@ManyToOne
	@JoinColumn(name="id_ciudad")
	private Ciudad ciudad;

	public Sucursal() {
	}

	public Integer getIdSucursal() {
		return this.idSucursal;
	}

	public void setIdSucursal(Integer idSucursal) {
		this.idSucursal = idSucursal;
	}

	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
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

	public List<Empleado> getEmpleados() {
		return this.empleados;
	}

	public void setEmpleados(List<Empleado> empleados) {
		this.empleados = empleados;
	}

	public Empleado addEmpleado(Empleado empleado) {
		getEmpleados().add(empleado);
		empleado.setSucursal(this);

		return empleado;
	}

	public Empleado removeEmpleado(Empleado empleado) {
		getEmpleados().remove(empleado);
		empleado.setSucursal(null);

		return empleado;
	}

	public List<Inventario> getInventarios() {
		return this.inventarios;
	}

	public void setInventarios(List<Inventario> inventarios) {
		this.inventarios = inventarios;
	}

	public Inventario addInventario(Inventario inventario) {
		getInventarios().add(inventario);
		inventario.setSucursal(this);

		return inventario;
	}

	public Inventario removeInventario(Inventario inventario) {
		getInventarios().remove(inventario);
		inventario.setSucursal(null);

		return inventario;
	}

	public List<Pedido> getPedidos() {
		return this.pedidos;
	}

	public void setPedidos(List<Pedido> pedidos) {
		this.pedidos = pedidos;
	}

	public Pedido addPedido(Pedido pedido) {
		getPedidos().add(pedido);
		pedido.setSucursal(this);

		return pedido;
	}

	public Pedido removePedido(Pedido pedido) {
		getPedidos().remove(pedido);
		pedido.setSucursal(null);

		return pedido;
	}

	public Ciudad getCiudad() {
		return this.ciudad;
	}

	public void setCiudad(Ciudad ciudad) {
		this.ciudad = ciudad;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Sucursal sucursal = (Sucursal) o;
		return Objects.equals(idSucursal, sucursal.idSucursal) && Objects.equals(nombre, sucursal.nombre);
	}

	@Override
	public int hashCode() {
		return Objects.hash(idSucursal, nombre);
	}

	@Override
	public String toString() {
		return "Sucursal{" +
				"idSucursal=" + idSucursal +
				", direccion='" + direccion + '\'' +
				", nombre='" + nombre + '\'' +
				", telefono='" + telefono + '\'' +
				'}';
	}
}