package com.aracnidostore.productos;

public class Comida extends Producto {

    private String fechaVencimiento;

    public Comida(String nombre, double precio, int stock, String fechaVencimiento) {
        super(nombre, precio, stock);
        this.fechaVencimiento = fechaVencimiento;
    }

    public Comida(int id, String nombre, double precio, int stock, String fechaVencimiento) {
        super(id, nombre, precio, stock);
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    @Override
    public String toString() {
        return super.toString() + " | Tipo: Comida | Vence: " + fechaVencimiento;
    }

    @Override
    public String toFileString() {
        return "COMIDA|" + getId() + "|" + getNombre() + "|" + getPrecio() + "|" + getStock() + "|" + fechaVencimiento;
    }
}
