package com.aracnidostore.repository;

import com.aracnidostore.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio de categorías. Al extender JpaRepository<Categoria, Long>
 * (entidad, tipo del id) se heredan sin escribir nada: save(), findById(),
 * findAll(), delete(), count(), etc. Spring genera la implementación solo
 * al arrancar: esto es una interfaz, no hay clase que la implemente.
 */
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
