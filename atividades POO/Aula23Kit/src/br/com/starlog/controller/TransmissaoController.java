package br.com.starlog.controller;

import br.com.starlog.model.CriptoMensagem;
import java.util.ArrayList;
import java.util.List;

public class TransmissaoController {
    private final List<String> historico = new ArrayList<>();

    public String processarTransmissao(String remetenteRaw, String msgRaw, String chaveStr) {
        int deslocamento = interpretarChave(chaveStr);
        CriptoMensagem mensagem = new CriptoMensagem(remetenteRaw, msgRaw, deslocamento);
        String transmissao = montarPacote(mensagem);
        historico.add(transmissao);
        return transmissao;
    }

    private int interpretarChave(String entrada) {
        String numero = entrada == null ? "" : entrada.trim();
        try {
            return Integer.parseInt(numero);
        } catch (NumberFormatException causa) {
            throw new IllegalArgumentException("Chave deve ser um número inteiro válido.", causa);
        }
    }

    private String montarPacote(CriptoMensagem mensagem) {
        return String.format("[%s] #%d -> %s", mensagem.getRemetente(),
                mensagem.calcularChecksum(), mensagem.cifrar());
    }

    public List<String> getHistorico() {
        return List.copyOf(historico);
    }

    public int getTotalTransmissoes() {
        return historico.size();
    }
}
