package com.aracnidostore.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/** Prueba el flujo de estados permitido: PENDIENTE -> CONFIRMADO -> ENVIADO -> ENTREGADO. */
class EstadoPedidoTest {

    @Test
    void elFlujoNormalEsValido() {
        assertTrue(EstadoPedido.PENDIENTE.puedePasarA(EstadoPedido.CONFIRMADO));
        assertTrue(EstadoPedido.CONFIRMADO.puedePasarA(EstadoPedido.ENVIADO));
        assertTrue(EstadoPedido.ENVIADO.puedePasarA(EstadoPedido.ENTREGADO));
    }

    @Test
    void sePuedeCancelarSoloAntesDelEnvio() {
        assertTrue(EstadoPedido.PENDIENTE.puedePasarA(EstadoPedido.CANCELADO));
        assertTrue(EstadoPedido.CONFIRMADO.puedePasarA(EstadoPedido.CANCELADO));
        assertFalse(EstadoPedido.ENVIADO.puedePasarA(EstadoPedido.CANCELADO));
    }

    @Test
    void noSePuedeSaltarEstados() {
        assertFalse(EstadoPedido.PENDIENTE.puedePasarA(EstadoPedido.ENVIADO));
        assertFalse(EstadoPedido.PENDIENTE.puedePasarA(EstadoPedido.ENTREGADO));
        assertFalse(EstadoPedido.CONFIRMADO.puedePasarA(EstadoPedido.ENTREGADO));
    }

    @Test
    void entregadoYCanceladoSonFinales() {
        for (EstadoPedido nuevo : EstadoPedido.values()) {
            assertFalse(EstadoPedido.ENTREGADO.puedePasarA(nuevo));
            assertFalse(EstadoPedido.CANCELADO.puedePasarA(nuevo));
        }
    }
}
