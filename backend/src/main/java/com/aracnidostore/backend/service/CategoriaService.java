package com.aracnidostore.backend.service;

import com.aracnidostore.backend.dto.CategoriaRequestDTO;
import com.aracnidostore.backend.dto.CategoriaResponseDTO;
import com.aracnidostore.backend.exception.CategoriaEnUsoException;
import com.aracnidostore.backend.exception.NombreDuplicadoException;
import com.aracnidostore.backend.exception.ResourceNotFoundException;
import com.aracnidostore.backend.model.Categoria;
import com.aracnidostore.backend.repository.CategoriaRepository;
import com.aracnidostore.backend.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;

    public CategoriaService(CategoriaRepository categoriaRepository,
                             ProductoRepository productoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.productoRepository = productoRepository;
    }

    public List<CategoriaResponseDTO> listarTodas() {
        return categoriaRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public CategoriaResponseDTO obtenerPorId(Long id) {
        Categoria categoria = buscarEntidadPorId(id);
        return toResponseDTO(categoria);
    }

    public CategoriaResponseDTO crear(CategoriaRequestDTO dto) {
        if (categoriaRepository.existsByNombreIgnoreCase(dto.getNombre())) {
            throw new NombreDuplicadoException(dto.getNombre());
        }

        Categoria categoria = new Categoria(dto.getNombre());
        Categoria guardada = categoriaRepository.save(categoria);
        return toResponseDTO(guardada);
    }

    public void eliminar(Long id) {
        Categoria categoria = buscarEntidadPorId(id);

        if (productoRepository.existsByCategoriaId(id)) {
            throw new CategoriaEnUsoException(categoria.getNombre());
        }

        categoriaRepository.delete(categoria);
    }

    private Categoria buscarEntidadPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", id));
    }

    private CategoriaResponseDTO toResponseDTO(Categoria categoria) {
        long cantidadProductos = productoRepository.countByCategoriaId(categoria.getId());
        return new CategoriaResponseDTO(categoria.getId(), categoria.getNombre(), cantidadProductos);
    }
}
