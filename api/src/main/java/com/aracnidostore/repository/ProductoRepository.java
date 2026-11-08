package com.aracnidostore.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aracnidostore.dto.ProductoConCategoria;
import com.aracnidostore.model.Producto;

/**
 * Consultas de productos.
 */
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByStockLessThan(int stock);

    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    List<Producto> findByPrecioBetween(BigDecimal min, BigDecimal max);

    List<Producto> findAllByOrderByPrecioDesc();

    List<Producto> findByCategoriaNombreIgnoreCase(String nombre);

    List<Producto> findByCategoriaId(Long categoriaId);

    Optional<Producto> findByNombreIgnoreCase(String nombre);

    @Query("SELECT SUM(p.precio * p.stock) FROM Producto p")
    BigDecimal calcularValorInventario();

    @Query("SELECT new com.aracnidostore.dto.ProductoConCategoria("
            + "p.id, p.nombre, p.precio, p.stock, c.nombre) "
            + "FROM Producto p JOIN p.categoria c")
    List<ProductoConCategoria> listarConCategoria();
}
