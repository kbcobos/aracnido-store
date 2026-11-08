package com.aracnidostore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.aracnidostore.dto.ItemPedidoRequest;
import com.aracnidostore.dto.PedidoRequest;
import com.aracnidostore.dto.PedidoResponse;
import com.aracnidostore.exception.StockInsuficienteException;
import com.aracnidostore.exception.TransicionEstadoInvalidaException;
import com.aracnidostore.exception.UsuarioNoEncontradoException;
import com.aracnidostore.model.EstadoPedido;
import com.aracnidostore.model.LineaPedido;
import com.aracnidostore.model.Pedido;
import com.aracnidostore.model.Producto;
import com.aracnidostore.model.Usuario;
import com.aracnidostore.repository.PedidoRepository;
import com.aracnidostore.repository.ProductoRepository;
import com.aracnidostore.repository.UsuarioRepository;

/**
 * Tests unitarios de la lógica de pedidos.
 */
@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private ProductoRepository productoRepository;

    private PedidoService service;
    private Usuario usuario;

    @BeforeEach
    void preparar() {
        service = new PedidoService(pedidoRepository, usuarioRepository, productoRepository, 5);
        usuario = new Usuario("Peter Parker", "peter@dailybugle.com");
        usuario.setId(1L);
    }

    private Producto producto(Long id, double precio, int stock) {
        Producto p = new Producto("Producto " + id, "desc", BigDecimal.valueOf(precio), stock, null, null);
        p.setId(id);
        return p;
    }

    @Test
    void crearPedidoDescuentaStockYQuedaPendiente() {
        Producto p = producto(1L, 45000.0, 10);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(p));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));

        PedidoResponse r = service.crear(new PedidoRequest(1L, List.of(new ItemPedidoRequest(1L, 3))));

        assertEquals(7, p.getStock());
        assertEquals(EstadoPedido.PENDIENTE, r.estado());
        assertEquals(0, new BigDecimal("135000").compareTo(r.total()));
        assertEquals(1, r.items().size());
    }

    @Test
    void siElMismoProductoSeRepiteSeSumanLasCantidades() {
        Producto p = producto(1L, 1000.0, 10);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(p));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));

        PedidoResponse r = service.crear(new PedidoRequest(1L,
                List.of(new ItemPedidoRequest(1L, 2), new ItemPedidoRequest(1L, 3))));

        assertEquals(5, p.getStock());
        assertEquals(1, r.items().size());
        assertEquals(5, r.items().get(0).cantidad());
    }

    @Test
    void sinStockSuficienteLanzaExcepcionYNoGuarda() {
        Producto p = producto(1L, 45000.0, 2);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(p));

        assertThrows(StockInsuficienteException.class,
                () -> service.crear(new PedidoRequest(1L, List.of(new ItemPedidoRequest(1L, 5)))));

        assertEquals(2, p.getStock());
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    @Test
    void usuarioInexistenteLanzaExcepcion() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UsuarioNoEncontradoException.class,
                () -> service.crear(new PedidoRequest(99L, List.of(new ItemPedidoRequest(1L, 1)))));
    }

    @Test
    void cancelarUnPedidoDevuelveElStock() {
        Producto p = producto(1L, 45000.0, 7);
        Pedido pedido = new Pedido(usuario);
        pedido.setId(1L);
        pedido.agregarLinea(new LineaPedido(p, 3));
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        PedidoResponse r = service.cambiarEstado(1L, EstadoPedido.CANCELADO);

        assertEquals(10, p.getStock());
        assertEquals(EstadoPedido.CANCELADO, r.estado());
    }

    @Test
    void unaTransicionInvalidaLanzaExcepcionYNoTocaElStock() {
        Producto p = producto(1L, 45000.0, 7);
        Pedido pedido = new Pedido(usuario);
        pedido.setId(1L);
        pedido.agregarLinea(new LineaPedido(p, 3));
        pedido.setEstado(EstadoPedido.ENTREGADO);
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        assertThrows(TransicionEstadoInvalidaException.class,
                () -> service.cambiarEstado(1L, EstadoPedido.CANCELADO));

        assertEquals(7, p.getStock());
        assertEquals(EstadoPedido.ENTREGADO, pedido.getEstado());
    }
}
