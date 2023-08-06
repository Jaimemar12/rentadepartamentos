package com.utt.basededatos;

import org.bson.Document;

import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.*;

import java.util.HashSet;
import java.util.Set;

public class ClienteControl {

    final static String BASEDEDATOS = "RentaDepartamentos";
    final static String CONNECCION = "mongodb+srv://michellelara258:ypZJPUUhsv0FOHqJ@departamentos.8mra6zr.mongodb.net/?retryWrites=true&w=majority";
    final static String COLECCION = "cliente";

    private ClienteControl() {
    }

    public static boolean crearCliente(String nombre, String apellido, String direccion, String telefono,
            String correo) {
        if (buscarClientePorTelefono(telefono) != null) {
            return false;
        }

        ClienteModelo cliente = new ClienteModelo(nombre, apellido, direccion, telefono, correo);

        try (MongoClient clienteMongo = MongoClients.create(CONNECCION)) {
            MongoDatabase baseDeDatos = clienteMongo.getDatabase(BASEDEDATOS);
            baseDeDatos.getCollection(COLECCION).insertOne(cliente.comoBson());
            return true;
        } catch (MongoException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Document buscarClientePorTelefono(String telefono) {
        try (MongoClient clienteMongo = MongoClients.create(CONNECCION)) {
            MongoDatabase baseDeDatos = clienteMongo.getDatabase(BASEDEDATOS);
            return baseDeDatos.getCollection(COLECCION).find(new Document("telefono", telefono)).first();
        } catch (MongoException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Document buscarClientePorID(String idCliente) {
        try (MongoClient clienteMongo = MongoClients.create(CONNECCION)) {
            MongoDatabase baseDeDatos = clienteMongo.getDatabase(BASEDEDATOS);
            return baseDeDatos.getCollection(COLECCION).find(new Document("_id", idCliente)).first();
        } catch (MongoException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Set<Document> buscarClientes() {
        try (MongoClient clienteMongo = MongoClients.create(CONNECCION)) {
            MongoDatabase baseDeDatos = clienteMongo.getDatabase(BASEDEDATOS);
            FindIterable<Document> clientes = baseDeDatos.getCollection(COLECCION).find();
            Set<Document> documentos = new HashSet<>();
            for (Document cliente : clientes) {
                documentos.add(cliente);
            }
            return documentos;
        } catch (MongoException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean actualizarCliente(String telefono, String variable, String valor) {
        if (buscarClientePorTelefono(telefono) == null) {
            return false;
        }

        try (MongoClient clienteMongo = MongoClients.create(CONNECCION)) {
            MongoDatabase baseDeDatos = clienteMongo.getDatabase(BASEDEDATOS);
            baseDeDatos.getCollection(COLECCION).updateOne(eq("telefono", telefono), set(variable, valor));
            return true;
        } catch (MongoException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean borrarCliente(String telefono) {
        if (buscarClientePorTelefono(telefono) == null) {
            return false;
        }

        try (MongoClient clienteMongo = MongoClients.create(CONNECCION)) {
            MongoDatabase baseDeDatos = clienteMongo.getDatabase(BASEDEDATOS);
            baseDeDatos.getCollection(COLECCION).deleteOne(eq("telefono", telefono));
            return true;
        } catch (MongoException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getID(String telefono) {
        Document cliente = buscarClientePorTelefono(telefono);
        if (cliente == null) {
            return null;
        }

        return cliente.get("_id").toString();
    }

    public static String getTelefono(String idCliente) {
        Document cliente = null;
        try (MongoClient clienteMongo = MongoClients.create(CONNECCION)) {
            MongoDatabase baseDeDatos = clienteMongo.getDatabase(BASEDEDATOS);
            cliente = baseDeDatos.getCollection(COLECCION).find(new Document("_id", idCliente)).first();
        } catch (MongoException e) {
            e.printStackTrace();
        }
        if (cliente == null) {
            return null;
        }
        return cliente.get("telefono").toString();
    }
}
