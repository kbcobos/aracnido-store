package service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import exception.ProductoNoEncontradoException;
import model.Producto;
import util.Validador;

/**
 * Encapsula la colección de productos y toda la lógica de negocio para
 * manipularla: valida los datos antes de guardarlos, busca, actualiza
 * y elimina.
 */
public class ProductoService {

    /** Umbral por defecto (en unidades) para considerar el stock "bajo". */
    public static final int UMBRAL_STOCK_BAJO_DEFAULT = 5;

    private final List<Producto> productos = new ArrayList<>();

    public Producto agregar(String nombre, double precio, int stock) {
        return agregar(new Producto(nombre, precio, stock));
    }

    /**
     * Agrega un Producto ya construido — funciona igual para un Producto
     * genérico que para una Bebida o Comida. Antes de
     * agregarlo, valida que los datos sean coherentes; si no lo son,
     * lanza una excepción y el producto NO se agrega.
     */
    public Producto agregar(Producto producto) {
        Validador.validarNombre(producto.getNombre());
        Validador.validarPrecio(producto.getPrecio());
        Validador.validarStock(producto.getStock());

        productos.add(producto);
        return producto;
    }

    public List<Producto> listar() {
        return productos;
    }

    /**
     * Búsqueda por id que garantiza un resultado: si no existe ningún
     * producto con ese id, lanza ProductoNoEncontradoException en vez de
     * devolver null.
     */
    public Producto obtenerPorId(int id) {
        for (Producto producto : productos) {
            if (producto.getId() == id) {
                return producto;
            }
        }
        throw new ProductoNoEncontradoException("No existe un producto con ID " + id);
    }

    /**
     * Búsqueda por nombre, sin distinguir mayúsculas/minúsculas.
     */
    public Producto buscarPorNombre(String nombre) {
        for (Producto producto : productos) {
            if (producto.getNombre().equalsIgnoreCase(nombre)) {
                return producto;
            }
        }
        return null;
    }

    /** Actualiza el precio de un producto, validando que sea coherente. */
    public void actualizarPrecio(int id, double nuevoPrecio) {
        Producto producto = obtenerPorId(id);
        Validador.validarPrecio(nuevoPrecio);
        producto.setPrecio(nuevoPrecio);
    }

    /** Actualiza el stock de un producto, validando que no sea negativo. */
    public void actualizarStock(int id, int nuevoStock) {
        Producto producto = obtenerPorId(id);
        Validador.validarStock(nuevoStock);
        producto.setStock(nuevoStock);
    }

    public void eliminar(int id) {
        Producto producto = obtenerPorId(id);
        productos.remove(producto);
    }

    /**
     * true si el producto tiene stock suficiente para la cantidad
     * pedida. La usa PedidoService antes de armar un pedido.
     */
    public boolean haySuficienteStock(int id, int cantidad) {
        Producto producto = obtenerPorId(id);
        return producto.getStock() >= cantidad;
    }

    /**
     * Resta stock de un producto.
     */
    public void descontarStock(int id, int cantidad) {
        Producto producto = obtenerPorId(id);
        producto.setStock(producto.getStock() - cantidad);
    }

    public void guardarEnArchivo(String ruta) {
        ProductoPersistencia.guardar(productos, ruta);
    }

    /**
     * Carga productos desde un archivo guardado en una ejecución anterior.
     * Devuelve true si el archivo existía y se cargó, false si no había
     * ningún archivo.
     */
    public boolean cargarDesdeArchivo(String ruta) {
        List<Producto> cargados = ProductoPersistencia.cargar(ruta);
        if (cargados == null) {
            return false;
        }
        for (Producto producto : cargados) {
            productos.add(producto);
        }
        return true;
    }

    /**
     * Devuelven una COPIA de la lista, ordenada por el criterio indicado.
     */
    public List<Producto> listarOrdenadoPorNombre() {
        List<Producto> copia = new ArrayList<>(productos);
        copia.sort(Comparator.comparing(Producto::getNombre, String.CASE_INSENSITIVE_ORDER));
        return copia;
    }

    public List<Producto> listarOrdenadoPorPrecio() {
        List<Producto> copia = new ArrayList<>(productos);
        copia.sort(Comparator.comparingDouble(Producto::getPrecio));
        return copia;
    }

    public List<Producto> listarOrdenadoPorStock() {
        List<Producto> copia = new ArrayList<>(productos);
        copia.sort(Comparator.comparingInt(Producto::getStock));
        return copia;
    }

    /**
     * Suma precio × stock de cada producto — el valor total, en pesos,
     * de todo lo que hay cargado en el inventario en este momento.
     */
    public double valorTotalInventario() {
        double total = 0;
        for (Producto producto : productos) {
            total += producto.getPrecio() * producto.getStock();
        }
        return total;
    }

    /**
     * Productos con stock bajo o agotado, para el panel de alertas.
     * Devuelve una copia ordenada de menor a mayor stock,
     * así lo más urgente aparece primero.
     */
    public List<Producto> listarStockBajo(int umbral) {
        List<Producto> stockBajo = new ArrayList<>();
        for (Producto producto : productos) {
            if (producto.getStock() <= umbral) {
                stockBajo.add(producto);
            }
        }
        stockBajo.sort(Comparator.comparingInt(Producto::getStock));
        return stockBajo;
    }

    /** Sobrecarga con el umbral por defecto (5 unidades). */
    public List<Producto> listarStockBajo() {
        return listarStockBajo(UMBRAL_STOCK_BAJO_DEFAULT);
    }
}
