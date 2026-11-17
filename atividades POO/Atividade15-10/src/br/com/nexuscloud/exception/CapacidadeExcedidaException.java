package br.com.nexuscloud.exception;

/** Indica uma alocação que ultrapassa a RAM disponível. */
public class CapacidadeExcedidaException extends Exception {
    public CapacidadeExcedidaException(String mensagem) {
        super(mensagem);
    }
}
