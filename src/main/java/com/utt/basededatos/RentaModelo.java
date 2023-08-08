package com.utt.basededatos;

import org.bson.Document;
import org.bson.types.ObjectId;

class RentaModelo {
    private String fechaInicio;
    private String fechaFin;
    private String balance;
    private String deposito;
    private String motivo;
    private String idDepartamento;
    private String idCliente;
    private String estado;

    RentaModelo(String fechaInicio, String fechaFin, String balance, String deposito, String idDepartamento, String idCliente) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.balance = balance;
        this.deposito = deposito;
        this.motivo = "";
        this.idDepartamento = idDepartamento;
        this.idCliente = idCliente;
        this.estado = "Activo";
    }

    Document comoBson() {
        Document renta = new Document("_id", new ObjectId().toString());
        renta.append("fecha_inicio", fechaInicio);
        renta.append("fecha_fin", fechaFin);
        renta.append("balance", balance);
        renta.append("deposito", deposito);
        renta.append("motivo", motivo);
        renta.append("id_departamento", idDepartamento);
        renta.append("id_cliente", idCliente);
        renta.append("estado", estado);
        return renta;
    }
}
