package com.utt.basededatos;

import org.bson.Document;
import org.bson.types.ObjectId;

class DepartamentoModelo {
    private String ubicacion;
    private String numRecamaras;
    private String precio;
    private String estado;

    DepartamentoModelo(String ubicacion, String numRecamaras, String precio) {
        this.ubicacion = ubicacion;
        this.numRecamaras = numRecamaras;
        this.precio = precio;
        this.estado = "Activo";
    }

    Document comoBson() {
        Document departamento = new Document("_id", new ObjectId().toString());
        departamento.append("ubicacion", ubicacion);
        departamento.append("numero_recamaras", numRecamaras);
        departamento.append("precio", precio);
        departamento.append("estado", estado);
        return departamento;
    }
}
