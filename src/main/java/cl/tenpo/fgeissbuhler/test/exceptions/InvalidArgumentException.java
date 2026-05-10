package cl.tenpo.fgeissbuhler.test.exceptions;

/**
 * Para validación de parámetros de entrada en métodos
 */
public class InvalidArgumentException extends RuntimeException {
    
    public InvalidArgumentException(String message) {
        super(message);
    }
    
}
