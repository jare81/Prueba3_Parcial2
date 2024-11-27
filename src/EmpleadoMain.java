/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author Dell
 */
public class EmpleadoMain {

    public static void main(String[] args) throws IOException {
        Scanner entrada = new Scanner(System.in).useDelimiter("\n");
        int opcion = 0;
        EmpleadoManager admin = new EmpleadoManager();
        try {

            do {
                System.out.println("\nMENU");
                System.out.println("1 - Agregar Empleado");
                System.out.println("2 - Listar Empleado No Despedidos");
                System.out.println("3 - Agregar venta de empleado");
                System.out.println("4 - Pagar al empleado");
                System.out.println("5 - Despedir al empleado");
                System.out.println("6 - Imprimir Datos de empleado");
                System.out.println("7 - Salir");

                System.out.print("Escoja una opcion: ");
                opcion = entrada.nextInt();

                switch (opcion) {
                    case 1:
                        System.out.println("\nAGREGAR EMPLEADO\n");

                        System.out.print("Ingrese Nombre: ");
                        String name = entrada.next();
                        System.out.print("Ingrese salario: ");
                        double salario = entrada.nextDouble();

                        admin.addEmployye(name, salario);

                        break;

                    case 2:
                        System.out.println("\nLISTAR EMPLEADOS NO DESPEDIDOS\n");
                        admin.employeeList();
                        break;

                    case 3:
                        System.out.println("\nAGREGAR VENTA DE EMPLEADO\n");
                        System.out.print("Ingrese el codigo del empleado: ");
                        int cod = entrada.nextInt();
                        System.out.print("Ingrese el monto de la venta: ");
                        double monto = entrada.nextDouble();

                        admin.addSaleToEmployee(opcion, monto);
                        break;

                    case 4:
                        System.out.println("\nPAGAR AL EMPLEADO\n");
                        System.out.print("Ingrese el codigo del empleado: ");
                        int cods = entrada.nextInt();

                        admin.payEmployee(cods);

                        break;

                    case 5:
                        System.out.println("\nDESPEDIR AL EMPLEADO\n");
                        System.out.print("Ingrese el codigo del empleado: ");
                        int code = entrada.nextInt();

                        if (admin.fireEmployee(code)) {
                            System.out.println("El empleado ha sido despedido.");
                        } else {
                            System.out.println("No se pudo despedir al empleado. Verifique");
                        }

                        break;

                    case 6:
                        System.out.println("\nIMPRIMIR DATOS DE EMPLEADO\n");
                        System.out.print("Ingrese el codigo del empleado: ");
                        int codes = entrada.nextInt();
                        
                        admin.printEmployee(codes);
                        break;

                    default:
                        System.out.println("Opción no válida. Intente de nuevo.");
                        break;

                }

            } while (opcion != 7);

        } catch (InputMismatchException e) {
            System.out.println("Error de  " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error de  " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error de  " + e.getMessage());
        }

    }
}
