package ar.com.ada.api.billeteravirtual.models.request;

import java.math.BigDecimal;

import ar.com.ada.api.billeteravirtual.entities.Transaccion.ConceptoTransaccionEnum;

public class EnvioSaldoRequest {

    public BigDecimal importe;
    public String moneda;
    public String email;
    public ConceptoTransaccionEnum motivo;
    public String detalle;
    
}