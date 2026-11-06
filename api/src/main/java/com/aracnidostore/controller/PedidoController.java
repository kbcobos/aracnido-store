package com.aracnidostore.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aracnidostore.dto.CambioEstadoRequest;
import com.aracnidostore.dto.PedidoRequest;
import com.aracnidostore.dto.PedidoResponse;
import com.aracnidostore.service.PedidoService;

import jakarta.validation.Valid;

/**
 * Endpoints de pedidos. Se mapea desde /api porque incluye dos rutas:
 * /api/pedidos/... y el historial por usuario /api/usuarios/{id}/pedidos.
 */
@RestController
@RequestMapping("/api")
public class PedidoController {

    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    // POST /api/pedidos -> 201; 400 si falta stock; 404 si no existe usuario/producto
    @PostMapping("/pedidos")
    public ResponseEntity<PedidoResponse> crear(@Valid @RequestBody PedidoRequest req) {
        PedidoResponse creado = service.crear(req);
        return ResponseEntity.created(URI.create("/api/pedidos/" + creado.id())).body(creado);
    }

    @GetMapping("/pedidos")
    public List<PedidoResponse> listar() {
        return service.listar();
    }

    @GetMapping("/pedidos/{id}")
    public PedidoResponse obtener(@PathVariable Long id) {
        return service.obtener(id);
    }

    // PATCH /api/pedidos/1/estado   { "estado": "CONFIRMADO" }
    @PatchMapping("/pedidos/{id}/estado")
    public PedidoResponse cambiarEstado(@PathVariable Long id,
                                        @Valid @RequestBody CambioEstadoRequest req) {
        return service.cambiarEstado(id, req.estado());
    }

    // GET /api/usuarios/5/pedidos -> historial del usuario
    @GetMapping("/usuarios/{usuarioId}/pedidos")
    public List<PedidoResponse> historial(@PathVariable Long usuarioId) {
        return service.listarPorUsuario(usuarioId);
    }
}
