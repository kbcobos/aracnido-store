package model;

/**
 * Ejemplo de herencia/polimorfismo.
 * Una Bebida es un Producto que además tiene volumen en litros.
 */
public class Bebida extends Producto {

    private double volumenLitros;

    public Bebida(String nombre, double precio, int stock, double volumenLitros) {
        super(nombre, precio, stock);
        this.volumenLitros = volumenLitros;
    }

    // Constructor con id explícito, usado por ProductoPersistencia al reconstruir una Bebida guardada en archivo.
    public Bebida(int id, String nombre, double precio, int stock, double volumenLitros) {
        super(id, nombre, precio, stock);
        this.volumenLitros = volumenLitros;
    }

    public double getVolumenLitros() {
        return volumenLitros;
    }

    public void setVolumenLitros(double volumenLitros) {
        this.volumenLitros = volumenLitros;
    }

    // Sobrescribe el toString() de Producto agregando el dato propio de Bebida, en vez de reemplazarlo del todo.
    @Override
    public String toString() {
        return super.toString() + " | Tipo: Bebida | Volumen: " + volumenLitros + "L";
    }

    @Override
    public String toFileString() {
        return "BEBIDA|" + getId() + "|" + getNombre() + "|" + getPrecio() + "|" + getStock() + "|" + volumenLitros;
    }
}
