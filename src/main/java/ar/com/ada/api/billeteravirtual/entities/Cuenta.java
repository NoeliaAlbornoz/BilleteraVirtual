package ar.com.ada.api.billeteravirtual.entities;

import java.math.BigDecimal;
import java.util.*;

import javax.persistence.*;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name = "cuenta")
public class Cuenta {

	@Id
	@Column(name = "cuenta_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer cuentaId;
	private BigDecimal saldo;
	private String moneda;
	@ManyToOne
	@JoinColumn(name = "billetera_id", referencedColumnName = "billetera_id")
	private Billetera billetera;
	@OneToMany(mappedBy = "cuenta", cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Transaccion> transacciones = new ArrayList<>();

	public Integer getCuentaId() {
		return cuentaId;
	}

	public void setCuentaId(Integer cuentaId) {
		this.cuentaId = cuentaId;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public Billetera getBilletera() {
		return billetera;
	}

	public void setBilletera(Billetera billetera) {
		this.billetera = billetera;
	}

	public List<Transaccion> getTransacciones() {
		return transacciones;
	}

	public void setTransacciones(List<Transaccion> transacciones) {
		this.transacciones = transacciones;
	}

	/*
	 * Bidirección a través de un método que agrega a la lista.
	 */
	public void agregarTransaccion(Transaccion transaccion) {

		BigDecimal saldoNuevo;
		BigDecimal saldoActual = this.getSaldo();
		BigDecimal importe = transaccion.getImporte();
		

		this.transacciones.add(transaccion);
		transaccion.setCuenta(this);

		if (transaccion.getTipoOperacion().equals(1)) {// Entrante
			saldoNuevo = saldoActual.add(importe);
		} else {// Saliente
			saldoNuevo = saldoActual.subtract(importe);
		}

		this.setSaldo(saldoNuevo);

	}

	public Transaccion generarTransaccion(String conceptoOperacion, String detalle, BigDecimal importe,
			Integer tipoOperacion) {

		Transaccion transaccion = new Transaccion();

		transaccion.setMoneda(moneda);
		transaccion.setFecha(new Date());
		transaccion.setConceptoOperacion(conceptoOperacion);
		transaccion.setDetalle(detalle);
		transaccion.setImporte(importe);
		transaccion.setTipoOperacion(tipoOperacion);// 1 Entrada, 0 Salida
		transaccion.setEstadoId(2);// -1 Rechazada 0 Pendiente 2 Aprobada

		if (transaccion.getTipoOperacion() == 1) { // Entrada

			transaccion.setaUsuarioId(billetera.getPersona().getUsuario().getUsuarioId());
			transaccion.setaCuentaId(this.getCuentaId());

		} else { // Salida

			transaccion.setDeCuentaId(this.getCuentaId());
			transaccion.setDeUsuarioId(billetera.getPersona().getUsuario().getUsuarioId());

		}

		return transaccion;

	}

}