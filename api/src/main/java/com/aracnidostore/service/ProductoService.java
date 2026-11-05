package com.aracnidostore.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aracnidostore.dto.ProductoRequest;
import com.aracnidostore.exception.CategoriaNoEncontradaException;
import com.aracnidostore.exception.ProductoNoEncontradoException;
import com.aracnidostore.model.Categoria;
import com.aracnidostore.model.Producto;
import com.aracnidostore.repository.CategoriaRepository;
import com.aracnidostore.repository.ProductoRepository;

/**
 * Lógica de negocio de productos. Es el equivalente a ProductoService de la
 * preentrega, pero en vez de una lista en memoria usa el repositorio (MySQL).
 */
@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    // Spring inyecta las dependencias por constructor.
    public ProductoService(ProductoRepository productoRepository,
                           CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional(readOnly = true)
    public List<Producto> listar() {
        return productoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Producto> buscarPorNombre(String texto) {
        return productoRepository.findByNombreContainingIgnoreCase(texto);
    }

    @Transactional(readOnly = true)
    public Producto obtener(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNoEncontradoException(id));
    }

    @Transactional
    public Producto crear(ProductoRequest req) {
        Categoria categoria = buscarCategoria(req.categoriaId());
        Producto p = new Producto(req.nombre(), req.descripcion(), req.precio(),
                req.stock(), req.imagenUrl(), categoria);
        return productoRepository.save(p);
    }

    @Transactional
    public Producto actualizar(Long id, ProductoRequest req) {
        Producto p = obtener(id);
        p.setNombre(req.nombre());
        p.setDescripcion(req.descripcion());
        p.setPrecio(req.precio());
        p.setStock(req.stock());
        p.setImagenUrl(req.imagenUrl());
        p.setCategoria(buscarCategoria(req.categoriaId()));
        return productoRepository.save(p);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new ProductoNoEncontradoException(id);
        }
        productoRepository.deleteById(id);
        // flush fuerza el DELETE ahora: si el producto está en algún pedido,
        // la base rechaza el borrado y el error sale acá (y no más tarde).
        productoRepository.flush();
    }

    private Categoria buscarCategoria(Long categoriaId) {
        return categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new CategoriaNoEncontradaException(categoriaId));
    }
}
