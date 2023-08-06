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

public class DepartamentoControl {

    final static String BASEDEDATOS = "RentaDepartamentos";
    final static String CONNECCION = "mongodb+srv://michellelara258:ypZJPUUhsv0FOHqJ@departamentos.8mra6zr.mongodb.net/?retryWrites=true&w=majority";
    final static String COLECCION = "departamento";

    private DepartamentoControl() {
    }

    public static boolean crearDepartamento(String ubicacion, String numRecamaras, String precio) {
        if (buscarDepartamentoPorUbicacion(ubicacion) != null) {
            return false;
        }

        DepartamentoModelo departamento = new DepartamentoModelo(ubicacion, numRecamaras, precio);

        try (MongoClient clienteMongo = MongoClients.create(CONNECCION)) {
            MongoDatabase baseDeDatos = clienteMongo.getDatabase(BASEDEDATOS);
            baseDeDatos.getCollection(COLECCION).insertOne(departamento.comoBson());
            return true;
        } catch (MongoException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Document buscarDepartamentoPorUbicacion(String ubicacion) {
        try (MongoClient clienteMongo = MongoClients.create(CONNECCION)) {
            MongoDatabase baseDeDatos = clienteMongo.getDatabase(BASEDEDATOS);
            return baseDeDatos.getCollection(COLECCION).find(new Document("ubicacion", ubicacion)).first();
        } catch (MongoException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Document buscarDepartamentoPorID(String idDepartamento) {
        try (MongoClient clienteMongo = MongoClients.create(CONNECCION)) {
            MongoDatabase baseDeDatos = clienteMongo.getDatabase(BASEDEDATOS);
            return baseDeDatos.getCollection(COLECCION).find(new Document("_id", idDepartamento)).first();
        } catch (MongoException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Set<Document> buscarDepartamentos() {
        try (MongoClient clienteMongo = MongoClients.create(CONNECCION)) {
            MongoDatabase baseDeDatos = clienteMongo.getDatabase(BASEDEDATOS);
            FindIterable<Document> departamentos = baseDeDatos.getCollection(COLECCION).find();
            Set<Document> documentos = new HashSet<>();
            for (Document departamento : departamentos) {
                documentos.add(departamento);
            }
            return documentos;
        } catch (MongoException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean actualizarDepartamento(String ubicacion, String variable, String valor) {
        if (buscarDepartamentoPorUbicacion(ubicacion) == null) {
            return false;
        }

        try (MongoClient clienteMongo = MongoClients.create(CONNECCION)) {
            MongoDatabase baseDeDatos = clienteMongo.getDatabase(BASEDEDATOS);
            baseDeDatos.getCollection(COLECCION).updateOne(eq("ubicacion", ubicacion), set(variable, valor));
            return true;
        } catch (MongoException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean borrarDepartamento(String ubicacion) {
        if (buscarDepartamentoPorUbicacion(ubicacion) == null) {
            return false;
        }

        try (MongoClient clienteMongo = MongoClients.create(CONNECCION)) {
            MongoDatabase baseDeDatos = clienteMongo.getDatabase(BASEDEDATOS);
            baseDeDatos.getCollection(COLECCION).deleteOne(eq("ubicacion", ubicacion));
            return true;
        } catch (MongoException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getUbicacion(String idDepartamento) {
        Document departamento = null;
        try (MongoClient clienteMongo = MongoClients.create(CONNECCION)) {
            MongoDatabase baseDeDatos = clienteMongo.getDatabase(BASEDEDATOS);
            departamento = baseDeDatos.getCollection(COLECCION).find(new Document("_id", idDepartamento)).first();
        } catch (MongoException e) {
            e.printStackTrace();
        }
        if (departamento == null) {
            return null;
        }
        return departamento.get("ubicacion").toString();
    }

    public static String getID(String ubicacion) {
        Document departamento = buscarDepartamentoPorUbicacion(ubicacion);
        if (departamento == null) {
            return null;
        }

        return departamento.get("_id").toString();
    }
}
