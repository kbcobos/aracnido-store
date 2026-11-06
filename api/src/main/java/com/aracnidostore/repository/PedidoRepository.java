package com.aracnidostore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aracnidostore.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    // "Usuario" + "Id" -> navega pedido.usuario.id. Más nuevos primero.
    List<Pedido> findByUsuarioIdOrderByFechaDesc(Long usuarioId);

    List<Pedido> findAllByOrderByFechaDesc();
}
