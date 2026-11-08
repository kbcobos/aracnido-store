package com.aracnidostore.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aracnidostore.dto.ProductoConCategoria;
import com.aracnidostore.dto.ProductoRequest;
import com.aracnidostore.exception.CategoriaNoEncontradaException;
import com.aracnidostore.exception.ProductoDuplicadoException;
import com.aracnidostore.exception.ProductoNoEncontradoException;
import com.aracnidostore.model.Categoria;
import com.aracnidostore.model.Producto;
import com.aracnidostore.repository.CategoriaRepository;
import com.aracnidostore.repository.ProductoRepository;

/**
 * Lógica de negocio de productos.
 */
@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final int stockMinimo;

    public ProductoService(ProductoRepository productoRepository,
                           CategoriaRepository categoriaRepository,
                           @Value("${aracnido.stock.minimo:5}") int stockMinimo) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
        this.stockMinimo = stockMinimo;
    }

    /**
     * Alerta de stock bajo: productos con MENOS de "limite" unidades.
     */
    @Transactional(readOnly = true)
    public List<Producto> stockBajo(Integer limite) {
        int umbral = (limite != null) ? limite : stockMinimo;
        return productoRepository.findByStockLessThan(umbral);
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

    @Transactional(readOnly = true)
    public List<Producto> buscarPorPrecio(BigDecimal min, BigDecimal max) {
        if (min.compareTo(max) > 0) {
            throw new IllegalArgumentException("El precio mínimo no puede ser mayor al máximo.");
        }
        return productoRepository.findByPrecioBetween(min, max);
    }

    @Transactional(readOnly = true)
    public List<Producto> listarPorPrecioDescendente() {
        return productoRepository.findAllByOrderByPrecioDesc();
    }

    @Transactional(readOnly = true)
    public List<Producto> listarPorNombreDeCategoria(String categoria) {
        return productoRepository.findByCategoriaNombreIgnoreCase(categoria);
    }

    // Si la categoría no existe responde 404.
    @Transactional(readOnly = true)
    public List<Producto> listarPorCategoriaId(Long categoriaId) {
        if (!categoriaRepository.existsById(categoriaId)) {
            throw new CategoriaNoEncontradaException(categoriaId);
        }
        return productoRepository.findByCategoriaId(categoriaId);
    }

    @Transactional(readOnly = true)
    public BigDecimal calcularValorInventario() {
        BigDecimal valor = productoRepository.calcularValorInventario();
        return (valor != null) ? valor : BigDecimal.ZERO;
    }

    @Transactional(readOnly = true)
    public List<ProductoConCategoria> listarConCategoria() {
        return productoRepository.listarConCategoria();
    }

    @Transactional
    public Producto crear(ProductoRequest req) {
        validarNombreUnico(req.nombre(), null);
        Categoria categoria = buscarCategoria(req.categoriaId());
        Producto p = new Producto(req.nombre(), req.descripcion(), req.precio(),
                req.stock(), req.imagenUrl(), categoria);
        return productoRepository.save(p);
    }

    @Transactional
    public Producto actualizar(Long id, ProductoRequest req) {
        Producto p = obtener(id);
        validarNombreUnico(req.nombre(), id);
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
        productoRepository.flush();
    }

    private void validarNombreUnico(String nombre, Long idActual) {
        productoRepository.findByNombreIgnoreCase(nombre.trim()).ifPresent(existente -> {
            if (!existente.getId().equals(idActual)) {
                throw new ProductoDuplicadoException(existente.getNombre());
            }
        });
    }

    private Categoria buscarCategoria(Long categoriaId) {
        return categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new CategoriaNoEncontradaException(categoriaId));
    }
}
