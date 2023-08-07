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
                return;
            }
            System.out.println("\nIngresa la ubicacion del departamento:");
            ubicacion = scn.nextLine();
            idDepartamento = DepartamentoControl.getID(ubicacion);
            if (idDepartamento == null) {
                System.out.println("\nNo se encontro departamento");
                return;
            }
            System.out.println("\nIngresa la fecha de inicio de la renta:");
            fechaInicio = scn.nextLine();
            System.out.println("Ingresa la fecha final de la renta:");
            fechaFin = scn.nextLine();
            System.out.println("Ingresa el monto de renta:");
            montoRenta = scn.nextLine();
        } while (!RentaControl.crearRenta(fechaInicio, fechaFin, montoRenta, idDepartamento, idCliente));
        System.out.println("\nNueva renta creada");
    }

    @Override
    public void ver() {
        while (true) {
            System.out.println("\n1. Buscar renta");
            System.out.println("2. Ver informacion de todas las rentas activas");
            System.out.println("3. Ver informacion de todas las rentas inactivas\n");
            Scanner scn = new Scanner(System.in);
            String respuesta = scn.nextLine();
            if (respuesta.equals("1")) {
                System.out.println("\nIngresa el telefono del cliente");
                String telefono = scn.nextLine();
                String idCliente = ClienteControl.getID(telefono);
                if (idCliente == null) {
                    System.out.println("\nNo se encontro cliente");
                    return;
                } else {
                    Document renta = RentaControl.buscarRentaPorCliente(idCliente);
                    if (renta == null) {
                        System.out.println("\nNo se encontro renta");
                    } else {
                        mostrarRentaActiva(renta);
                    }
                    return;
                }
            } else if (respuesta.equals("2")) {
                Set<Document> rentas = RentaControl.buscarRentas();
                for (Document renta : rentas) {
                    mostrarRentaActiva(renta);
                }
                return;
            } else if (respuesta.equals("3")) {
                Set<Document> rentas = RentaControl.buscarRentas();
                for (Document renta : rentas) {
                    mostrarRentaInactiva(renta);
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
                System.out.println("\nNo se encontro cliente");
                return;
            } else {
                Document renta = RentaControl.buscarRentaPorCliente(idCliente);
                if (renta == null) {
                    System.out.println("\nNo se encontro renta");
                } else {
                    System.out.println("\nQue deseas actualizar?");
                    System.out.println("1. Fecha de inicio");
                    System.out.println("2. Fecha final");
                    System.out.println("3. Monto de renta");
                    System.out.println("4. Motivo de cancelacion");
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
                            variable = "motivo";
                            break;
                    }

                    System.out.println("\nIngresa el nuevo valor:");
                    String valor = scn.nextLine();
                    RentaControl.actualizarRenta(idCliente, variable, valor);
                    System.out.println("\nRenta editada");
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
                    "\nIngresa el telefono del cliente que para eliminar la renta\nNota: todos datos enralazados seran borrados igual\n");
            String telefono = scn.nextLine();
            String idCliente = ClienteControl.getID(telefono);
            if (idCliente == null) {
                System.out.println("\nNo se encontro cliente");
            } else {
                Document renta = RentaControl.buscarRentaPorCliente(idCliente);
                if (renta == null) {
                    System.out.println("\nNo se encontro renta");
                } else {
                    System.out.println(
                            "\nIngresa el motivo de cancelacion");
                    String motivo = scn.nextLine();
                    RentaControl.actualizarRenta(idCliente, "motivo", motivo);
                    String idDepartamento = RentaControl.getDepartamentoID(idCliente);
                    String ubicacion = DepartamentoControl.getUbicacion(idDepartamento);
                    ClienteControl.borrarCliente(telefono);
                    DepartamentoControl.borrarDepartamento(ubicacion);
                    RentaControl.borrarRenta(idCliente);
                    System.out.println("\nRenta borrado");
                    return;
                }
            }
        }
    }

    private void mostrarRentaActiva(Document renta) {
        if (renta.get("estado").equals("Activo")) {
            StringBuilder stringBuilder = new StringBuilder();

            Document cliente = ClienteControl.buscarClientePorID(renta.get("id_cliente").toString());
            Document departamento = DepartamentoControl
                    .buscarDepartamentoPorID(renta.get("id_departamento").toString());
            stringBuilder.append("\n");
            for (int i = 0; i < 80; i++) {
                stringBuilder.append("-");
            }
            stringBuilder.append("\nNombre:\t\t" + cliente.get("nombre") + "\n");
            stringBuilder.append("Apellido:\t" + cliente.get("apellido") + "\n");
            stringBuilder.append("Direccion:\t" + cliente.get("direccion") + "\n");
            stringBuilder.append("Correo:\t\t" + cliente.get("correo") + "\n");
            stringBuilder.append("Telefono:\t" + cliente.get("telefono") + "\n");
            stringBuilder.append("\nUbicacion:\t\t" + departamento.get("ubicacion") + "\n");
            stringBuilder.append("Numero de recamaras:\t" + departamento.get("numero_recamaras") + "\n");
            stringBuilder.append("Precio:\t\t\t$" + departamento.get("precio") + "\n");
            stringBuilder.append("\nFecha de inicio:\t" + renta.get("fecha_inicio") + "\n");
            stringBuilder.append("Fecha de fin:\t\t" + renta.get("fecha_fin") + "\n");
            stringBuilder.append("Monto de renta:\t\t$" + renta.get("monto_renta") + "\n");
            for (int i = 0; i < 80; i++) {
                stringBuilder.append("-");
            }
            stringBuilder.append("\n");
            System.out.println(stringBuilder.toString());
        }
    }

    private void mostrarRentaInactiva(Document renta) {
        if (renta.get("estado").equals("Inactivo")) {
            StringBuilder stringBuilder = new StringBuilder();

            Document cliente = ClienteControl.buscarClientePorID(renta.get("id_cliente").toString());
            Document departamento = DepartamentoControl
                    .buscarDepartamentoPorID(renta.get("id_departamento").toString());
            stringBuilder.append("\n");
            for (int i = 0; i < 80; i++) {
                stringBuilder.append("-");
            }
            stringBuilder.append("\nNombre:\t\t" + cliente.get("nombre") + "\n");
            stringBuilder.append("Apellido:\t" + cliente.get("apellido") + "\n");
            stringBuilder.append("Direccion:\t" + cliente.get("direccion") + "\n");
            stringBuilder.append("Correo:\t\t" + cliente.get("correo") + "\n");
            stringBuilder.append("Telefono:\t" + cliente.get("telefono") + "\n");
            stringBuilder.append("\nUbicacion:\t\t" + departamento.get("ubicacion") + "\n");
            stringBuilder.append("Numero de recamaras:\t" + departamento.get("numero_recamaras") + "\n");
            stringBuilder.append("Precio:\t\t\t$" + departamento.get("precio") + "\n");
            stringBuilder.append("\nFecha de inicio:\t" + renta.get("fecha_inicio") + "\n");
            stringBuilder.append("Fecha de fin:\t\t" + renta.get("fecha_fin") + "\n");
            stringBuilder.append("Monto de renta:\t\t$" + renta.get("monto_renta") + "\n");
            for (int i = 0; i < 80; i++) {
                stringBuilder.append("-");
            }
            stringBuilder.append("\n");
            System.out.println(stringBuilder.toString());
        }
    }

}
