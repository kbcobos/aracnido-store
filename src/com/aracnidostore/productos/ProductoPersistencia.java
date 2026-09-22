package com.aracnidostore.productos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ProductoPersistencia {

    public static void guardar(List<Producto> productos, String ruta) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ruta))) {
            for (Producto producto : productos) {
                writer.println(producto.toFileString());
            }
        } catch (IOException e) {
            System.out.println("No se pudo guardar el archivo de productos: " + e.getMessage());
        }
    }

    public static List<Producto> cargar(String ruta) {
        if (!Files.exists(Path.of(ruta))) {
            return null;
        }

        List<Producto> productos = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.isBlank()) {
                    continue;
                }
                Producto producto = fromLinea(linea);
                if (producto != null) {
                    productos.add(producto);
                }
            }
        } catch (IOException e) {
            System.out.println("No se pudo leer el archivo de productos: " + e.getMessage());
        }

        return productos;
    }

    private static Producto fromLinea(String linea) {
        String[] campos = linea.split("\\|");
        String tipo = campos[0];

        try {
            int id = Integer.parseInt(campos[1]);
            String nombre = campos[2];
            double precio = Double.parseDouble(campos[3]);
            int stock = Integer.parseInt(campos[4]);

            return switch (tipo) {
                case "BEBIDA" -> new Bebida(id, nombre, precio, stock, Double.parseDouble(campos[5]));
                case "COMIDA" -> new Comida(id, nombre, precio, stock, campos[5]);
                default -> new Producto(id, nombre, precio, stock);
            };
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.out.println("Línea de producto inválida, se omite: " + linea);
            return null;
        }
    }
}
