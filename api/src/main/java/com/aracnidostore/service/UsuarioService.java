package com.aracnidostore.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aracnidostore.dto.UsuarioRequest;
import com.aracnidostore.exception.RecursoDuplicadoException;
import com.aracnidostore.exception.UsuarioNoEncontradoException;
import com.aracnidostore.model.Usuario;
import com.aracnidostore.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Usuario> listar() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario obtener(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException(id));
    }

    @Transactional
    public Usuario crear(UsuarioRequest req) {
        validarEmailLibre(req.email(), null);
        return repository.save(new Usuario(req.nombre().trim(), req.email().trim()));
    }

    @Transactional
    public Usuario actualizar(Long id, UsuarioRequest req) {
        Usuario u = obtener(id);
        validarEmailLibre(req.email(), id);
        u.setNombre(req.nombre().trim());
        u.setEmail(req.email().trim());
        return repository.save(u);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new UsuarioNoEncontradoException(id);
        }
        repository.deleteById(id);
        // Si el usuario tiene pedidos, MySQL rechaza el borrado (409).
        repository.flush();
    }

    private void validarEmailLibre(String email, Long idActual) {
        repository.findByEmailIgnoreCase(email.trim()).ifPresent(existente -> {
            if (!existente.getId().equals(idActual)) {
                throw new RecursoDuplicadoException(
                        "Ya existe un usuario con el email " + existente.getEmail());
            }
        });
    }
}
