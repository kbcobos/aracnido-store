import com.aracnidostore.excepciones.StockInsuficienteException;
import com.aracnidostore.pedidos.Pedido;
import com.aracnidostore.pedidos.PedidoService;
import com.aracnidostore.productos.Bebida;
import com.aracnidostore.productos.Comida;
import com.aracnidostore.productos.Producto;
import com.aracnidostore.productos.ProductoService;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Charset codificacionConsola = Charset.forName(System.getProperty("native.encoding"));
        Scanner scanner = new Scanner(System.in, codificacionConsola);
        ProductoService productoService = new ProductoService();
        PedidoService pedidoService = new PedidoService(productoService);
        DatosEjemplo.cargar(productoService, pedidoService);

        boolean salir = false;

        while (!salir) {
            mostrarMenu();
            String entrada = scanner.nextLine();

            try {
                int opcion = Integer.parseInt(entrada);

                switch (opcion) {
                    case 1 -> agregarProducto(scanner, productoService);
                    case 2 -> listarProductos(productoService);
                    case 3 -> buscarActualizarProducto(scanner, productoService);
                    case 4 -> eliminarProducto(scanner, productoService);
                    case 5 -> crearPedido(scanner, pedidoService);
                    case 6 -> listarPedidos(pedidoService);
                    case 7 -> {
                        salir = true;
                        System.out.println("¡Gracias por usar Arácnido Store!");
                    }
                    default -> System.out.println("Opción inválida. Elija un número del 1 al 7.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Ingrese un número del 1 al 7.");
            }

            System.out.println();
        }

        scanner.close();
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
        System.out.println("7) Salir");
        System.out.println();
        System.out.print("Elija una opción: ");
    }

    private static void agregarProducto(Scanner scanner, ProductoService productoService) {
        System.out.println("Tipo de producto: 1) Genérico   2) Bebida   3) Comida");
        System.out.print("Elija una opción: ");
        String tipo = scanner.nextLine().trim();

        System.out.print("Nombre del producto: ");
        String nombre = scanner.nextLine().trim();

        if (nombre.isEmpty()) {
            System.out.println("El nombre no puede estar vacío. Operación cancelada.");
            return;
        }

        double precio;
        try {
            System.out.print("Precio: ");
            precio = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Precio inválido. Debe ser un número. Operación cancelada.");
            return;
        }
        if (precio <= 0) {
            System.out.println("El precio debe ser mayor a 0. Operación cancelada.");
            return;
        }

        int stock;
        try {
            System.out.print("Stock inicial: ");
            stock = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Stock inválido. Debe ser un número entero. Operación cancelada.");
            return;
        }
        if (stock < 0) {
            System.out.println("El stock no puede ser negativo. Operación cancelada.");
            return;
        }

        Producto producto = switch (tipo) {
            case "2" -> crearBebida(scanner, nombre, precio, stock);
            case "3" -> crearComida(scanner, nombre, precio, stock);
            default -> new Producto(nombre, precio, stock);
        };

        productoService.agregar(producto);
        System.out.println("Producto agregado con ID " + producto.getId() + ".");
    }

    private static Bebida crearBebida(Scanner scanner, String nombre, double precio, int stock) {
        try {
            System.out.print("Volumen en litros: ");
            double volumen = Double.parseDouble(scanner.nextLine());
            return new Bebida(nombre, precio, stock, volumen);
        } catch (NumberFormatException e) {
            System.out.println("Volumen inválido. Se usa 0 por defecto.");
            return new Bebida(nombre, precio, stock, 0);
        }
    }

    private static Comida crearComida(Scanner scanner, String nombre, double precio, int stock) {
        System.out.print("Fecha de vencimiento (dd/mm/aaaa): ");
        String fechaVencimiento = scanner.nextLine().trim();
        return new Comida(nombre, precio, stock, fechaVencimiento);
    }

    private static void listarProductos(ProductoService productoService) {
        List<Producto> productos = productoService.listar();

        if (productos.isEmpty()) {
            System.out.println("No hay productos cargados todavía.");
            return;
        }

        System.out.println("--- Listado de productos ---");
        for (Producto producto : productos) {
            System.out.println(producto);
        }
    }

    private static void buscarActualizarProducto(Scanner scanner, ProductoService productoService) {
        System.out.print("Ingrese ID o nombre del producto a buscar: ");
        String texto = scanner.nextLine().trim();

        Producto producto;
        try {
            int id = Integer.parseInt(texto);
            producto = productoService.buscarPorId(id);
        } catch (NumberFormatException e) {
            // No era un número: se busca por nombre en su lugar.
            producto = productoService.buscarPorNombre(texto);
        }

        if (producto == null) {
            System.out.println("No se encontró ningún producto con ese ID o nombre.");
            return;
        }

        System.out.println("Producto encontrado:");
        System.out.println(producto);

        System.out.print("¿Actualizar precio (p), stock (s), o no actualizar (n)?: ");
        String opcion = scanner.nextLine().trim().toLowerCase();

        if (opcion.equals("p")) {
            actualizarPrecio(scanner, productoService, producto);
        } else if (opcion.equals("s")) {
            actualizarStock(scanner, productoService, producto);
        }
    }

    private static void actualizarPrecio(Scanner scanner, ProductoService productoService, Producto producto) {
        try {
            System.out.print("Nuevo precio: ");
            double nuevoPrecio = Double.parseDouble(scanner.nextLine());
            boolean actualizado = productoService.actualizarPrecio(producto.getId(), nuevoPrecio);
            System.out.println(actualizado ? "Precio actualizado." : "No se pudo actualizar: el precio debe ser mayor a 0.");
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido. No se actualizó el precio.");
        }
    }

    private static void actualizarStock(Scanner scanner, ProductoService productoService, Producto producto) {
        try {
            System.out.print("Nuevo stock: ");
            int nuevoStock = Integer.parseInt(scanner.nextLine());
            boolean actualizado = productoService.actualizarStock(producto.getId(), nuevoStock);
            System.out.println(actualizado ? "Stock actualizado." : "No se pudo actualizar: el stock no puede ser negativo.");
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido. No se actualizó el stock.");
        }
    }

    private static void eliminarProducto(Scanner scanner, ProductoService productoService) {
        try {
            System.out.print("ID del producto a eliminar: ");
            int id = Integer.parseInt(scanner.nextLine());

            Producto producto = productoService.buscarPorId(id);
            if (producto == null) {
                System.out.println("No existe un producto con ese ID.");
                return;
            }

            System.out.print("¿Confirma eliminar \"" + producto.getNombre() + "\"? (s/n): ");
            String confirmacion = scanner.nextLine().trim().toLowerCase();

            if (confirmacion.equals("s")) {
                productoService.eliminar(id);
                System.out.println("Producto eliminado.");
            } else {
                System.out.println("Operación cancelada.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ID inválido. Debe ser un número entero.");
        }
    }

    private static void crearPedido(Scanner scanner, PedidoService pedidoService) {
        Pedido pedido = pedidoService.crearPedidoVacio();
        boolean agregarOtro = true;

        while (agregarOtro) {
            try {
                System.out.print("ID del producto a agregar: ");
                int id = Integer.parseInt(scanner.nextLine());
                System.out.print("Cantidad: ");
                int cantidad = Integer.parseInt(scanner.nextLine());

                pedidoService.agregarLinea(pedido, id, cantidad);
                System.out.println("Producto agregado al pedido.");
            } catch (NumberFormatException e) {
                System.out.println("ID o cantidad inválidos. Intente de nuevo.");
            } catch (StockInsuficienteException e) {
                System.out.println("No se pudo agregar: " + e.getMessage());
            }

            System.out.print("¿Agregar otro producto al pedido? (s/n): ");
            agregarOtro = scanner.nextLine().trim().equalsIgnoreCase("s");
        }

        if (pedido.getLineas().isEmpty()) {
            System.out.println("El pedido no tiene productos. Se cancela.");
            return;
        }

        pedidoService.confirmar(pedido);
        System.out.println();
        System.out.println("Pedido confirmado:");
        System.out.println(pedido);
    }

    private static void listarPedidos(PedidoService pedidoService) {
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
}
