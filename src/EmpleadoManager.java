/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Dell
 */
public class EmpleadoManager {

    //auto generado de codigos y empleados
    private RandomAccessFile rcods, remps, ryear;

    /*
    formato Codigo.empSystem.out.println("Empleado agregado: " + name + " con código " + code);
    int code
    
    formato Empleados.emp
    int code
    String name
    double salari
    long fecha Contratacion
    long fecha despido
    
     */
    public EmpleadoManager() {
        try {
            //1- Asegurar que el folder company exista
            File mf = new File("company");
            mf.mkdir();

            //2 - Intan iar RAFs dentro del company
            //crear una extencion exclusiva para los archivos 
            rcods = new RandomAccessFile("company/codigos.emp", "rw");

            remps = new RandomAccessFile("company/empleados.emp", "rw");

            //3 - Inicializar el archivo de codigo si es nuevo
            initCodes();

        } catch (IOException e) {

        }

    }

    private void initCodes() throws IOException {
        //verificar si el archivo no pesa nada significa que esta vacio

        // puntero en ----> 0
        if (rcods.length() == 0) {
            rcods.writeInt(1);
        }
        //puntero en ----> 4
    }

    private int getCode() throws IOException {
        rcods.seek(0);
        //puntero  ---->0
        int code = rcods.readInt();
        //puntero ------>4

        rcods.seek(0);
        rcods.writeInt(code+1);
        return code;
    }

    public void addEmployye(String name, double salario) throws IOException {
        //asegurar que el puntero este en el final del archivo
        if (name == null || name.isEmpty()) {
            System.out.println("El nombre no puede estar vacio.");
            return;
        }

        remps.seek(remps.length());
        int code = getCode();
        //p 0
        remps.writeInt(code);
        //p 4
        remps.writeUTF(name);
        //p 12
        remps.writeDouble(salario);
        //p 20
        remps.writeLong(Calendar.getInstance().getTimeInMillis());
        //p 28
        remps.writeLong(0);

        //p 36  EOF
        //Asegurar crear folder y archivos individuales
        createEmployeeFolder(code);
        System.out.println("Empleado agregado: " + name + " con codigo " + code);

    }

    private String employeeFolder(int code) {
        return "company/empleado" + code;
    }

    private void createEmployeeFolder(int code) throws IOException {
        //crear folder empleado + code
        File edir = new File(employeeFolder(code));
        edir.mkdir();
        //creacr el archivo de ventas
        createYearSalesFileFor(code);
    }

    private RandomAccessFile salesFileFor(int code) throws IOException {
        String dirPadre = employeeFolder(code);

        int yearactual = Calendar.getInstance().get(Calendar.YEAR);

        String path = dirPadre + "/ventas" + yearactual + ".emp";

        return new RandomAccessFile(path, "rw");

    }

    private void createYearSalesFileFor(int code) throws IOException {
        ryear = salesFileFor(code);

        if (ryear.length() == 0) {
            for (int mes = 0; mes < 12; mes++) {
                ryear.writeDouble(0);
                ryear.writeBoolean(false);

            }
        }
    }

    private boolean isEmployeeActive(int code) throws IOException {
        remps.seek(0);

        while (remps.getFilePointer() < remps.length()) {
            int codigo = remps.readInt();
            long pos = remps.getFilePointer();
            remps.readUTF();
            remps.skipBytes(16);

            if (remps.readLong() == 0 && codigo == code) {
                remps.seek(pos); //para acceder a la informacion del usuario activo
                return true;//debemos mover el puntero a la posicion en donde esta el usuario
            }
        }
        return false;

    }

    public boolean fireEmployee(int code) throws IOException {
        if (isEmployeeActive(code)) {
            String name = remps.readUTF();
            remps.skipBytes(16);
            remps.writeLong(new Date().getTime());
            System.out.println("Despidiendo a " + name);

            return true;
        }
        return false;
    }

    public void addSaleToEmployee(int code, double monto) throws IOException {

        if (monto <= 0) {
            System.out.println("El monto de la venta debe ser mayor a cero.");
            return;
        }

        if (isEmployeeActive(code)) {
            RandomAccessFile sale = salesFileFor(code);

            int mes = Calendar.getInstance().get(Calendar.MONTH);

            int posicion = mes * 9;
            sale.seek(posicion);

            double ventas = sale.readDouble();
            boolean estatus = sale.readBoolean();

            if (estatus) {
                sale.seek(posicion);
                sale.writeDouble(ventas + monto);
                System.out.println("Venta de Lps. " + monto + " agregada al empleado " + code);
            } else {
                System.out.println("El empleado ya recibio pago por este mes.");
            }
        } 

    }

