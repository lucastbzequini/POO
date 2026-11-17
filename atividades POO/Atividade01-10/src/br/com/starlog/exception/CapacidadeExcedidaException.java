package br.com.starlog.exception;

/** Informa que todas as posições do módulo já estão ocupadas. */
public class CapacidadeExcedidaException extends Exception {
    public CapacidadeExcedidaException(String mensagem) {
        super(mensagem);
    }
}
