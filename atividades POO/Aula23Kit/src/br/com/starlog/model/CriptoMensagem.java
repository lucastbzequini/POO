package br.com.starlog.model;

import java.util.Locale;

public class CriptoMensagem {
    private static final String ALFABETO = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final String remetente;
    private final String mensagem;
    private final int chaveDeslocamento;

    public CriptoMensagem(String remetente, String mensagem, int chaveDeslocamento) {
        this.remetente = prepararTexto(remetente, "Remetente não pode ser nulo ou vazio.");
        this.mensagem = prepararTexto(mensagem, "Mensagem não pode ser nula ou vazia.");
        if (chaveDeslocamento <= 0 || chaveDeslocamento >= ALFABETO.length()) {
            throw new IllegalArgumentException("Chave deve estar entre 1 e 25.");
        }
        this.chaveDeslocamento = chaveDeslocamento;
    }

    private static String prepararTexto(String texto, String erro) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException(erro);
        }
        return texto.trim().toUpperCase(Locale.ROOT);
    }

    public String getRemetente() { return remetente; }
    public String getMensagem() { return mensagem; }
    public int getChaveDeslocamento() { return chaveDeslocamento; }

    public String cifrar() {
        String substituicoes = ALFABETO.substring(chaveDeslocamento)
                + ALFABETO.substring(0, chaveDeslocamento);
        char[] letras = mensagem.toCharArray();
        for (int indice = 0; indice < letras.length; indice++) {
            int posicao = ALFABETO.indexOf(letras[indice]);
            if (posicao != -1) {
                letras[indice] = substituicoes.charAt(posicao);
            }
        }
        return new String(letras);
    }

    public int calcularChecksum() {
        int soma = 0;
        for (int indice = 0; indice < mensagem.length(); indice++) {
            soma += mensagem.charAt(indice);
        }
        return soma;
    }
}
