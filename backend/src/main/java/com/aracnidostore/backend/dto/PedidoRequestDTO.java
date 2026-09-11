package com.aracnidostore.backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class PedidoRequestDTO {

    @NotBlank(message = "Debe indicar el usuario")
    private String usuario;

    @NotEmpty(message = "El pedido debe tener al menos una línea")
    @Valid
    private List<LineaPedidoRequestDTO> lineas;

    public PedidoRequestDTO() {
    }

    public PedidoRequestDTO(String usuario, List<LineaPedidoRequestDTO> lineas) {
        this.usuario = usuario;
        this.lineas = lineas;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public List<LineaPedidoRequestDTO> getLineas() {
        return lineas;
    }

    public void setLineas(List<LineaPedidoRequestDTO> lineas) {
        this.lineas = lineas;
    }
}
