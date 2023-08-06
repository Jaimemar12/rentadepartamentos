package com.utt;

import java.util.Scanner;
import java.util.Set;

import org.bson.Document;

import com.utt.basededatos.ClienteControl;
import com.utt.basededatos.DepartamentoControl;
import com.utt.basededatos.RentaControl;

public class ClienteMenu implements Menu {

    @Override
    public void crear() {
        String nombre = "";
        String apellido = "";
        String direccion = "";
        String telefono = "";
        String correo = "";
        do {
            Scanner scn = new Scanner(System.in);
            System.out.println("\nIngresa el nombre del cliente:");
            nombre = scn.nextLine();
            System.out.println("Ingresa el apellido del cliente:");
            apellido = scn.nextLine();
            System.out.println("Ingresa el direccion del cliente:");
            direccion = scn.nextLine();
            System.out.println("Ingresa el telefono del cliente:");
            telefono = scn.nextLine();
            System.out.println("Ingresa el correo del cliente:");
            correo = scn.nextLine();
        } while (!ClienteControl.crearCliente(nombre, apellido, direccion, telefono, correo));
        System.out.println("\nNuevo cliente creado");
    }

    @Override
    public void ver() {
        while (true) {
            System.out.println("\n1. Buscar cliente");
            System.out.println("2. Ver informacion de todos los clientes\n");
            Scanner scn = new Scanner(System.in);
            String respuesta = scn.nextLine();
            if (respuesta.equals("1")) {
                System.out.println("\nIngresa el telefono del cliente");
                String telefono = scn.nextLine();
                Document cliente = ClienteControl.buscarClientePorTelefono(telefono);
                if (cliente == null) {
                    System.out.println("\nNo se encontro cliente");
                    return;
                } else {
                    mostrarCliente(cliente);
                    return;
                }
            } else if (respuesta.equals("2")) {
                Set<Document> clientes = ClienteControl.buscarClientes();
                for (Document cliente : clientes) {
                    mostrarCliente(cliente);
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
            Document cliente = ClienteControl.buscarClientePorTelefono(telefono);
            if (cliente == null) {
                System.out.println("\nNo se encontro cliente");
            } else {
                System.out.println("\nQue deseas actualizar?");
                System.out.println("1. Nombre");
                System.out.println("2. Apellido");
                System.out.println("3. Direccion");
                System.out.println("4. Correo");
                System.out.println("5. Telefono\n");
                String opcion = scn.nextLine();

                switch (opcion) {
                    case "1":
                        variable = "nombre";
                        break;
                    case "2":
                        variable = "apellido";
                        break;
                    case "3":
                        variable = "direccion";
                        break;
                    case "4":
                        variable = "correo";
                        break;
                    case "5":
                        variable = "telefono";
                        break;
                }

                System.out.println("\nIngresa el nuevo valor:");
                String valor = scn.nextLine();
                ClienteControl.actualizarCliente(telefono, variable, valor);
                System.out.println("\nCliente editado");
                return;
            }
        }
    }

    @Override
    public void borrar() {
        while (true) {
            Scanner scn = new Scanner(System.in);
            System.out.println(
                    "\nIngresa el telefono del cliente que deseas borrar\nNota: Si el cliente tiene renta o departamento enralazado estos datos seran borrados igual\n");
            String telefono = scn.nextLine();
            Document cliente = ClienteControl.buscarClientePorTelefono(telefono);
            if (cliente == null) {
                System.out.println("\nNo se encontro cliente\n");
            } else {
                String idCliente = ClienteControl.getID(telefono);
                String idDepartamento = RentaControl.getDepartamentoID(idCliente);
                String ubicacion = DepartamentoControl.getUbicacion(idDepartamento);
                ClienteControl.borrarCliente(telefono);
                DepartamentoControl.borrarDepartamento(ubicacion);
                RentaControl.borrarRenta(idCliente);
                System.out.println("\nCliente borrado");
                return;
            }
        }
    }

    // BORRAR ESTOOOOOOOOOOOOOOOOOOOOOO: Si quieren agregar id solo poner un nuevo append y usar _id
    private void mostrarCliente(Document cliente) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n");
        for (int i = 0; i < 80; i++) {
            stringBuilder.append("-");
        }
        stringBuilder.append("\nNombre:\t\t" + cliente.get("nombre") + "\n");
        stringBuilder.append("Apellido:\t" + cliente.get("apellido") + "\n");
        stringBuilder.append("Direccion:\t" + cliente.get("direccion") + "\n");
        stringBuilder.append("Correo:\t\t" + cliente.get("correo") + "\n");
        stringBuilder.append("Telefono:\t" + cliente.get("telefono") + "\n");
        for (int i = 0; i < 80; i++) {
            stringBuilder.append("-");
        }
        stringBuilder.append("\n");
        System.out.println(stringBuilder.toString());
    }
}