    public void payEmployee(int code) throws IOException {
        if (isEmployeeActive(code)) {

            String name = remps.readUTF();
            double salary = remps.readDouble();
            remps.skipBytes(16);

            RandomAccessFile ventas = salesFileFor(code);

            int mes = Calendar.getInstance().get(Calendar.MONTH);
            int year = Calendar.getInstance().get(Calendar.YEAR);
            int posicion = mes * 9;

            ventas.seek(posicion);
            double ventasMes = ventas.readDouble();
            boolean pagado = ventas.readBoolean();

            if (pagado) {
                System.out.println("El empleado ya recibio pago este mes.");
                return;
            }

            double comision = ventasMes * 0.10;
            double salarioBase = salary + comision;
            double deduccion = salarioBase * 0.035;
            double salarioNeto = salarioBase - deduccion;

            try (RandomAccessFile recibo = new RandomAccessFile(employeeFolder(code) + "/recibos.emp", "rw")) {
                recibo.seek(recibo.length());

                recibo.writeLong(new Date().getTime()); //fecha
                recibo.writeDouble(comision); //comison
                recibo.writeDouble(salarioBase); //sueldo abse
                recibo.writeDouble(deduccion); //deduccion
                recibo.writeDouble(salarioNeto);
                recibo.writeInt(year);
                recibo.writeInt(mes);
            }

            ventas.seek(posicion + 8);
            ventas.writeBoolean(true);

            System.out.println("Pago realizado a " + name + " para el mes " + (mes + 1)
                    + ". Sueldo neto: Lps. " + salarioNeto);

        } else {
            System.out.println("Empleado no activo. No se pago");
        }
    }

    public void employeeList() throws IOException {
        SimpleDateFormat formatFecha = new SimpleDateFormat("dd/MM/yyyy");

        remps.seek(0);
        boolean found = false;
        //P 36 < 36
        while (remps.getFilePointer() < remps.length()) {
            //p 0
            int code = remps.readInt();
            //p 4
            String name = remps.readUTF();
            //p 12
            double salary = remps.readDouble();
            //p20
            Date dateH = new Date(remps.readLong());

            long dateD = remps.readLong();
            //p 28
            if (dateD == 0) {
                found = true;
                System.out.println("Codigo: " + code + " Nombre: " + name + " Salario: Lps. " + salary + " Ingreso: " + dateH.toString());
            }
            //p 36 
        }

        if (!found) {
            System.out.println("No hay empleados activos.");
        }
    }

    public void printEmployee(int code) throws IOException {
        if (!isEmployeeActive(code)) {
            System.out.println("El empleado con código " + code + " no esta activo o no existe.");
            return;
        }

        remps.seek(0);

        boolean encontrado = false;
        Date contratacion = null;

        while (remps.getFilePointer() < remps.length()) {
            int codigo = remps.readInt();
            String name = remps.readUTF();
            double salary = remps.readDouble();
            contratacion = new Date(remps.readLong());
            long dateD = remps.readLong();

            if (codigo == code && dateD == 0) {
                encontrado = true;
                break;
            }

            if (encontrado == false) {
                System.out.println("Empleado no encontrado.");
                return;
            }

            System.out.println("Detalles del empleado:");
            System.out.println("Código: " + code);
            System.out.println("Nombre: " + name);
            System.out.println("Salario Base: Lps. " + salary);
            System.out.println("Fecha de contratación: " + contratacion);

            RandomAccessFile ventas = salesFileFor(code);
            double totalVentas = 0;
            System.out.println("Ventas anuales del año actual:");

            for (int mes = 0; mes < 12; mes++) {
                int posicion = mes * 9;
                ventas.seek(posicion);
                double ventasMes = ventas.readDouble();
                boolean pagado = ventas.readBoolean();
                totalVentas += ventasMes;
                System.out.printf("Mes %d: Lps. %.2f %s%n", (mes + 1), ventasMes, (pagado ? "(Pagado)" : "(Pendiente)"));
            }

            System.out.println("Total de ventas del año actual: Lps. " + totalVentas);

            File recibosFile = new File(employeeFolder(code) + "/recibos.emp");
            if (recibosFile.exists()) {
                try (RandomAccessFile recibos = new RandomAccessFile(recibosFile, "r")) {
                    System.out.println("Recibos históricos:");
                    while (recibos.getFilePointer() < recibos.length()) {
                        Date fecha = new Date(recibos.readLong());
                        double comision = recibos.readDouble();
                        double salarioBase = recibos.readDouble();
                        double deduccion = recibos.readDouble();
                        double salarioNeto = recibos.readDouble();
                        int year = recibos.readInt();
                        int mes = recibos.readInt();

                        System.out.printf("Fecha: %s " + fecha
                                + "\nComisión: Lps. %.2f  " + comision 
                                + "\nSalario Base: Lps. %.2f " + salarioBase
                                + "\nDeducción: Lps. %.2f " + deduccion
                                + "\nSalario Neto: Lps. %.2f " + salarioNeto 
                                +"\nAño: %d" + year + " Mes: " + (mes+1));
                                       
                    }
                }
            } else {
                System.out.println("No hay recibos históricos disponibles para este empleado.");
            }

        }
    }

}
