package com.aracnidostore.backend.dto;

public class CategoriaResponseDTO {

    private Long id;
    private String nombre;
    private long cantidadProductos;

    public CategoriaResponseDTO() {
    }

    public CategoriaResponseDTO(Long id, String nombre, long cantidadProductos) {
        this.id = id;
        this.nombre = nombre;
        this.cantidadProductos = cantidadProductos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public long getCantidadProductos() {
        return cantidadProductos;
    }

    public void setCantidadProductos(long cantidadProductos) {
        this.cantidadProductos = cantidadProductos;
    }
}
