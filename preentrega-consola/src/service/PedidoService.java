package service;

import exception.StockInsuficienteException;
import model.LineaPedido;
import model.Pedido;
import model.Producto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Encapsula la colección de pedidos y la lógica para armarlos.
 */
public class PedidoService {

    private final List<Pedido> pedidos = new ArrayList<>();
    private final ProductoService productoService;

    public PedidoService(ProductoService productoService) {
        this.productoService = productoService;
    }

    public Pedido crearPedidoVacio() {
        return new Pedido();
    }

    /**
     * Agrega una línea a un pedido que todavía se está armando.
     * Pensado para llamarse una vez por cada producto
     * que el usuario quiera sumar al pedido.
     */
    public void agregarLinea(Pedido pedido, int productoId, int cantidad) {
        Producto producto = productoService.obtenerPorId(productoId);

        if (!productoService.haySuficienteStock(productoId, cantidad)) {
            throw new StockInsuficienteException(producto.getNombre(), producto.getStock(), cantidad);
        }

        productoService.descontarStock(productoId, cantidad);
        pedido.agregarLinea(new LineaPedido(producto, cantidad));
    }

    /**
     * Guarda el pedido ya armado en la colección de pedidos confirmados.
     * El stock de cada línea ya se descontó al llamar a agregarLinea(),
     * así que acá solo queda registrarlo.
     */
    public void confirmar(Pedido pedido) {
        pedidos.add(pedido);
    }

    public List<Pedido> listar() {
        return pedidos;
    }

    public void guardarEnArchivo(String ruta) {
        PedidoPersistencia.guardar(pedidos, ruta);
    }

    /**
     * Carga pedidos desde un archivo guardado en una ejecución anterior.
     *
     * Devuelve true si el archivo existía y se cargó, false si no había
     * ningún archivo guardado.
     */
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

    /**
     * Suma el total de todos los pedidos confirmados.
     */
    public double totalFacturado() {
        double total = 0;
        for (Pedido pedido : pedidos) {
            total += pedido.calcularTotal();
        }
        return total;
    }

    /**
     * Recorre todos los pedidos sumando cuántas unidades se pidieron de
     * cada producto, y devuelve el que tiene el total más alto.
     * Devuelve null si todavía no hay pedidos.
     */
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
