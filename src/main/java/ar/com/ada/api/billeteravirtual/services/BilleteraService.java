package ar.com.ada.api.billeteravirtual.services;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.ada.api.billeteravirtual.entities.Billetera;
import ar.com.ada.api.billeteravirtual.entities.Cuenta;
import ar.com.ada.api.billeteravirtual.entities.Transaccion;
import ar.com.ada.api.billeteravirtual.entities.Usuario;
import ar.com.ada.api.billeteravirtual.repos.BilleteraRepository;

@Service
public class BilleteraService {

    @Autowired
    BilleteraRepository repo;

    @Autowired
    UsuarioService usuarioService;

    public void grabar(Billetera billetera) {
        repo.save(billetera);
    }

    public void cargarSaldo(BigDecimal saldo, String moneda, Integer billeteraId, String conceptoOperacion,
            String detalle) {

                Billetera billetera = this.buscarPorId(billeteraId);

                cargarSaldo(saldo, moneda, billetera, conceptoOperacion, detalle);
            }

    /**
     * Metodo cargarSaldo buscar billetera por id se identifica cuenta por moneda
     * determinar importe a cargar hacer transaccion
     * 
     * ver delegaciones sobre entidades
     * 
     */

    public void cargarSaldo(BigDecimal saldo, String moneda, Billetera billetera, String conceptoOperacion,
            String detalle) {

        Cuenta cuenta = billetera.getCuenta(moneda);

        Transaccion transaccion = new Transaccion();
        // transaccion.setCuenta(cuenta);
        transaccion.setMoneda(moneda);
        transaccion.setFecha(new Date());
        transaccion.setConceptoOperacion(conceptoOperacion);
        transaccion.setDetalle(detalle);
        transaccion.setImporte(saldo);
        transaccion.setTipoOperacion(1);// 1 Entrada, 0 Salida
        transaccion.setEstadoId(2);// -1 Rechazada 0 Pendiente 2 Aprobada
        transaccion.setDeCuentaId(cuenta.getCuentaId());
        transaccion.setDeUsuarioId(billetera.getPersona().getUsuario().getUsuarioId());
        transaccion.setaUsuarioId(billetera.getPersona().getUsuario().getUsuarioId());
        transaccion.setaCuentaId(cuenta.getCuentaId());

        cuenta.agregarTransaccion(transaccion);

        this.grabar(billetera);

    }

    /**
     * Metodo consultarSaldo buscar billetera por id se identifica cuenta por moneda
     * traer saldo
     */

    public BigDecimal consultarSaldo(Integer billeteraId, String moneda) {

        Billetera billetera = repo.findByBilleteraId(billeteraId);

        Cuenta cuenta = billetera.getCuenta(moneda);

        return cuenta.getSaldo();

    }

    public Billetera buscarPorId(Integer id) {

        return repo.findByBilleteraId(id);
    }

    public void enviarSaldo(BigDecimal importe, String moneda, Integer billeteraOrigenId, Integer billeteraDestinoId,
            String concepto, String detalle) {

        /**
         * Metodo enviarSaldo buscar billetera por id se identifica cuenta por moneda
         * determinar importe a transferir billetera de origen y billetera destino
         * actualizar los saldos de las cuentas (resta en la origen y suma en la
         * destino) generar 2 transacciones
         * 
         * ver delegaciones sobre entidades
         * 
         */

        Billetera billeteraSaliente = this.buscarPorId(billeteraOrigenId);
        Billetera billeteraEntrante = this.buscarPorId(billeteraDestinoId);

        Cuenta cuentaSaliente = billeteraSaliente.getCuenta(moneda);
        Cuenta cuentaEntrante = billeteraEntrante.getCuenta(moneda);

        Transaccion tSaliente = new Transaccion();
        Transaccion tEntrante = new Transaccion();

        tSaliente = cuentaSaliente.generarTransaccion(concepto, detalle, importe, 1);
        tSaliente.setaCuentaId(cuentaEntrante.getCuentaId());
        tSaliente.setaUsuarioId(billeteraEntrante.getPersona().getUsuario().getUsuarioId());

        tEntrante = cuentaEntrante.generarTransaccion(concepto, detalle, importe, 0);
        tEntrante.setDeCuentaId(cuentaSaliente.getCuentaId());
        tEntrante.setDeUsuarioId(billeteraSaliente.getPersona().getUsuario().getUsuarioId());

        cuentaSaliente.agregarTransaccion(tSaliente);
        cuentaEntrante.agregarTransaccion(tEntrante);

        this.grabar(billeteraSaliente);
        this.grabar(billeteraEntrante);

    }

    public void enviarSaldo(BigDecimal importe, String moneda, Integer billeteraOrigenId, String email, String concepto,
            String detalle) {

        Usuario usuarioDestino = usuarioService.buscarPorEmail(email);
        this.enviarSaldo(importe, moneda, billeteraOrigenId,
                usuarioDestino.getPersona().getBilletera().getBilleteraId(), concepto, detalle);

    }

}