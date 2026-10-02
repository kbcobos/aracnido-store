package com.aracnidostore.pedidos;

import com.aracnidostore.excepciones.StockInsuficienteException;
import com.aracnidostore.productos.Producto;
import com.aracnidostore.productos.ProductoService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PedidoService {

    private final List<Pedido> pedidos = new ArrayList<>();
    private final ProductoService productoService;

    public PedidoService(ProductoService productoService) {
        this.productoService = productoService;
    }

    public Pedido crearPedidoVacio() {
        return new Pedido();
    }

    public void agregarLinea(Pedido pedido, int productoId, int cantidad) throws StockInsuficienteException {
        Producto producto = productoService.buscarPorId(productoId);

        if (producto == null) {
            throw new StockInsuficienteException("No existe un producto con ID " + productoId);
        }
        if (!productoService.haySuficienteStock(productoId, cantidad)) {
            throw new StockInsuficienteException(producto.getNombre(), producto.getStock(), cantidad);
        }

        productoService.descontarStock(productoId, cantidad);
        pedido.agregarLinea(new LineaPedido(producto, cantidad));
    }

    public void confirmar(Pedido pedido) {
        pedidos.add(pedido);
    }

    public List<Pedido> listar() {
        return pedidos;
    }

    public void guardarEnArchivo(String ruta) {
        PedidoPersistencia.guardar(pedidos, ruta);
    }

    public boolean cargarDesdeArchivo(String ruta) {
        List<Pedido> cargados = PedidoPersistencia.cargar(ruta, productoService);
        if (cargados == null) {
            return false;
        }
        for (Pedido pedido : cargados) {
            confirmar(pedido);
        }
        return true;
    }

    public double totalFacturado() {
        double total = 0;
        for (Pedido pedido : pedidos) {
            total += pedido.calcularTotal();
        }
        return total;
    }

    public Producto productoMasPedido() {
        Map<Producto, Integer> totalesPorProducto = new HashMap<>();

        for (Pedido pedido : pedidos) {
            for (LineaPedido linea : pedido.getLineas()) {
                Producto producto = linea.getProducto();
                int cantidadAcumulada = totalesPorProducto.getOrDefault(producto, 0);
                totalesPorProducto.put(producto, cantidadAcumulada + linea.getCantidad());
            }
        }

        Producto masPedido = null;
        int maxCantidad = 0;

        for (Map.Entry<Producto, Integer> entrada : totalesPorProducto.entrySet()) {
            if (entrada.getValue() > maxCantidad) {
                maxCantidad = entrada.getValue();
                masPedido = entrada.getKey();
            }
        }

        return masPedido;
    }
}
