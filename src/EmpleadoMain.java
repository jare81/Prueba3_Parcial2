/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author Dell
 */
public class EmpleadoMain {
    public static void main(String[] args) throws IOException {
        Scanner entrada = new Scanner(System.in).useDelimiter("\n");
        int opcion=0;
        EmpleadoManager admin = new EmpleadoManager();
        
        do{
        System.out.println("MENU");
        System.out.println("1 - Agregar Empleado");
        System.out.println("2 - Listar Empleado No Despedidos");
        System.out.println("3 - Agregar venta de empleado");
        System.out.println("4 - Pagar al empleado");
        System.out.println("5 - Despedir al empleado");
        System.out.println("6 - Salir");
        
        System.out.println("Escoja una opcion: ");
        opcion = entrada.nextInt();
        
        switch(opcion){
            case 1:
                 System.out.println("\nAGREGAR EMPLEADO\n");

                System.out.println("Ingrese Nombre");
                String name = entrada.next();
                System.out.println("Ingrese salario");
                double salario = entrada.nextDouble();
                
                admin.addEmployye(name, salario);
                
                break;
                
            case 2:
                System.out.println("\nLISTAR EMPLEADOS NO DESPEDIDOS\n");
                admin.employeeList();
                break;
                
                
                
            case 3:
                System.out.println("\nAGREGAR VENTA DE EMPLEADO\n");
                System.out.println("Ingrese el codigo del empleado");
                int cod = entrada.nextInt();
                System.out.println("Ingrese el salario del empleado");
                double sal = entrada.nextDouble();
                admin.addSaleToEmployee(opcion, sal);
                break;
                
            case 4:
                System.out.println("\nPAGAR AL EMPLEADO\n");
                System.out.println("Ingrese el codigo del empleado");
                int cods = entrada.nextInt();
                
                break;
                
            case 5:
                System.out.println("\nDESPEDIR AL EMPLEADO\n");
                System.out.println("Ingrese el codigo del empleado");
                int dd = entrada.nextInt();
                break;
                
        }
        
        
        }while(opcion!=6);
        
        
        
        
    }
}
