package com.aracnidostore.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class LineaPedidoRequestDTO {

    @NotNull(message = "Debe indicar el id del producto")
    private Long productoId;

    @NotNull(message = "Debe indicar la cantidad")
    @Positive(message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;

    public LineaPedidoRequestDTO() {
    }

    public LineaPedidoRequestDTO(Long productoId, Integer cantidad) {
        this.productoId = productoId;
        this.cantidad = cantidad;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}
