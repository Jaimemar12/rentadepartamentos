package com.utt;

import java.util.Scanner;
import java.util.Set;

import org.bson.Document;

import com.utt.basededatos.ClienteControl;
import com.utt.basededatos.DepartamentoControl;
import com.utt.basededatos.RentaControl;

public class RentaMenu implements Menu {

    @Override
    public void crear() {
        String fechaInicio = "";
        String fechaFin = "";
        String montoRenta = "";
        String montoPago = "";
        String ubicacion = "";
        String telefono = "";
        String idCliente = null;
        String idDepartamento = null;
        do {
            Scanner scn = new Scanner(System.in);
            System.out.println("\nIngresa el telefono del cliente:");
            telefono = scn.nextLine();
            idCliente = ClienteControl.getID(telefono);
            if (idCliente == null) {
                System.out.println("\nNo se encontro cliente");
                continue;
            }
            System.out.println("\nIngresa la ubicacion del departamento:");
            ubicacion = scn.nextLine();
            idDepartamento = DepartamentoControl.getID(ubicacion);
            if (idDepartamento == null) {
                System.out.println("\nNo se encontro departamento");
                continue;
            }
            System.out.println("\nIngresa la fecha de inicio de la renta:");
            fechaInicio = scn.nextLine();
            System.out.println("Ingresa la fecha final de la renta:");
            fechaFin = scn.nextLine();
            System.out.println("Ingresa el monto de renta:");
            montoRenta = scn.nextLine();
            System.out.println("Ingresa el monto de pago:");
            montoPago = scn.nextLine();
        } while (!RentaControl.crearRenta(fechaInicio, fechaFin, montoRenta, montoPago, idDepartamento, idCliente));
        System.out.println("Nueva renta creada");
    }

    @Override
    public void ver() {
        while (true) {
            System.out.println("\n1. Buscar renta");
            System.out.println("2. Ver informacion de todas las rentas");
            Scanner scn = new Scanner(System.in);
            String respuesta = scn.nextLine();
            if (respuesta.equals("1")) {
                System.out.println("\nIngresa el telefono del cliente");
                String telefono = scn.nextLine();
                String idCliente = ClienteControl.getID(telefono);
                if (idCliente == null) {
                    System.out.println("No se encontro cliente");
                    continue;
                } else {
                    Document renta = RentaControl.buscarRentaPorCliente(idCliente);
                    if (renta == null) {
                        System.out.println("No se encontro renta");
                    } else {
                        System.out.println(renta.toJson());
                    }
                    return;
                }
            } else if (respuesta.equals("2")) {
                Set<Document> rentas = RentaControl.buscarRentas();
                for (Document renta : rentas) {
                    System.out.println(renta);
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
            System.out.println("\nIngresa el telefono del cliente");
            String telefono = scn.nextLine();

            String idCliente = ClienteControl.getID(telefono);
            if (idCliente == null) {
                System.out.println("No se encontro cliente");
                continue;
            } else {
                Document renta = RentaControl.buscarRentaPorCliente(idCliente);
                if (renta == null) {
                    System.out.println("No se encontro renta");
                } else {
                    System.out.println("\nQue deseas actualizar?");
                    System.out.println("1. Fecha de inicio");
                    System.out.println("2. Fecha final");
                    System.out.println("3. Monto de renta");
                    System.out.println("4. Monto de pago");
                    String opcion = scn.nextLine();

                    switch (opcion) {
                        case "1":
                            variable = "fecha_inicio";
                            break;
                        case "2":
                            variable = "fecha_fin";
                            break;
                        case "3":
                            variable = "monto_renta";
                            break;
                        case "4":
                            variable = "monto_pago";
                            break;
                    }

                    System.out.println("\nIngresa el nuevo valor:");
                    String valor = scn.nextLine();
                    RentaControl.actualizarRenta(idCliente, variable, valor);
                    System.out.println("Renta editada");
                    return;
                }
            }
        }
    }

    @Override
    public void borrar() {
        while (true) {
            Scanner scn = new Scanner(System.in);
            System.out.println(
                    "Ingresa el telefono del cliente que para eliminar la renta\nNota: todos datos enralazados seran borrados igual");
            String telefono = scn.nextLine();
            String idCliente = ClienteControl.getID(telefono);
            if (idCliente == null) {
                System.out.println("No se encontro cliente");
            } else {
                Document renta = RentaControl.buscarRentaPorCliente(idCliente);
                if (renta == null) {
                    System.out.println("No se encontro renta");
                } else {
                    String idDepartamento = RentaControl.getDepartamentoID(idCliente);
                    String ubicacion = DepartamentoControl.getUbicacion(idDepartamento);
                    ClienteControl.borrarCliente(telefono);
                    DepartamentoControl.borrarDepartamento(ubicacion);
                    RentaControl.borrarRenta(idCliente);
                    System.out.println("Renta borrado");
                    return;
                }
            }
        }
    }

}
