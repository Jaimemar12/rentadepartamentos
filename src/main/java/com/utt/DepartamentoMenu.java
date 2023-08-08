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
            while (true) {
                System.out.println("Ingresa el numero de recamaras del departamento:");
                numRecamaras = scn.nextLine();
                if (numeroRecamarasValido(numRecamaras)) {
                    break;
                }
                System.out.println("Solo se permite de 1-3 recamaras");
            }
            System.out.println("Ingresa el precio del departamento:");
            precio = scn.nextLine();
        } while (!DepartamentoControl.crearDepartamento(ubicacion, numRecamaras, precio));
        System.out.println("\nNuevo departamento creado");
    }

    private boolean numeroRecamarasValido(String numRecamaras) {
        int numero = Integer.parseInt(numRecamaras);
        return numero > 0 && numero < 4;
    }

    @Override
    public void ver() {
        while (true) {
            System.out.println("\n1. Buscar departamento");
            System.out.println("2. Ver informacion de todos los departamentos activos");
            System.out.println("3. Ver informacion de todos los departamentos inactivos");
            Scanner scn = new Scanner(System.in);
            String respuesta = scn.nextLine();
            if (respuesta.equals("1")) {
                System.out.println("\nIngresa la ubicacion del departamento");
                String ubicacion = scn.nextLine();
                Document departamento = DepartamentoControl.buscarDepartamentoPorUbicacion(ubicacion);
                if (departamento == null) {
                    System.out.println("\nNo se encontro departamento");
                    continue;
                } else {
                    mostrarDepartamentoActivo(departamento);
                    return;
                }
            } else if (respuesta.equals("2")) {
                Set<Document> departamentos = DepartamentoControl.buscarDepartamentos();
                for (Document departamento : departamentos) {
                    mostrarDepartamentoActivo(departamento);
                }
                return;
            } else if (respuesta.equals("3")) {
                Set<Document> departamentos = DepartamentoControl.buscarDepartamentos();
                for (Document departamento : departamentos) {
                    mostrarDepartamentoInactivo(departamento);
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
            Document departamento = DepartamentoControl.buscarDepartamentoPorUbicacion(ubicacion);
            if (departamento == null) {
                System.out.println("\nNo se encontro departamento");
            } else {
                System.out.println("\nQue deseas actualizar?");
                System.out.println("1. Ubicacion");
                System.out.println("2. Numero de recamaras");
                System.out.println("3. Precio\n");
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

                String valor = "";
                while (true) {
                    System.out.println("\nIngresa el nuevo valor:");
                    valor = scn.nextLine();
                    if (!variable.equals("numero_recamaras")) {
                        break;
                    }
                    if (numeroRecamarasValido(valor)) {
                        break;
                    }
                    System.out.println("Solo se permite de 1-3 recamaras");
                }
                DepartamentoControl.actualizarDepartamento(ubicacion, variable, valor);
                System.out.println("\nDepartamento editado");
                return;
            }
        }
    }

    @Override
    public void borrar() {
        while (true) {
            Scanner scn = new Scanner(System.in);
            System.out.println(
                    "\nIngresa la ubicacion del departamento que deseas borrar\nNota: Si el cliente tiene renta o cliente enralazado, estos datos seran borrados igual\n");
            String ubicacion = scn.nextLine();
            Document departamento = DepartamentoControl.buscarDepartamentoPorUbicacion(ubicacion);
            if (departamento == null) {
                System.out.println("\nNo se encontro departamento");
            } else {
                String idDepartamento = DepartamentoControl.getID(ubicacion);
                String idCliente = RentaControl.getClienteID(idDepartamento);
                String telefono = ClienteControl.getTelefono(idCliente);
                ClienteControl.borrarCliente(telefono);
                DepartamentoControl.borrarDepartamento(ubicacion);
                RentaControl.borrarRenta(idCliente);
                System.out.println("\nDepartamento borrado");
                return;
            }
        }
    }

    private void mostrarDepartamentoActivo(Document departamento) {
        if (departamento.get("estado").equals("Activo")) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\n");
            for (int i = 0; i < 80; i++) {
                stringBuilder.append("-");
            }
            stringBuilder.append("\nUbicacion:\t\t" + departamento.get("ubicacion") + "\n");
            stringBuilder.append("Numero de recamaras:\t" + departamento.get("numero_recamaras") + "\n");
            stringBuilder.append("Precio:\t\t\t$" + departamento.get("precio") + "\n");
            for (int i = 0; i < 80; i++) {
                stringBuilder.append("-");
            }
            stringBuilder.append("\n");
            System.out.println(stringBuilder.toString());
        }
    }

    private void mostrarDepartamentoInactivo(Document departamento) {
        if (departamento.get("estado").equals("Inactivo")) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\n");
            for (int i = 0; i < 80; i++) {
                stringBuilder.append("-");
            }
            stringBuilder.append("\nUbicacion:\t\t" + departamento.get("ubicacion") + "\n");
            stringBuilder.append("Numero de recamaras:\t" + departamento.get("numero_recamaras") + "\n");
            stringBuilder.append("Precio:\t\t\t$" + departamento.get("precio") + "\n");
            for (int i = 0; i < 80; i++) {
                stringBuilder.append("-");
            }
            stringBuilder.append("\n");
            System.out.println(stringBuilder.toString());
        }
    }
}
