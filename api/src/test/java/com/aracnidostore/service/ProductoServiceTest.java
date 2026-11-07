package com.aracnidostore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        Producto p = new Producto("Cartucho", "desc", 9500.0, 3, null, null);
        when(productoRepository.findByStockLessThanEqualOrderByStockAsc(5)).thenReturn(List.of(p));

        List<Producto> resultado = service.stockBajo(null);

        assertEquals(1, resultado.size());
        verify(productoRepository).findByStockLessThanEqualOrderByStockAsc(5);
    }

    @Test
    void stockBajoConLimiteUsaElLimiteIndicado() {
        when(productoRepository.findByStockLessThanEqualOrderByStockAsc(10)).thenReturn(List.of());

        service.stockBajo(10);

        verify(productoRepository).findByStockLessThanEqualOrderByStockAsc(10);
    }
}
