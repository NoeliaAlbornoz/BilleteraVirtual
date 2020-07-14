package ar.com.ada.api.billeteravirtual.models.request;

import java.math.BigDecimal;

import ar.com.ada.api.billeteravirtual.entities.Transaccion.ConceptoTransaccionEnum;

public class CargaSaldoRequest {

    public BigDecimal importe;
    public String moneda;
    public ConceptoTransaccionEnum motivo;
    
}