package model;

/**
 * Representa un producto del catálogo. El id se autogenera con un
 * contador estático: cada vez que se crea un Producto nuevo, toma el
 * valor actual del contador y lo incrementa para el próximo — así nunca
 * se repite un id, sin que haya que pasarlo a mano al crear el objeto.
 */
public class Producto {

    private static int contador = 1;

    private final int id;
    private String nombre;
    private double precio;
    private int stock;

    public Producto(String nombre, double precio, int stock) {
        this.id = contador++;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    /**
     * Constructor con id explícito: lo usa ProductoPersistencia al
     * reconstruir productos guardados en un archivo, para que conserven
     * el mismo id que tenían antes de guardarse (necesario para que los
     * pedidos guardados, que referencian productos por id, sigan
     * apuntando al producto correcto al recargar).
     */
    public Producto(int id, String nombre, double precio, int stock) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        if (id >= contador) {
            contador = id + 1;
        }
    }

    // "id" no tiene setter: una vez creado el producto, su id no cambia.

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "ID: " + id +
                " | Nombre: " + nombre +
                " | Precio: $" + precio +
                " | Stock: " + stock;
    }

    /**
     * Representación en una línea de texto, separada por "|", para guardar en archivo.
     */
    public String toFileString() {
        return "PRODUCTO|" + id + "|" + nombre + "|" + precio + "|" + stock;
    }
}
