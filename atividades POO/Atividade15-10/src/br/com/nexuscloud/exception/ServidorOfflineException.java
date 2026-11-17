package br.com.nexuscloud.exception;

/** Indica uma operação que exige um servidor ligado. */
public class ServidorOfflineException extends RuntimeException {
    public ServidorOfflineException(String mensagem) {
        super(mensagem);
    }
}
