package com.aracnidostore.backend.dto;

import com.aracnidostore.backend.model.EstadoPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class PedidoResponseDTO {

    private Long id;
    private String usuario;
    private LocalDateTime fecha;
    private EstadoPedido estado;
    private List<LineaPedidoResponseDTO> lineas;
    private BigDecimal total;

    public PedidoResponseDTO() {
    }

    public PedidoResponseDTO(Long id, String usuario, LocalDateTime fecha, EstadoPedido estado,
                              List<LineaPedidoResponseDTO> lineas, BigDecimal total) {
        this.id = id;
        this.usuario = usuario;
        this.fecha = fecha;
        this.estado = estado;
        this.lineas = lineas;
        this.total = total;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public List<LineaPedidoResponseDTO> getLineas() {
        return lineas;
    }

    public void setLineas(List<LineaPedidoResponseDTO> lineas) {
        this.lineas = lineas;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
