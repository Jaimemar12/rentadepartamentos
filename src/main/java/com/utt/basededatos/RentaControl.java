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

public class RentaControl {

    final static String BASEDEDATOS = "RentaDepartamentos";
    final static String CONNECCION = "mongodb+srv://michellelara258:ypZJPUUhsv0FOHqJ@departamentos.8mra6zr.mongodb.net/?retryWrites=true&w=majority";
    final static String COLECCION = "renta";

    private RentaControl(){}

    public static boolean crearRenta(String fechaInicio, String fechaFin, String montoRenta, String montoPago, String idDepartamento, String idCliente) {
        if (buscarRentaPorCliente(idCliente) != null) {
            return false;
        }

        RentaModelo renta = new RentaModelo(fechaInicio, fechaFin, montoRenta, montoPago, idDepartamento, idCliente);

        try (MongoClient clienteMongo = MongoClients.create(CONNECCION)) {
            MongoDatabase baseDeDatos = clienteMongo.getDatabase(BASEDEDATOS);
            baseDeDatos.getCollection(COLECCION).insertOne(renta.comoBson());
            return true;
        } catch (MongoException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Document buscarRentaPorCliente(String idCliente) {
        try (MongoClient clienteMongo = MongoClients.create(CONNECCION)) {
            MongoDatabase baseDeDatos = clienteMongo.getDatabase(BASEDEDATOS);
            return baseDeDatos.getCollection(COLECCION).find(new Document("id_cliente", idCliente)).first();
        } catch (MongoException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Document buscarRentaPorDepartamento(String idDepartamento) {
        try (MongoClient clienteMongo = MongoClients.create(CONNECCION)) {
            MongoDatabase baseDeDatos = clienteMongo.getDatabase(BASEDEDATOS);
            return baseDeDatos.getCollection(COLECCION).find(new Document("id_departamento", idDepartamento)).first();
        } catch (MongoException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Set<Document> buscarRentas() {
        try (MongoClient clienteMongo = MongoClients.create(CONNECCION)) {
            MongoDatabase baseDeDatos = clienteMongo.getDatabase(BASEDEDATOS);
            FindIterable<Document> rentas = baseDeDatos.getCollection(COLECCION).find();
            Set<Document> documentos = new HashSet<>();
            for (Document renta : rentas) {
                documentos.add(renta);
            }
            return documentos;
        } catch (MongoException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean actualizarRenta(String idCliente, String variable, String valor) {
        if (buscarRentaPorCliente(idCliente) == null) {
            return false;
        }

        try (MongoClient clienteMongo = MongoClients.create(CONNECCION)) {
            MongoDatabase baseDeDatos = clienteMongo.getDatabase(BASEDEDATOS);
            baseDeDatos.getCollection(COLECCION).updateOne(eq("id_cliente", idCliente), set(variable, valor));
            return true;
        } catch (MongoException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean borrarRenta(String idCliente) {
        if (buscarRentaPorCliente(idCliente) == null) {
            return false;
        }

        try (MongoClient clienteMongo = MongoClients.create(CONNECCION)) {
            MongoDatabase baseDeDatos = clienteMongo.getDatabase(BASEDEDATOS);
            baseDeDatos.getCollection(COLECCION).deleteOne(eq("id_cliente", idCliente));
            return true;
        } catch (MongoException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getDepartamentoID(String idCliente) {
        Document renta = buscarRentaPorCliente(idCliente);
        if (renta == null) {
            return null;
        }

        return renta.get("id_departamento").toString();
    }

    public static String getClienteID(String idDepartamento) {
        Document renta = buscarRentaPorDepartamento(idDepartamento);
        if (renta == null) {
            return null;
        }

        return renta.get("id_cliente").toString();
    }
}
