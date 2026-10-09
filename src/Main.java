import exception.ProductoNoEncontradoException;
import exception.StockInsuficienteException;
import service.PedidoService;
import service.ProductoService;
import ui.MenuPedido;
import ui.MenuProducto;
import util.Validador;

import java.nio.charset.Charset;
import java.util.Scanner;

public class Main {

    private static final String RUTA_PRODUCTOS = "productos.txt";
    private static final String RUTA_PEDIDOS = "pedidos.txt";

    public static void main(String[] args) {
        Charset codificacionConsola = Charset.forName(System.getProperty("native.encoding"));
        Scanner sc = new Scanner(System.in, codificacionConsola);

        ProductoService productoService = new ProductoService();
        PedidoService pedidoService = new PedidoService(productoService);
        MenuProducto menuProducto = new MenuProducto(sc, productoService);
        MenuPedido menuPedido = new MenuPedido(sc, pedidoService, productoService, menuProducto);

        boolean habiaDatosGuardados = productoService.cargarDesdeArchivo(RUTA_PRODUCTOS);
        if (habiaDatosGuardados) {
            pedidoService.cargarDesdeArchivo(RUTA_PEDIDOS);
            System.out.println("Catálogo y pedidos cargados desde la ejecución anterior.");
            System.out.println();
        } else {
            DatosEjemplo.cargar(productoService, pedidoService);
        }

        menuProducto.avisarSiHayStockBajo();

        int opcion;

        do {
            mostrarMenu();
            opcion = Validador.leerEntero(sc, "Elija una opción: ");

            try {
                switch (opcion) {
                    case 1 -> menuProducto.agregarProducto();
                    case 2 -> menuProducto.listarProductos();
                    case 3 -> menuProducto.buscarActualizarProducto();
                    case 4 -> menuProducto.eliminarProducto();
                    case 5 -> menuPedido.crearPedido();
                    case 6 -> menuPedido.listarPedidos();
                    case 7 -> menuPedido.mostrarEstadisticas();
                    case 8 -> menuProducto.mostrarAlertasStockBajo();
                    case 9 -> {
                        productoService.guardarEnArchivo(RUTA_PRODUCTOS);
                        pedidoService.guardarEnArchivo(RUTA_PEDIDOS);
                        System.out.println("Datos guardados. ¡Gracias por usar Arácnido Store!");
                    }
                    default -> System.out.println("Opción inválida. Elija un número del 1 al 9.");
                }
            } catch (ProductoNoEncontradoException | StockInsuficienteException e) {
                System.out.println(e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }

            System.out.println();
        } while (opcion != 9);

        sc.close();
    }

    private static void mostrarMenu() {
        System.out.println("========== SISTEMA DE GESTIÓN - ARÁCNIDO STORE ==========");
        System.out.println();
        System.out.println("1) Agregar producto");
        System.out.println("2) Listar productos");
        System.out.println("3) Buscar/Actualizar producto");
        System.out.println("4) Eliminar producto");
        System.out.println("5) Crear un pedido");
        System.out.println("6) Listar pedidos");
        System.out.println("7) Ver estadísticas");
        System.out.println("8) Ver alertas de stock bajo");
        System.out.println("9) Salir");
        System.out.println();
    }
}
