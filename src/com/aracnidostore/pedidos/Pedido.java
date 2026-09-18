package com.aracnidostore.pedidos;

import java.util.ArrayList;
import java.util.List;

public class Pedido {

    private static int contador = 1;

    private final int id;
    private final List<LineaPedido> lineas = new ArrayList<>();

    public Pedido() {
        this.id = contador++;
    }

    public int getId() {
        return id;
    }

    public List<LineaPedido> getLineas() {
        return lineas;
    }

    public void agregarLinea(LineaPedido linea) {
        lineas.add(linea);
    }

    public double calcularTotal() {
        double total = 0;
        for (LineaPedido linea : lineas) {
            total += linea.getSubtotal();
        }
        return total;
    }

    @Override
    public String toString() {
        StringBuilder detalle = new StringBuilder();
        detalle.append("Pedido #").append(id).append(":\n");
        for (LineaPedido linea : lineas) {
            detalle.append("   - ").append(linea).append("\n");
        }
        detalle.append("Total: $").append(calcularTotal());
        return detalle.toString();
    }
}
