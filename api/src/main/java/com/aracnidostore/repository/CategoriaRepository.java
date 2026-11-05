package com.aracnidostore.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aracnidostore.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    // Para evitar categorías repetidas sin importar mayúsculas/minúsculas.
    Optional<Categoria> findByNombreIgnoreCase(String nombre);
}
