package com.utt;

import java.util.Scanner;
import java.util.Set;

import org.bson.Document;

import com.utt.basededatos.ClienteControl;
import com.utt.basededatos.DepartamentoControl;
import com.utt.basededatos.RentaControl;

public class DepartamentoMenu implements Menu {

    @Override
    public void crear() {
        String ubicacion = "";
        String numRecamaras = "";
        String precio = "";
        do {
            Scanner scn = new Scanner(System.in);
            System.out.println("\nIngresa la ubicacion del departamento:");
            ubicacion = scn.nextLine();
            System.out.println("Ingresa el numero de recamaras del departamento:");
            numRecamaras = scn.nextLine();
            System.out.println("Ingresa el precio del departamento:");
            precio = scn.nextLine();
        } while (!DepartamentoControl.crearDepartamento(ubicacion, numRecamaras, precio));
        System.out.println("Nuevo departamento creado");
    }

    @Override
    public void ver() {
        while (true) {
            System.out.println("\n1. Buscar departamento");
            System.out.println("2. Ver informacion de todos los departamentos");
            Scanner scn = new Scanner(System.in);
            String respuesta = scn.nextLine();
            if (respuesta.equals("1")) {
                System.out.println("\nIngresa la ubicacion del departamento");
                String ubicacion = scn.nextLine();
                Document departamento = DepartamentoControl.buscarDepartamento(ubicacion);
                if (departamento == null) {
                    System.out.println("No se encontro departamento");
                    continue;
                } else {
                    System.out.println(departamento.toJson());
                    return;
                }
            } else if (respuesta.equals("2")) {
                Set<Document> departamentos = DepartamentoControl.buscarDepartamentos();
                for (Document departamento : departamentos) {
                    System.out.println(departamento);
                }
                return;
            }
        }
    }

    @Override
    public void editar() {
        String variable = "";
        while (true) {
            Scanner scn = new Scanner(System.in);
            System.out.println("\nIngresa la ubicacion del departamento");
            String ubicacion = scn.nextLine();
            Document departamento = DepartamentoControl.buscarDepartamento(ubicacion);
            if (departamento == null) {
                System.out.println("No se encontro departamento");
            } else {
                System.out.println("\nQue deseas actualizar?");
                System.out.println("1. Ubicacion");
                System.out.println("2. Numero de recamaras");
                System.out.println("3. Precio");
                String opcion = scn.nextLine();

                switch (opcion) {
                    case "1":
                        variable = "ubicacion";
                        break;
                    case "2":
                        variable = "numero_recamaras";
                        break;
                    case "3":
                        variable = "precio";
                        break;
                }

                System.out.println("\nIngresa el nuevo valor:");
                String valor = scn.nextLine();
                DepartamentoControl.actualizarDepartamento(ubicacion, variable, valor);
                System.out.println("Departamento editado");
                return;
            }
        }
    }

    @Override
    public void borrar() {
        while (true) {
            Scanner scn = new Scanner(System.in);
            System.out.println(
                    "Ingresa la ubicacion del departamento que deseas borrar\nNota: Si el cliente tiene renta o cliente enralazado, estos datos seran borrados igual");
            String ubicacion = scn.nextLine();
            Document departamento = DepartamentoControl.buscarDepartamento(ubicacion);
            if (departamento == null) {
                System.out.println("No se encontro departamento");
            } else {
                String idDepartamento = DepartamentoControl.getID(ubicacion);
                String idCliente = RentaControl.getClienteID(idDepartamento);
                String telefono = ClienteControl.getTelefono(idCliente);
                ClienteControl.borrarCliente(telefono);
                DepartamentoControl.borrarDepartamento(ubicacion);
                RentaControl.borrarRenta(idCliente);
                System.out.println("Departamento borrado");
                return;
            }
        }
    }

}
