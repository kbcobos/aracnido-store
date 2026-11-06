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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aracnidostore.dto.ProductoRequest;
import com.aracnidostore.dto.ProductoResponse;
import com.aracnidostore.model.Producto;
import com.aracnidostore.service.ProductoService;

import jakarta.validation.Valid;

/** Endpoints REST de productos: /api/productos */
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    // GET /api/productos            -> todos
    // GET /api/productos?nombre=web -> filtra por nombre
    @GetMapping
    public List<ProductoResponse> listar(@RequestParam(required = false) String nombre) {
        List<Producto> productos = (nombre == null || nombre.isBlank())
                ? service.listar()
                : service.buscarPorNombre(nombre);
        return productos.stream().map(ProductoResponse::from).toList();
    }

    // GET /api/productos/stock-bajo            -> usa el mínimo configurado (5)
    // GET /api/productos/stock-bajo?limite=10  -> stock <= 10, el más escaso primero
    @GetMapping("/stock-bajo")
    public List<ProductoResponse> stockBajo(@RequestParam(required = false) Integer limite) {
        return service.stockBajo(limite).stream().map(ProductoResponse::from).toList();
    }

    // GET /api/productos/1
    @GetMapping("/{id}")
    public ProductoResponse obtener(@PathVariable Long id) {
        return ProductoResponse.from(service.obtener(id));
    }

    // POST /api/productos -> 201 Created + header Location
    @PostMapping
    public ResponseEntity<ProductoResponse> crear(@Valid @RequestBody ProductoRequest req) {
        Producto creado = service.crear(req);
        return ResponseEntity.created(URI.create("/api/productos/" + creado.getId()))
                .body(ProductoResponse.from(creado));
    }

    // PUT /api/productos/1
    @PutMapping("/{id}")
    public ProductoResponse actualizar(@PathVariable Long id,
                                       @Valid @RequestBody ProductoRequest req) {
        return ProductoResponse.from(service.actualizar(id, req));
    }

    // DELETE /api/productos/1 -> 204 No Content
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
