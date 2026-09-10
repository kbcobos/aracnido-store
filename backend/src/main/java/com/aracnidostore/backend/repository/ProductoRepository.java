package com.aracnidostore.backend.repository;

import com.aracnidostore.backend.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    List<Producto> findByStockLessThanEqual(Integer stockMinimo);

    List<Producto> findByCategoriaId(Long categoriaId);

    boolean existsByCategoriaId(Long categoriaId);
}
