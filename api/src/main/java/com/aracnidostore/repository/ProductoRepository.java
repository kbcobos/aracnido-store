package com.aracnidostore.repository;

import com.aracnidostore.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio de productos. Además de los métodos heredados, define uno
 * propio solo con el NOMBRE del método: Spring Data lo lee y arma la
 * consulta sola.
 *
 * Es el reemplazo de ProductoService.buscarPorNombre() de la preentrega,
 * pero ahora busca por coincidencia parcial y delega el trabajo a MySQL.
 */
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    // Productos con stock menor o igual al límite, los más escasos primero.
    List<Producto> findByStockLessThanEqualOrderByStockAsc(int limite);
}
