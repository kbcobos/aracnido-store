package com.aracnidostore.backend.service;

import com.aracnidostore.backend.dto.LineaPedidoRequestDTO;
import com.aracnidostore.backend.dto.LineaPedidoResponseDTO;
import com.aracnidostore.backend.dto.PedidoRequestDTO;
import com.aracnidostore.backend.dto.PedidoResponseDTO;
import com.aracnidostore.backend.exception.EstadoInvalidoException;
import com.aracnidostore.backend.exception.ResourceNotFoundException;
import com.aracnidostore.backend.exception.StockInsuficienteException;
import com.aracnidostore.backend.model.EstadoPedido;
import com.aracnidostore.backend.model.LineaPedido;
import com.aracnidostore.backend.model.Pedido;
import com.aracnidostore.backend.model.Producto;
import com.aracnidostore.backend.repository.PedidoRepository;
import com.aracnidostore.backend.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;
    private final ProductoService productoService;

    public PedidoService(PedidoRepository pedidoRepository,
                          ProductoRepository productoRepository,
                          ProductoService productoService) {
        this.pedidoRepository = pedidoRepository;
        this.productoRepository = productoRepository;
        this.productoService = productoService;
    }

    @Transactional
    public PedidoResponseDTO crear(PedidoRequestDTO dto) {
        Pedido pedido = new Pedido(dto.getUsuario());

        for (LineaPedidoRequestDTO lineaDto : dto.getLineas()) {
            Producto producto = productoRepository.findById(lineaDto.getProductoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto", lineaDto.getProductoId()));

            if (producto.getStock() < lineaDto.getCantidad()) {
                throw new StockInsuficienteException(
                        producto.getNombre(), producto.getStock(), lineaDto.getCantidad());
            }

            pedido.agregarLinea(new LineaPedido(producto, lineaDto.getCantidad()));
        }

        Pedido guardado = pedidoRepository.save(pedido);
        return toResponseDTO(guardado);
    }

    @Transactional(readOnly = true)
    public PedidoResponseDTO obtenerPorId(Long id) {
        return toResponseDTO(buscarEntidadPorId(id));
    }

    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> listarPorUsuario(String usuario) {
        return pedidoRepository.findByUsuarioOrderByFechaDesc(usuario)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> listarTodos() {
        return pedidoRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional
    public PedidoResponseDTO confirmar(Long id) {
        Pedido pedido = buscarEntidadPorId(id);

        if (pedido.getEstado() != EstadoPedido.PENDIENTE) {
            throw new EstadoInvalidoException(
                    "Solo se puede confirmar un pedido en estado PENDIENTE (actual: " + pedido.getEstado() + ")");
        }

        for (LineaPedido linea : pedido.getLineas()) {
            productoService.descontarStock(linea.getProducto().getId(), linea.getCantidad());
        }

        pedido.setEstado(EstadoPedido.CONFIRMADO);
        return toResponseDTO(pedidoRepository.save(pedido));
    }

    @Transactional
    public PedidoResponseDTO cancelar(Long id) {
        Pedido pedido = buscarEntidadPorId(id);
        EstadoPedido estadoActual = pedido.getEstado();

        if (estadoActual == EstadoPedido.ENTREGADO || estadoActual == EstadoPedido.CANCELADO) {
            throw new EstadoInvalidoException(
                    "No se puede cancelar un pedido en estado " + estadoActual);
        }

        if (estadoActual == EstadoPedido.CONFIRMADO || estadoActual == EstadoPedido.ENVIADO) {
            for (LineaPedido linea : pedido.getLineas()) {
                productoService.reponerStock(linea.getProducto().getId(), linea.getCantidad());
            }
        }

        pedido.setEstado(EstadoPedido.CANCELADO);
        return toResponseDTO(pedidoRepository.save(pedido));
    }

    @Transactional
    public PedidoResponseDTO enviar(Long id) {
        Pedido pedido = buscarEntidadPorId(id);

        if (pedido.getEstado() != EstadoPedido.CONFIRMADO) {
            throw new EstadoInvalidoException(
                    "Solo se puede enviar un pedido en estado CONFIRMADO (actual: " + pedido.getEstado() + ")");
        }

        pedido.setEstado(EstadoPedido.ENVIADO);
        return toResponseDTO(pedidoRepository.save(pedido));
    }

    @Transactional
    public PedidoResponseDTO entregar(Long id) {
        Pedido pedido = buscarEntidadPorId(id);

        if (pedido.getEstado() != EstadoPedido.ENVIADO) {
            throw new EstadoInvalidoException(
                    "Solo se puede entregar un pedido en estado ENVIADO (actual: " + pedido.getEstado() + ")");
        }

        pedido.setEstado(EstadoPedido.ENTREGADO);
        return toResponseDTO(pedidoRepository.save(pedido));
    }

    private Pedido buscarEntidadPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", id));
    }

    private PedidoResponseDTO toResponseDTO(Pedido pedido) {
        List<LineaPedidoResponseDTO> lineasDto = pedido.getLineas().stream()
                .map(linea -> new LineaPedidoResponseDTO(
                        linea.getProducto().getId(),
                        linea.getProducto().getNombre(),
                        linea.getCantidad(),
                        linea.getPrecioUnitario(),
                        linea.getSubtotal()))
                .toList();

        return new PedidoResponseDTO(
                pedido.getId(),
                pedido.getUsuario(),
                pedido.getFecha(),
                pedido.getEstado(),
                lineasDto,
                pedido.getTotal());
    }
}
