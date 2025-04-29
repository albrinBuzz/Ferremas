package com.ferremas.model;

import java.io.Serializable;

import com.ferremas.util.Logger;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;


/**
 * The persistent class for the pedido database table.
 * 
 */
@Entity
@NamedQuery(name="Pedido.findAll", query="SELECT p FROM Pedido p")
public class Pedido implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator = "pedido_seq")
	@SequenceGenerator(name = "pedido_seq",sequenceName = "pedido_seq",allocationSize = 1)
	@Column(name="id_pedido")
	private Integer idPedido;

	private String estado;

	@Temporal(TemporalType.DATE)
	private Date fecha;

	private String rutcliente;

	private Integer total;

	//bi-directional many-to-one association to Detallepedido
	@OneToMany(mappedBy="pedido",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	private List<Detallepedido> detallepedidos;

	//bi-directional many-to-one association to Estadopedido
	@ManyToOne
	@JoinColumn(name="id_estadopedido")
	private Estadopedido estadopedido;

	//bi-directional many-to-one association to Sucursal
	@ManyToOne
	@JoinColumn(name="id_sucursal")
	private Sucursal sucursal;

	//bi-directional many-to-one association to Transaccion
	@OneToMany(mappedBy="pedido")
	private List<Transaccion> transaccions;

	//bi-directional many-to-one association to Transferencia
	@OneToMany(mappedBy="pedido",fetch = FetchType.EAGER)
	private List<Transferencia> transferencias;

	public Pedido() {
		detallepedidos=new ArrayList<>();
		total=0;
	}

	public Integer getIdPedido() {
		return this.idPedido;
	}

	public void setIdPedido(Integer idPedido) {
		this.idPedido = idPedido;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getRutcliente() {
		return this.rutcliente;
	}

	public void setRutcliente(String rutcliente) {
		this.rutcliente = rutcliente;
	}

	public Integer getTotal() {
		return this.total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public List<Detallepedido> getDetallepedidos() {
		return this.detallepedidos;
	}

	public void setDetallepedidos(List<Detallepedido> detallepedidos) {
		this.detallepedidos = detallepedidos;
	}

	public Detallepedido addDetallepedido(Detallepedido detallepedido,int cantidad,int id_producto) {

		boolean found = false;

		for (Detallepedido existingDetalle : detallepedidos) {
			if (existingDetalle.getProducto().getIdProducto() == id_producto) {
				int cantidads = existingDetalle.getCantidad();
				cantidads += detallepedido.getCantidad();
				existingDetalle.setCantidad(cantidads);
				found = true;
				break;
			}
		}

		if (!found) {
			detallepedidos.add(detallepedido);
		}

		detallepedido.setPedido(this);
		total += cantidad * detallepedido.getProducto().getPrecio();
		return detallepedido;

		/*getDetallepedidos().add(detallepedido);
		detallepedido.setPedido(this);
		return detallepedido;*/
	}

	public void decrementarCantidad(int productId) {

		Function<Detallepedido, Detallepedido> decrementar=detalle->{
			//total -= detalle.getCantidad()*detalle.getPrecio();
			if (detalle.getCantidad()-1<=0){
				detallepedidos.remove(detalle);
			}
			detalle.setCantidad(detalle.getCantidad()-1);

			total -=detalle.getProducto().getPrecio();
			return detalle;
		};
		aplicar(productId, decrementar);


	}

	public void incrementarCantidad(int productId) {

		Function<Detallepedido, Detallepedido> incrementar= detalle->{
			total -= detalle.getCantidad()*detalle.getProducto().getPrecio();
			detalle.setCantidad(detalle.getCantidad()+1);

			total += detalle.getCantidad()*detalle.getProducto().getPrecio();
			return detalle;
		};
		aplicar(productId, incrementar);


	}

	private void aplicar(int productId,Function<Detallepedido, Detallepedido> funcion) {
		Iterator<Detallepedido> detalles=detallepedidos.iterator();
		while(detalles.hasNext()) {
			Detallepedido detalle=detalles.next();
			if (detalle.getProducto().getIdProducto() == productId) {
				detalle= funcion.apply(detalle);
				break;
			}
		}

	}




	public Detallepedido removeDetallepedido(Detallepedido detallepedido) {
		getDetallepedidos().remove(detallepedido);
		detallepedido.setPedido(null);

		return detallepedido;
	}

	public Estadopedido getEstadopedido() {
		return this.estadopedido;
	}

	public void setEstadopedido(Estadopedido estadopedido) {
		this.estadopedido = estadopedido;
	}

	public Sucursal getSucursal() {
		return this.sucursal;
	}

	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}

	public List<Transaccion> getTransaccions() {
		return this.transaccions;
	}

	public void setTransaccions(List<Transaccion> transaccions) {
		this.transaccions = transaccions;
	}

	public Transaccion addTransaccion(Transaccion transaccion) {
		getTransaccions().add(transaccion);
		transaccion.setPedido(this);

		return transaccion;
	}

	public Transaccion removeTransaccion(Transaccion transaccion) {
		getTransaccions().remove(transaccion);
		transaccion.setPedido(null);

		return transaccion;
	}

	public List<Transferencia> getTransferencias() {
		return this.transferencias;
	}

	public void setTransferencias(List<Transferencia> transferencias) {
		this.transferencias = transferencias;
	}

	public Transferencia addTransferencia(Transferencia transferencia) {
		getTransferencias().add(transferencia);
		transferencia.setPedido(this);

		return transferencia;
	}

	public Transferencia removeTransferencia(Transferencia transferencia) {
		getTransferencias().remove(transferencia);
		transferencia.setPedido(null);

		return transferencia;
	}

	public void imprimirDetalles() {
		for (Detallepedido existingDetalle : detallepedidos) {
			Logger.logInfo(existingDetalle.toString()+"\n");
		}
	}

	@Override
	public String toString() {
		return "Pedido{" +
				"rutcliente='" + rutcliente + '\'' +
				", total=" + total +
				", IdPedido=" + idPedido +

				", Estado=" + estadopedido.toString() +
				'}';
	}
}