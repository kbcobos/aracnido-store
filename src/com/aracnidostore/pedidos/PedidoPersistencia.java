package com.aracnidostore.pedidos;

import com.aracnidostore.productos.Producto;
import com.aracnidostore.productos.ProductoService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PedidoPersistencia {

    public static void guardar(List<Pedido> pedidos, String ruta) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ruta))) {
            for (Pedido pedido : pedidos) {
                writer.println("PEDIDO|" + pedido.getId());
                for (LineaPedido linea : pedido.getLineas()) {
                    writer.println("LINEA|" + linea.getProducto().getId() + "|" + linea.getCantidad());
                }
            }
        } catch (IOException e) {
            System.out.println("No se pudo guardar el archivo de pedidos: " + e.getMessage());
        }
    }

    public static List<Pedido> cargar(String ruta, ProductoService productoService) {
        if (!Files.exists(Path.of(ruta))) {
            return null;
        }

        List<Pedido> pedidos = new ArrayList<>();
        Pedido pedidoActual = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.isBlank()) {
                    continue;
                }

                String[] campos = linea.split("\\|");

                try {
                    if (campos[0].equals("PEDIDO")) {
                        pedidoActual = new Pedido(Integer.parseInt(campos[1]));
                        pedidos.add(pedidoActual);

                    } else if (campos[0].equals("LINEA") && pedidoActual != null) {
                        int productoId = Integer.parseInt(campos[1]);
                        int cantidad = Integer.parseInt(campos[2]);
                        Producto producto = productoService.buscarPorId(productoId);

                        if (producto != null) {
                            pedidoActual.agregarLinea(new LineaPedido(producto, cantidad));
                        } else {
                            System.out.println("Se omite una línea de pedido: no existe el producto con id " + productoId);
                        }
                    }
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    System.out.println("Línea de pedido inválida, se omite: " + linea);
                }
            }
        } catch (IOException e) {
            System.out.println("No se pudo leer el archivo de pedidos: " + e.getMessage());
        }

        return pedidos;
    }
}
