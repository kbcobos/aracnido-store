package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa un pedido: un id autogenerado y una lista de líneas,
 * cada una con su producto y cantidad.
 */
public class Pedido {

    private static int contador = 1;

    private final int id;
    private final List<LineaPedido> lineas = new ArrayList<>();

    public Pedido() {
        this.id = contador++;
    }

    // Constructor con id explícito, usado por PedidoPersistencia al reconstruir un pedido guardado en archivo.
    public Pedido(int id) {
        this.id = id;
        if (id >= contador) {
            contador = id + 1;
        }
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

    /**
     * Suma el subtotal de cada línea para obtener el costo total del pedido.
     */
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
