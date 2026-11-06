package com.aracnidostore.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aracnidostore.dto.PedidoRequest;
import com.aracnidostore.dto.PedidoResponse;
import com.aracnidostore.exception.PedidoNoEncontradoException;
import com.aracnidostore.exception.ProductoNoEncontradoException;
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
 * Lógica de pedidos. Devuelve PedidoResponse (DTO) y no la entidad: el
 * armado del DTO ocurre dentro de la transacción, cuando las relaciones
 * lazy (usuario, productos) todavía se pueden leer.
 */
@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;

    public PedidoService(PedidoRepository pedidoRepository,
                         UsuarioRepository usuarioRepository,
                         ProductoRepository productoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
    }

    /**
     * Crea un pedido PENDIENTE y descuenta el stock.
     * Es todo-o-nada: si un producto no existe o no alcanza el stock, se
     * lanza una excepción y @Transactional deshace todo, incluidos los
     * descuentos de stock ya hechos a los productos anteriores.
     */
    @Transactional
    public PedidoResponse crear(PedidoRequest req) {
        Usuario usuario = usuarioRepository.findById(req.usuarioId())
                .orElseThrow(() -> new UsuarioNoEncontradoException(req.usuarioId()));

        // Si el mismo producto viene repetido, se suman las cantidades.
        Map<Long, Integer> cantidades = new LinkedHashMap<>();
        req.itemsPedido().forEach(i -> cantidades.merge(i.productoId(), i.cantidad(), Integer::sum));

        Pedido pedido = new Pedido(usuario);
        for (Map.Entry<Long, Integer> e : cantidades.entrySet()) {
            Producto producto = productoRepository.findById(e.getKey())
                    .orElseThrow(() -> new ProductoNoEncontradoException(e.getKey()));
            int cantidad = e.getValue();
            if (producto.getStock() < cantidad) {
                throw new StockInsuficienteException(producto.getNombre(), producto.getStock(), cantidad);
            }
            producto.setStock(producto.getStock() - cantidad);
            pedido.agregarLinea(new LineaPedido(producto, cantidad));
        }
        return PedidoResponse.from(pedidoRepository.save(pedido));
    }

    @Transactional(readOnly = true)
    public List<PedidoResponse> listar() {
        return pedidoRepository.findAllByOrderByFechaDesc().stream()
                .map(PedidoResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public PedidoResponse obtener(Long id) {
        return PedidoResponse.from(buscar(id));
    }

    @Transactional(readOnly = true)
    public List<PedidoResponse> listarPorUsuario(Long usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new UsuarioNoEncontradoException(usuarioId);
        }
        return pedidoRepository.findByUsuarioIdOrderByFechaDesc(usuarioId).stream()
                .map(PedidoResponse::from).toList();
    }

    /** Cambia el estado respetando el flujo. Al cancelar, devuelve el stock. */
    @Transactional
    public PedidoResponse cambiarEstado(Long id, EstadoPedido nuevo) {
        Pedido pedido = buscar(id);
        if (!pedido.getEstado().puedePasarA(nuevo)) {
            throw new TransicionEstadoInvalidaException(pedido.getEstado(), nuevo);
        }
        if (nuevo == EstadoPedido.CANCELADO) {
            for (LineaPedido l : pedido.getLineas()) {
                Producto p = l.getProducto();
                p.setStock(p.getStock() + l.getCantidad());
            }
        }
        pedido.setEstado(nuevo);
        return PedidoResponse.from(pedido);
    }

    private Pedido buscar(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNoEncontradoException(id));
    }
}
