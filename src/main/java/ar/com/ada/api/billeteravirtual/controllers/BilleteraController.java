package ar.com.ada.api.billeteravirtual.controllers;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class BilleteraController {

    /* WEB Method 1: consultar saldo: GET
                    URL: billeteras/{id}/saldos
    WEB Method 2: cargar saldo: POST
                    URL: billeteras/{id}/recargas
                    requestBody: {
                        "moneda":
                        "importe":
                    }
    WEB Method 3: enviar saldo: POST
                    URL: billeteras/{id}/envios
                    requestBody: {
                        "moneda":
                        "importe":
                        "email":
                        "motivo":
                        "detalleDelMotivo":
                    } 
    */

}