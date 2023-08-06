package com.utt;

import java.util.Scanner;

public class ProgramaRentaDepartamentos {
    public static void main(String[] args) {
        ClienteMenu cliente = new ClienteMenu();
        DepartamentoMenu departamento = new DepartamentoMenu();
        RentaMenu renta = new RentaMenu();

        System.out.println("Bienvenido a la administracion de datos de Tijuana Rental Homes");
        while (true) {
            System.out.println("\nSelecciona una de las opciones para iniciar");
            switch (verOpcionesMenu()) {
                case "1":
                    while (true) {
                        boolean continuar = true;
                        switch (verOpcionesCliente()) {
                            case "1":
                                cliente.crear();
                                break;
                            case "2":
                                cliente.ver();
                                break;
                            case "3":
                                cliente.editar();
                                break;
                            case "4":
                                cliente.borrar();
                                break;
                            case "5":
                                continuar = false;
                                break;
                        }
                        if (!continuar) {
                            break;
                        }
                    }
                    break;
                case "2":
                    while (true) {
                        boolean continuar = true;
                        switch (verOpcionesDepartamento()) {
                            case "1":
                                departamento.crear();
                                break;
                            case "2":
                                departamento.ver();
                                break;
                            case "3":
                                departamento.editar();
                                break;
                            case "4":
                                departamento.borrar();
                                break;
                            case "5":
                                continuar = false;
                                break;
                        }
                        if (!continuar) {
                            break;
                        }
                    }
                    break;
                case "3":
                    while (true) {
                        boolean continuar = true;
                        switch (verOpcionesRenta()) {
                            case "1":
                                renta.crear();
                                break;
                            case "2":
                                renta.ver();
                                break;
                            case "3":
                                renta.editar();
                                break;
                            case "4":
                                renta.borrar();
                                break;
                            case "5":
                                continuar = false;
                                break;
                        }
                        if (!continuar) {
                            break;
                        }
                    }
                    break;
                case "4":
                    System.exit(0);
                    break;
            }
        }
    }

    private static String verOpcionesMenu() {
        System.out.println("\n1. Ver menu de clientes");
        System.out.println("2. Ver menu de departamentos");
        System.out.println("3. Ver menu de renta");
        System.out.println("4. Terminar Programa");
        Scanner scn = new Scanner(System.in);
        String respuesta = scn.nextLine();
        return respuesta;
    }

    private static String verOpcionesCliente() {
        System.out.println("\n1. Crear nuevo cliente");
        System.out.println("2. Ver clientes");
        System.out.println("3. Editar cliente");
        System.out.println("4. Borrar cliente");
        System.out.println("5. Volver a menu principal");
        Scanner scn = new Scanner(System.in);
        String respuesta = scn.nextLine();
        return respuesta;
    }

    private static String verOpcionesDepartamento() {
        System.out.println("\n1. Crear nuevo departamento");
        System.out.println("2. Ver departamentos");
        System.out.println("3. Editar departamento");
        System.out.println("4. Borrar departamento");
        System.out.println("5. Volver a menu principal");
        Scanner scn = new Scanner(System.in);
        String respuesta = scn.nextLine();
        return respuesta;
    }

    private static String verOpcionesRenta() {
        System.out.println("\n1. Crear nueva renta");
        System.out.println("2. Ver rentas");
        System.out.println("3. Editar renta");
        System.out.println("4. Borrar renta");
        System.out.println("5. Volver a menu principal");
        Scanner scn = new Scanner(System.in);
        String respuesta = scn.nextLine();
        return respuesta;
    }
}
