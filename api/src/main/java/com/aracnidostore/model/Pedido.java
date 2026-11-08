package com.aracnidostore.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Pedido de un usuario: fecha, estado y una lista de líneas (cada una
 * con su producto y cantidad).
 */
@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private LocalDateTime fecha;

    // STRING: en la base se guarda el nombre ("PENDIENTE"), no el número
    // de posición del enum. Así, si algún día se reordenan o agregan
    // estados, los pedidos ya guardados no cambian de significado.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoPedido estado;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LineaPedido> lineas = new ArrayList<>();

    public Pedido() {
    }

    // Un pedido nuevo siempre arranca en PENDIENTE, con la fecha de ahora
    public Pedido(Usuario usuario) {
        this.usuario = usuario;
        this.fecha = LocalDateTime.now();
        this.estado = EstadoPedido.PENDIENTE;
    }

    /**
     * Se agrega una línea manteniendo la relación en los dos sentidos: la
     * lista de este pedido Y el campo "pedido" de la línea. Si solo se
     * hiciera lineas.add(), la línea no sabría a qué pedido pertenece y
     * Hibernate no podría guardar la clave foránea.
     */
    public void agregarLinea(LineaPedido linea) {
        lineas.add(linea);
        linea.setPedido(this);
    }

    /**
     * Suma el subtotal de cada línea: el costo total del pedido.
     */
    public BigDecimal calcularTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (LineaPedido linea : lineas) {
            total = total.add(linea.getSubtotal());
        }
        return total;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
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

    public List<LineaPedido> getLineas() {
        return lineas;
    }

    public void setLineas(List<LineaPedido> lineas) {
        this.lineas = lineas;
    }
}
