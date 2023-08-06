package com.utt.basededatos;

import org.bson.Document;
import org.bson.types.ObjectId;

class ClienteModelo {
    private String nombre;
    private String apellido;
    private String direccion;
    private String telefono;
    private String correo;

    ClienteModelo(String nombre, String apellido, String direccion, String telefono, String correo) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.direccion = direccion;
        this.telefono = telefono;
        this.correo = correo;
    }

    Document comoBson() {
        Document cliente = new Document("_id", new ObjectId().toString());
        cliente.append("nombre", nombre);
        cliente.append("apellido", apellido);
        cliente.append("direccion", direccion);
        cliente.append("telefono", telefono);
        cliente.append("correo", correo);
        return cliente;
    }
}
