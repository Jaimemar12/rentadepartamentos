package com.utt.basededatos;

import org.bson.Document;
import org.bson.types.ObjectId;

class RentaModelo {
    private String fechaInicio;
    private String fechaFin;
    private String montoRenta;
    private String montoPago;
    private String idDepartamento;
    private String idCliente;

    RentaModelo(String fechaInicio, String fechaFin, String montoRenta, String montoPago, String idDepartamento, String idCliente) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.montoRenta = montoRenta;
        this.montoPago = montoPago;
        this.idDepartamento = idDepartamento;
        this.idCliente = idCliente;
    }

    Document comoBson() {
        Document renta = new Document("_id", new ObjectId().toString());
        renta.append("fecha_inicio", fechaInicio);
        renta.append("fecha_fin", fechaFin);
        renta.append("monto_renta", montoRenta);
        renta.append("monto_pago", montoPago);
        renta.append("id_departamento", idDepartamento);
        renta.append("id_cliente", idCliente);
        return renta;
    }
}
