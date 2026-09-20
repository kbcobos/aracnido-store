package com.aracnidostore.productos;

import java.util.ArrayList;
import java.util.List;

public class ProductoService {

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
}
