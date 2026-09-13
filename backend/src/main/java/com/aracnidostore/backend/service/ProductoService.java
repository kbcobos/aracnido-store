package com.aracnidostore.backend.service;

import com.aracnidostore.backend.dto.ProductoRequestDTO;
import com.aracnidostore.backend.dto.ProductoResponseDTO;
import com.aracnidostore.backend.exception.ResourceNotFoundException;
import com.aracnidostore.backend.exception.StockInsuficienteException;
import com.aracnidostore.backend.model.Categoria;
import com.aracnidostore.backend.model.Producto;
import com.aracnidostore.backend.repository.CategoriaRepository;
import com.aracnidostore.backend.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoService(ProductoRepository productoRepository,
                            CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public List<ProductoResponseDTO> listarTodos() {
        return productoRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public ProductoResponseDTO obtenerPorId(Long id) {
        Producto producto = buscarEntidadPorId(id);
        return toResponseDTO(producto);
    }

    public List<ProductoResponseDTO> buscar(String query) {
        String texto = query == null ? "" : query.trim();

        try {
            Long id = Long.parseLong(texto);
            return productoRepository.findById(id)
                    .map(this::toResponseDTO)
                    .map(List::of)
                    .orElse(List.of());
        } catch (NumberFormatException noEsUnNumero) {
            return productoRepository.findByNombreContainingIgnoreCase(texto)
                    .stream()
                    .map(this::toResponseDTO)
                    .toList();
        }
    }

    public List<ProductoResponseDTO> productosConStockBajo(int umbral) {
        return productoRepository.findByStockLessThanEqual(umbral)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public ProductoResponseDTO crear(ProductoRequestDTO dto) {
        Categoria categoria = buscarCategoriaPorId(dto.getCategoriaId());

        Producto producto = new Producto(
                dto.getNombre(),
                dto.getDescripcion(),
                dto.getPrecio(),
                categoria,
                dto.getImagenUrl(),
                dto.getStock());

        Producto guardado = productoRepository.save(producto);
        return toResponseDTO(guardado);
    }

    public ProductoResponseDTO actualizar(Long id, ProductoRequestDTO dto) {
        Producto producto = buscarEntidadPorId(id);
        Categoria categoria = buscarCategoriaPorId(dto.getCategoriaId());

        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setCategoria(categoria);
        producto.setImagenUrl(dto.getImagenUrl());
        producto.setStock(dto.getStock());

        Producto actualizado = productoRepository.save(producto);
        return toResponseDTO(actualizado);
    }

    public void eliminar(Long id) {
        Producto producto = buscarEntidadPorId(id);
        productoRepository.delete(producto);
    }

    public void descontarStock(Long productoId, int cantidad) {
        Producto producto = buscarEntidadPorId(productoId);
        if (producto.getStock() < cantidad) {
            throw new StockInsuficienteException(producto.getNombre(), producto.getStock(), cantidad);
        }
        producto.setStock(producto.getStock() - cantidad);
        productoRepository.save(producto);
    }

    public void reponerStock(Long productoId, int cantidad) {
        Producto producto = buscarEntidadPorId(productoId);
        producto.setStock(producto.getStock() + cantidad);
        productoRepository.save(producto);
    }

    private Producto buscarEntidadPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", id));
    }

    private Categoria buscarCategoriaPorId(Long categoriaId) {
        return categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", categoriaId));
    }

    private ProductoResponseDTO toResponseDTO(Producto producto) {
        return new ProductoResponseDTO(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getCategoria().getId(),
                producto.getCategoria().getNombre(),
                producto.getImagenUrl(),
                producto.getStock());
    }
}
