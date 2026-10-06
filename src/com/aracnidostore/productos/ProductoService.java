package com.aracnidostore.productos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ProductoService {
    public static final int UMBRAL_STOCK_BAJO_DEFAULT = 5;

    private final List<Producto> productos = new ArrayList<>();

    public Producto agregar(String nombre, double precio, int stock) {
        return agregar(new Producto(nombre, precio, stock));
    }

    public Producto agregar(Producto producto) {
        productos.add(producto);
        return producto;
    }

    public List<Producto> listar() {
        return productos;
    }

    public Producto buscarPorId(int id) {
        for (Producto producto : productos) {
            if (producto.getId() == id) {
                return producto;
            }
        }
        return null;
    }

    public Producto buscarPorNombre(String nombre) {
        for (Producto producto : productos) {
            if (producto.getNombre().equalsIgnoreCase(nombre)) {
                return producto;
            }
        }
        return null;
    }

    public boolean actualizarPrecio(int id, double nuevoPrecio) {
        Producto producto = buscarPorId(id);
        if (producto == null || nuevoPrecio <= 0) {
            return false;
        }
        producto.setPrecio(nuevoPrecio);
        return true;
    }

    public boolean actualizarStock(int id, int nuevoStock) {
        Producto producto = buscarPorId(id);
        if (producto == null || nuevoStock < 0) {
            return false;
        }
        producto.setStock(nuevoStock);
        return true;
    }

    public boolean eliminar(int id) {
        Producto producto = buscarPorId(id);
        if (producto == null) {
            return false;
        }
        return productos.remove(producto);
    }

    public boolean haySuficienteStock(int id, int cantidad) {
        Producto producto = buscarPorId(id);
        return producto != null && producto.getStock() >= cantidad;
    }

    public void descontarStock(int id, int cantidad) {
        Producto producto = buscarPorId(id);
        if (producto != null) {
            producto.setStock(producto.getStock() - cantidad);
        }
    }

    public void guardarEnArchivo(String ruta) {
        ProductoPersistencia.guardar(productos, ruta);
    }

    public boolean cargarDesdeArchivo(String ruta) {
        List<Producto> cargados = ProductoPersistencia.cargar(ruta);
        if (cargados == null) {
            return false;
        }
        for (Producto producto : cargados) {
            agregar(producto);
        }
        return true;
    }

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

    public double valorTotalInventario() {
        double total = 0;
        for (Producto producto : productos) {
            total += producto.getPrecio() * producto.getStock();
        }
        return total;
    }

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

    public List<Producto> listarStockBajo() {
        return listarStockBajo(UMBRAL_STOCK_BAJO_DEFAULT);
    }
}
