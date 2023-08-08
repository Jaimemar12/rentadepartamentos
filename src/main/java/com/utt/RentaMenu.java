package com.utt;

import java.time.LocalDate;
import java.time.Period;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

import org.bson.Document;

import com.utt.basededatos.ClienteControl;
import com.utt.basededatos.DepartamentoControl;
import com.utt.basededatos.RentaControl;

public class RentaMenu implements Menu {

    @Override
    public void crear() {
        String fechaInicio = "";
        String fechaFin = "";
        String balance = "";
        String ubicacion = "";
        String telefono = "";
        String idCliente = null;
        String idDepartamento = null;
        String precio = "";
        Document departamento = null;
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
            departamento = DepartamentoControl.buscarDepartamentoPorID(idDepartamento);
            precio = departamento.get("precio").toString();
            System.out.println("\nIngresa la fecha de inicio de la renta: (aaaa-mm-dd)");
            fechaInicio = scn.nextLine();
            System.out.println("Ingresa la fecha final de la renta: (aaaa-mm-dd)");
            fechaFin = scn.nextLine();
            int meses = Period.between(LocalDate.parse(fechaInicio),
                    LocalDate.parse(fechaFin)).getMonths();
            balance = String.valueOf(meses * Integer.parseInt(precio));
            pagar(String.valueOf(Integer.parseInt(precio) * 2), "deposito y primer mes de pago");

        } while (!RentaControl.crearRenta(fechaInicio, fechaFin, balance, precio, idDepartamento, idCliente));
        System.out.println("\nNueva renta creada");
    }

    private void pagar(String precio, String razon) {
        System.out.println("Se requiere $" + precio + " de " + razon + ".");
        while (true) {
            Scanner scn = new Scanner(System.in);
            System.out.println("Ingresa numero de cuenta de banco.");
            String numeroDeCuenta = scn.nextLine();
            if (numeroDeCuentaEsValido(numeroDeCuenta)) {
                System.out.println("Ingresa el pin de cuenta.");
                String pin = scn.nextLine();
                if (numeroDePinEsValido(pin)) {
                    System.out.println("Pago se a recivido");
                    break;
                }
                System.out.println("Numero de pin invalido");
            }
            System.out.println("Numero de cuenta invalido");
        }
    }

    @Override
    public void ver() {
        while (true) {
            System.out.println("\n1. Buscar renta");
            System.out.println("2. Ver informacion de todas las rentas activas");
            System.out.println("3. Ver informacion de todas las rentas inactivas\n");
            System.out.println("4. Pagar renta");
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
            } else if (respuesta.equals("4")) {
                Document departamento = null;
                String precio = "";
                while (true) {

                    System.out.println("\nIngresa la ubicacion del departamento");
                    String ubicacion = scn.nextLine();
                    departamento = DepartamentoControl.buscarDepartamentoPorUbicacion(ubicacion);
                    if (departamento == null) {
                        System.out.println("\nNo se encontro departamento");
                    } else {
                        System.out.println("Hubo dias de retraso? (si/no)");
                        respuesta = scn.nextLine();
                        if (respuesta.toLowerCase().equals("si")) {
                            System.out.println("Ingresa el numero de dias de retraso:");
                            String dias = scn.nextLine();
                            int departamentoPrecio = Integer.parseInt(departamento.get("precio").toString());
                            precio = String.valueOf(
                                    departamentoPrecio + (Integer.parseInt(dias) * (departamentoPrecio * .05)));
                            break;
                        } else if (respuesta.toLowerCase().equals("no")) {
                            precio = departamento.get("precio").toString();
                            break;
                        }
                    }
                }
                pagar(precio, "renta");
                String idCliente = RentaControl.getClienteID(departamento.get("_id").toString());
                Document renta = RentaControl.buscarRentaPorCliente(idCliente);
                String nuevoBalance = String
                        .valueOf(Integer.parseInt(renta.get("balance").toString()) - Integer.parseInt(precio));
                RentaControl.actualizarRenta(idCliente, "balance", nuevoBalance);
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
                    System.out.println("3. Balance");
                    System.out.println("4. Deposito");
                    System.out.println("5. Motivo de cancelacion");
                    String opcion = scn.nextLine();

                    switch (opcion) {
                        case "1":
                            variable = "fecha_inicio";
                            break;
                        case "2":
                            variable = "fecha_fin";
                            break;
                        case "3":
                            variable = "balance";
                            break;
                        case "4":
                            variable = "deposito";
                            break;
                        case "5":
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
                    while (true) {
                        System.out.println(
                                "\nSe regresara el deposito? (si/no)");
                        String respuesta = scn.nextLine();
                        if (respuesta.toLowerCase().equals("si")) {
                            RentaControl.actualizarRenta(idCliente, "deposito", "0");
                            break;
                        } else if (respuesta.toLowerCase().equals("no")) {
                            break;
                        }
                        System.out.println("Ingresa si o no");
                    }
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
            stringBuilder.append("Deposito:\t\t$" + renta.get("deposito") + "\n");
            stringBuilder.append("Balance:\t\t$" + renta.get("balance") + "\n");
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
            stringBuilder.append("Deposito:\t\t\t$" + renta.get("deposito") + "\n");
            stringBuilder.append("Balance:\t\t$" + renta.get("balance") + "\n");
            stringBuilder.append("Motivo de cancelacion:\t" + renta.get("motivo") + "\n");
            for (int i = 0; i < 80; i++) {
                stringBuilder.append("-");
            }
            stringBuilder.append("\n");
            System.out.println(stringBuilder.toString());
        }
    }

    private boolean numeroDeCuentaEsValido(String numeroDeCuenta) {
        String expresion = "^[0-9]{9,18}$";
        Pattern patron = Pattern.compile(expresion);
        return patron.matcher(numeroDeCuenta).matches();
    }

    private boolean numeroDePinEsValido(String pin) {
        String expresion = "^[0-9]{4,6}$";
        Pattern patron = Pattern.compile(expresion);
        return patron.matcher(pin).matches();
    }

}
