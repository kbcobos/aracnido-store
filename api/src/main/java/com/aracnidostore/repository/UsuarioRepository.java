package com.aracnidostore.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aracnidostore.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmailIgnoreCase(String email);
}
