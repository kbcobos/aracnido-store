package com.aracnidostore.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aracnidostore.dto.CategoriaRequest;
import com.aracnidostore.exception.CategoriaNoEncontradaException;
import com.aracnidostore.exception.RecursoDuplicadoException;
import com.aracnidostore.model.Categoria;
import com.aracnidostore.repository.CategoriaRepository;

@Service
public class CategoriaService {

    private final CategoriaRepository repository;

    public CategoriaService(CategoriaRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Categoria> listar() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Categoria obtener(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new CategoriaNoEncontradaException(id));
    }

    @Transactional
    public Categoria crear(CategoriaRequest req) {
        validarNombreLibre(req.nombre(), null);
        return repository.save(new Categoria(req.nombre().trim()));
    }

    @Transactional
    public Categoria actualizar(Long id, CategoriaRequest req) {
        Categoria c = obtener(id);
        validarNombreLibre(req.nombre(), id);
        c.setNombre(req.nombre().trim());
        return repository.save(c);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new CategoriaNoEncontradaException(id);
        }
        repository.deleteById(id);
        // Si hay productos en la categoría, MySQL rechaza el borrado y el
        // handler global lo convierte en 409.
        repository.flush();
    }

    // Falla si otra categoría (distinta de idActual) ya usa ese nombre.
    private void validarNombreLibre(String nombre, Long idActual) {
        repository.findByNombreIgnoreCase(nombre.trim()).ifPresent(existente -> {
            if (!existente.getId().equals(idActual)) {
                throw new RecursoDuplicadoException(
                        "Ya existe una categoría llamada '" + existente.getNombre() + "'");
            }
        });
    }
}
