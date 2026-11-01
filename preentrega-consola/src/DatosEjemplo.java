import exception.ProductoNoEncontradoException;
import exception.StockInsuficienteException;
import model.Bebida;
import model.Comida;
import model.Pedido;
import model.Producto;
import service.PedidoService;
import service.ProductoService;

import java.util.List;

public class DatosEjemplo {

    public static void cargar(ProductoService productoService, PedidoService pedidoService) {
        List<Producto> genericos = cargarGenericos(productoService);
        List<Bebida> bebidas = cargarBebidas(productoService);
        List<Comida> comidas = cargarComidas(productoService);

        cargarPedidos(pedidoService, genericos, bebidas, comidas);

        System.out.println("Catálogo y pedidos de ejemplo cargados.");
        System.out.println();
    }

    private static List<Producto> cargarGenericos(ProductoService productoService) {
        Producto figuraSpiderMan = productoService.agregar("Figura de acción Spider-Man", 15500.0, 20);
        Producto mascaraMilesMorales = productoService.agregar("Máscara Miles Morales", 12800.0, 15);
        Producto llaveroVenom = productoService.agregar("Llavero Venom", 3200.0, 40);
        Producto posterDuendeVerde = productoService.agregar("Poster Duende Verde", 4500.0, 10);

        return List.of(figuraSpiderMan, mascaraMilesMorales, llaveroVenom, posterDuendeVerde);
    }

    private static List<Bebida> cargarBebidas(ProductoService productoService) {
        Bebida refrescoTelarana = new Bebida("Refresco Telaraña de Oscorp", 1800.0, 30, 0.5);
        Bebida cafeDailyBugle = new Bebida("Café Daily Bugle", 2100.0, 25, 0.35);
        Bebida batidoVenom = new Bebida("Batido Simbionte de Venom", 2600.0, 18, 0.6);

        productoService.agregar(refrescoTelarana);
        productoService.agregar(cafeDailyBugle);
        productoService.agregar(batidoVenom);

        return List.of(refrescoTelarana, cafeDailyBugle, batidoVenom);
    }

    private static List<Comida> cargarComidas(ProductoService productoService) {
        Comida pizzaPeterParker = new Comida("Pizza de Peter Parker", 5200.0, 12, "15/11/2026");
        Comida hamburguesaAracnida = new Comida("Hamburguesa Arácnida", 4800.0, 14, "20/11/2026");
        Comida comboSinisterSix = new Comida("Combo Sinister Six", 8900.0, 8, "01/12/2026");

        productoService.agregar(pizzaPeterParker);
        productoService.agregar(hamburguesaAracnida);
        productoService.agregar(comboSinisterSix);

        return List.of(pizzaPeterParker, hamburguesaAracnida, comboSinisterSix);
    }

    private static void cargarPedidos(PedidoService pedidoService,
                                       List<Producto> genericos,
                                       List<Bebida> bebidas,
                                       List<Comida> comidas) {
        try {
            // Pedido genérico: merchandising variado.
            Pedido pedidoGenerico = pedidoService.crearPedidoVacio();
            pedidoService.agregarLinea(pedidoGenerico, genericos.get(0).getId(), 2); // figura Spider-Man
            pedidoService.agregarLinea(pedidoGenerico, genericos.get(2).getId(), 3); // llavero Venom
            pedidoService.agregarLinea(pedidoGenerico, genericos.get(1).getId(), 1); // máscara Miles Morales
            pedidoService.confirmar(pedidoGenerico);

            // Pedido de comida.
            Pedido pedidoComida = pedidoService.crearPedidoVacio();
            pedidoService.agregarLinea(pedidoComida, comidas.get(0).getId(), 1); // pizza
            pedidoService.agregarLinea(pedidoComida, comidas.get(1).getId(), 2); // hamburguesa
            pedidoService.confirmar(pedidoComida);

            // Pedido de bebidas.
            Pedido pedidoBebidas = pedidoService.crearPedidoVacio();
            pedidoService.agregarLinea(pedidoBebidas, bebidas.get(0).getId(), 4); // refresco
            pedidoService.agregarLinea(pedidoBebidas, bebidas.get(1).getId(), 2); // café
            pedidoService.agregarLinea(pedidoBebidas, bebidas.get(2).getId(), 1); // batido
            pedidoService.confirmar(pedidoBebidas);

        } catch (StockInsuficienteException | ProductoNoEncontradoException e) {
            System.out.println("No se pudieron cargar todos los pedidos de ejemplo: " + e.getMessage());
        }
    }
}
