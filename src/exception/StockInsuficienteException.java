package exception;

/**
 * Se lanza cuando se intenta crear un pedido con una cantidad mayor al
 * stock disponible de un producto, o cuando se intenta cargar un stock
 * negativo.
 */
public class StockInsuficienteException extends RuntimeException {

    public StockInsuficienteException(String mensaje) {
        super(mensaje);
    }

    // Constructor de conveniencia: arma un mensaje detallado y consistente
    public StockInsuficienteException(String nombreProducto, int stockDisponible, int cantidadSolicitada) {
        super("Stock insuficiente para \"" + nombreProducto + "\": " +
                "disponible " + stockDisponible + ", solicitado " + cantidadSolicitada);
    }
}
