package com.aracnidostore.backend.repository;

import com.aracnidostore.backend.model.EstadoPedido;
import com.aracnidostore.backend.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByUsuarioOrderByFechaDesc(String usuario);

    List<Pedido> findByEstado(EstadoPedido estado);

    long countByEstado(EstadoPedido estado);
}
