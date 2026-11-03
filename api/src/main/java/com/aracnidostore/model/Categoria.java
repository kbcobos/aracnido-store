package com.aracnidostore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Categoría de productos (por ejemplo "Merchandising", "Bebidas",
 * "Comida"). Es nueva respecto a la preentrega de consola: allá los
 * productos no tenían categoría, solo un "tipo" implícito por herencia
 * (Bebida/Comida). Ahora es una tabla propia, y cada Producto apunta a
 * una Categoria.
 */
@Entity
@Table(name = "categorias")
public class Categoria {

    // IDENTITY = la base la genera sola
    // (AUTO_INCREMENT en MySQL) — igual que el contador estático que
    // usaba Producto en la preentrega, pero ahora lo maneja MySQL y
    // sobrevive entre ejecuciones sin guardar nada en archivos.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // unique = true: no puede haber dos categorías con el mismo nombre.
    @Column(nullable = false, unique = true, length = 80)
    private String nombre;

    // Hibernate exige un constructor sin argumentos para poder crear
    // objetos al leer filas de la base.
    public Categoria() {
    }

    public Categoria(String nombre) {
        this.nombre = nombre;
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
}
