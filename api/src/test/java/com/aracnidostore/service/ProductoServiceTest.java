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

import com.aracnidostore.dto.ProductoRequest;
import com.aracnidostore.exception.CategoriaNoEncontradaException;
import com.aracnidostore.exception.ProductoDuplicadoException;
import com.aracnidostore.exception.ProductoNoEncontradoException;
import com.aracnidostore.model.Producto;
import com.aracnidostore.repository.CategoriaRepository;
import com.aracnidostore.repository.ProductoRepository;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;
    @Mock
    private CategoriaRepository categoriaRepository;

    private ProductoService service;

    @BeforeEach
    void preparar() {
        service = new ProductoService(productoRepository, categoriaRepository, 5);
    }

    @Test
    void obtenerUnProductoInexistenteLanzaExcepcion() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ProductoNoEncontradoException.class, () -> service.obtener(99L));
    }

    @Test
    void stockBajoSinLimiteUsaElMinimoConfigurado() {
        Producto p = new Producto("Cartucho", "desc", new BigDecimal("9500.00"), 3, null, null);
        when(productoRepository.findByStockLessThan(5)).thenReturn(List.of(p));

        List<Producto> resultado = service.stockBajo(null);

        assertEquals(1, resultado.size());
        verify(productoRepository).findByStockLessThan(5);
    }

    @Test
    void stockBajoConLimiteUsaElLimiteIndicado() {
        when(productoRepository.findByStockLessThan(10)).thenReturn(List.of());

        service.stockBajo(10);

        verify(productoRepository).findByStockLessThan(10);
    }

    @Test
    void crearConNombreRepetidoLanzaExcepcionYNoGuarda() {
        Producto existente = new Producto("Máscara", "desc", new BigDecimal("22000.00"), 20, null, null);
        existente.setId(3L);
        when(productoRepository.findByNombreIgnoreCase("máscara")).thenReturn(Optional.of(existente));

        ProductoRequest req = new ProductoRequest("máscara", "otra", new BigDecimal("100"), 1, null, 1L);

        assertThrows(ProductoDuplicadoException.class, () -> service.crear(req));
        verify(productoRepository, never()).save(any(Producto.class));
    }

    @Test
    void crearConCategoriaInexistenteLanzaExcepcion() {
        when(productoRepository.findByNombreIgnoreCase("Nuevo")).thenReturn(Optional.empty());
        when(categoriaRepository.findById(9L)).thenReturn(Optional.empty());

        ProductoRequest req = new ProductoRequest("Nuevo", "desc", new BigDecimal("100"), 1, null, 9L);

        assertThrows(CategoriaNoEncontradaException.class, () -> service.crear(req));
    }

    @Test
    void rangoDePreciosConMinimoMayorAlMaximoEsInvalido() {
        assertThrows(IllegalArgumentException.class,
                () -> service.buscarPorPrecio(new BigDecimal("20000"), new BigDecimal("5000")));
    }

    @Test
    void valorInventarioSinProductosEsCero() {
        when(productoRepository.calcularValorInventario()).thenReturn(null);

        assertEquals(BigDecimal.ZERO, service.calcularValorInventario());
    }

    @Test
    void productosDeUnaCategoriaInexistenteLanzanExcepcion() {
        when(categoriaRepository.existsById(99L)).thenReturn(false);

        assertThrows(CategoriaNoEncontradaException.class, () -> service.listarPorCategoriaId(99L));
    }
}
