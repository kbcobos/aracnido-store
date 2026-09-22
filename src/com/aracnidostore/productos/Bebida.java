package com.aracnidostore.productos;

public class Bebida extends Producto {

    private double volumenLitros;

    public Bebida(String nombre, double precio, int stock, double volumenLitros) {
        super(nombre, precio, stock);
        this.volumenLitros = volumenLitros;
    }

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

    @Override
    public String toString() {
        return super.toString() + " | Tipo: Bebida | Volumen: " + volumenLitros + "L";
    }

    @Override
    public String toFileString() {
        return "BEBIDA|" + getId() + "|" + getNombre() + "|" + getPrecio() + "|" + getStock() + "|" + volumenLitros;
    }
}
