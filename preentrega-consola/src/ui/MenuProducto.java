package ui;

import java.util.List;
import java.util.Scanner;

import model.Bebida;
import model.Comida;
import model.Producto;
import service.ProductoService;
import util.Validador;

/**
 * Capa de interfaz: todo lo relacionado con productos que el usuario ve y escribe por consola.
 */
public class MenuProducto {

    // El Scanner y el Service se reciben por constructor.
    private final Scanner sc;
    private final ProductoService service;

    public MenuProducto(Scanner sc, ProductoService service) {
        this.sc = sc;
        this.service = service;
    }

    public void agregarProducto() {
        System.out.println("--- Nuevo producto ---");
        System.out.println("Tipo de producto: 1) Genérico   2) Bebida   3) Comida");
        String tipo = Validador.leerTexto(sc, "Elija una opción: ");

        String nombre = Validador.leerTexto(sc, "Nombre del producto: ");
        double precio = Validador.leerDouble(sc, "Precio: ");
        int stock = Validador.leerEntero(sc, "Stock inicial: ");

        // Acá se ve el polimorfismo: sin importar qué tipo se construya
        // (Producto, Bebida o Comida).
        Producto producto = switch (tipo) {
            case "2" -> crearBebida(nombre, precio, stock);
            case "3" -> crearComida(nombre, precio, stock);
            default -> new Producto(nombre, precio, stock);
        };

        // service.agregar() valida nombre/precio/stock y lanza una
        // excepción si algo está mal (la captura Main) — si eso pasa, el
        // producto NO queda agregado.
        Producto guardado = service.agregar(producto);
        System.out.println("Producto agregado con ID " + guardado.getId() + ".");
    }

    private Bebida crearBebida(String nombre, double precio, int stock) {
        double volumen = Validador.leerDouble(sc, "Volumen en litros: ");
        return new Bebida(nombre, precio, stock, volumen);
    }

    private Comida crearComida(String nombre, double precio, int stock) {
        String fechaVencimiento = Validador.leerTexto(sc, "Fecha de vencimiento (dd/mm/aaaa): ");
        return new Comida(nombre, precio, stock, fechaVencimiento);
    }

    public void listarProductos() {
        if (service.listar().isEmpty()) {
            System.out.println("No hay productos cargados todavía.");
            return;
        }

        System.out.println("Ordenar por: 1) Sin ordenar   2) Nombre   3) Precio   4) Stock");
        String criterio = Validador.leerTexto(sc, "Elija una opción: ");

        List<Producto> productos = switch (criterio) {
            case "2" -> service.listarOrdenadoPorNombre();
            case "3" -> service.listarOrdenadoPorPrecio();
            case "4" -> service.listarOrdenadoPorStock();
            default -> service.listar();
        };

        System.out.println("--- Listado de productos ---");
        for (Producto producto : productos) {
            System.out.println(producto);
        }
    }

    public void buscarActualizarProducto() {
        String texto = Validador.leerTexto(sc, "Ingrese ID o nombre del producto a buscar: ");

        Producto producto;
        try {
            int id = Integer.parseInt(texto);
            producto = service.obtenerPorId(id); // lanza ProductoNoEncontradoException si no existe
        } catch (NumberFormatException e) {
            // No era un número: se busca por nombre en su lugar.
            producto = service.buscarPorNombre(texto);
            if (producto == null) {
                System.out.println("No se encontró ningún producto con ese nombre.");
                return;
            }
        }

        System.out.println("Producto encontrado:");
        System.out.println(producto);

        String opcion = Validador.leerTexto(sc, "¿Actualizar precio (p), stock (s), o no actualizar (n)?: ").toLowerCase();

        if (opcion.equals("p")) {
            double nuevoPrecio = Validador.leerDouble(sc, "Nuevo precio: ");
            service.actualizarPrecio(producto.getId(), nuevoPrecio);
            System.out.println("Precio actualizado.");
        } else if (opcion.equals("s")) {
            int nuevoStock = Validador.leerEntero(sc, "Nuevo stock: ");
            service.actualizarStock(producto.getId(), nuevoStock);
            System.out.println("Stock actualizado.");
        }
    }

    public void eliminarProducto() {
        int id = Validador.leerEntero(sc, "ID del producto a eliminar: ");
        Producto producto = service.obtenerPorId(id); // lanza ProductoNoEncontradoException si no existe

        boolean confirma = Validador.leerTexto(sc, "¿Confirma eliminar \"" + producto.getNombre() + "\"? (s/n): ")
                .equalsIgnoreCase("s");

        if (confirma) {
            service.eliminar(id);
            System.out.println("Producto eliminado.");
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    /**
     * Panel de administración con el detalle de alertas: todos los
     * productos en stock bajo o agotado, ordenados del más urgente al
     * menos urgente.
     */
    public void mostrarAlertasStockBajo() {
        List<Producto> stockBajo = service.listarStockBajo();

        System.out.println("--- Alertas de stock bajo (umbral: " + ProductoService.UMBRAL_STOCK_BAJO_DEFAULT + " unidades) ---");

        if (stockBajo.isEmpty()) {
            System.out.println("Todo el catálogo tiene stock por encima del umbral. Sin alertas.");
            return;
        }

        for (Producto producto : stockBajo) {
            String etiqueta = producto.getStock() == 0 ? "¡AGOTADO!" : "stock bajo";
            System.out.printf("[%s] %s (ID %d) — quedan %d unidades%n",
                    etiqueta, producto.getNombre(), producto.getId(), producto.getStock());
        }
    }

    /**
     * Solo dispara el aviso si realmente hay algo para avisar.
     */
    public void avisarSiHayStockBajo() {
        List<Producto> stockBajo = service.listarStockBajo();
        if (stockBajo.isEmpty()) {
            return;
        }

        System.out.println("⚠️  Aviso: " + stockBajo.size() + " producto(s) con stock bajo o agotado:");
        for (Producto producto : stockBajo) {
            String etiqueta = producto.getStock() == 0 ? "AGOTADO" : "bajo";
            System.out.println("   - " + producto.getNombre() + " (ID " + producto.getId() + "): " + producto.getStock() + " unidades [" + etiqueta + "]");
        }
        System.out.println("   (Ver opción 8 del menú para el detalle completo.)");
        System.out.println();
    }
}
