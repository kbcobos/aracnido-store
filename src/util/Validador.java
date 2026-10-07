package util;

import java.util.Scanner;

import exception.StockInsuficienteException;

/**
 * Las validaciones (validarNombre, validarPrecio, validarStock) no
 * devuelven nada: si el dato es válido, simplemente no hacen nada; si
 * no lo es, lanzan la excepción correspondiente.
 * 
 * Las lecturas (leerEntero, leerDouble, leerTexto) sí devuelven un
 * valor, y las dos numéricas repiten la pregunta hasta que el usuario
 * ingresa algo con el formato correcto.
 */
public class Validador {

    public static void validarNombre(String nombre) {
        // Un nombre nulo o vacío no representa un producto válido.
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
    }

    public static void validarPrecio(double precio) {
        if (precio <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a 0.");
        }
    }

    public static void validarStock(int stock) {
        // Stock negativo no es válido.
        // igual que cuando falta stock para armar un pedido.
        if (stock < 0) {
            throw new StockInsuficienteException("El stock no puede ser negativo.");
        }
    }

    public static int leerEntero(Scanner sc, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número entero. Intente de nuevo.");
            }
        }
    }

    public static double leerDouble(Scanner sc, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Double.parseDouble(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número (puede tener decimales). Intente de nuevo.");
            }
        }
    }

    public static String leerTexto(Scanner sc, String mensaje) {
        System.out.print(mensaje);
        return sc.nextLine().trim();
    }
}
