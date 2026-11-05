package com.aracnidostore.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aracnidostore.dto.CategoriaRequest;
import com.aracnidostore.dto.CategoriaResponse;
import com.aracnidostore.model.Categoria;
import com.aracnidostore.service.CategoriaService;

import jakarta.validation.Valid;

/** Endpoints REST de categorías: /api/categorias */
@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService service;

    public CategoriaController(CategoriaService service) {
        this.service = service;
    }

    @GetMapping
    public List<CategoriaResponse> listar() {
        return service.listar().stream().map(CategoriaResponse::from).toList();
    }

    @GetMapping("/{id}")
    public CategoriaResponse obtener(@PathVariable Long id) {
        return CategoriaResponse.from(service.obtener(id));
    }

    @PostMapping
    public ResponseEntity<CategoriaResponse> crear(@Valid @RequestBody CategoriaRequest req) {
        Categoria creada = service.crear(req);
        return ResponseEntity.created(URI.create("/api/categorias/" + creada.getId()))
                .body(CategoriaResponse.from(creada));
    }

    @PutMapping("/{id}")
    public CategoriaResponse actualizar(@PathVariable Long id,
                                        @Valid @RequestBody CategoriaRequest req) {
        return CategoriaResponse.from(service.actualizar(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
