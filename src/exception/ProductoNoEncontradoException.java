package exception;

/**
 * Se lanza cuando se busca un producto por su ID y no existe en el catálogo.
 */
public class ProductoNoEncontradoException extends RuntimeException {

    public ProductoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
