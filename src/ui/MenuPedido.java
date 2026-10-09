package ui;

import java.util.List;
import java.util.Scanner;

import exception.ProductoNoEncontradoException;
import exception.StockInsuficienteException;
import model.Pedido;
import model.Producto;
import service.PedidoService;
import service.ProductoService;
import util.Validador;

/**
 * Capa de interfaz para todo lo relacionado con pedidos.
 *
 * Recibe también un MenuProducto para poder reutilizar
 * su aviso de stock bajo justo después de confirmar un pedido.
 */
public class MenuPedido {

    private final Scanner sc;
    private final PedidoService pedidoService;
    private final ProductoService productoService;
    private final MenuProducto menuProducto;

    public MenuPedido(Scanner sc, PedidoService pedidoService, ProductoService productoService, MenuProducto menuProducto) {
        this.sc = sc;
        this.pedidoService = pedidoService;
        this.productoService = productoService;
        this.menuProducto = menuProducto;
    }

    public void crearPedido() {
        Pedido pedido = pedidoService.crearPedidoVacio();
        boolean agregarOtro = true;

        while (agregarOtro) {
            int id = Validador.leerEntero(sc, "ID del producto a agregar: ");
            int cantidad = Validador.leerEntero(sc, "Cantidad: ");

            try {
                pedidoService.agregarLinea(pedido, id, cantidad);
                System.out.println("Producto agregado al pedido.");
            } catch (ProductoNoEncontradoException | StockInsuficienteException e) {
                // Se captura ACÁ, no se deja subir hasta Main: si falla
                // agregar una línea, queremos seguir preguntando por la
                // próxima, no cancelar el pedido entero.
                System.out.println("No se pudo agregar: " + e.getMessage());
            }

            agregarOtro = Validador.leerTexto(sc, "¿Agregar otro producto al pedido? (s/n): ").equalsIgnoreCase("s");
        }

        if (pedido.getLineas().isEmpty()) {
            System.out.println("El pedido no tiene productos. Se cancela.");
            return;
        }

        pedidoService.confirmar(pedido);
        System.out.println();
        System.out.println("Pedido confirmado:");
        System.out.println(pedido);

        // Alerta automática: confirmar un pedido es justo el momento en el
        // que el stock baja, así que es el lugar natural para avisar si
        // algún producto quedó en zona de stock bajo (o se agotó).
        System.out.println();
        menuProducto.avisarSiHayStockBajo();
    }

    public void listarPedidos() {
        List<Pedido> pedidos = pedidoService.listar();

        if (pedidos.isEmpty()) {
            System.out.println("No hay pedidos registrados todavía.");
            return;
        }

        System.out.println("--- Listado de pedidos ---");
        for (Pedido pedido : pedidos) {
            System.out.println(pedido);
            System.out.println();
        }
    }

    public void mostrarEstadisticas() {
        System.out.println("--- Estadísticas ---");
        System.out.printf("Valor total del inventario: $%.2f%n", productoService.valorTotalInventario());
        System.out.printf("Total facturado (todos los pedidos): $%.2f%n", pedidoService.totalFacturado());

        Producto masPedido = pedidoService.productoMasPedido();
        if (masPedido != null) {
            System.out.println("Producto más pedido: " + masPedido.getNombre());
        } else {
            System.out.println("Producto más pedido: todavía no hay pedidos registrados.");
        }
    }
}
