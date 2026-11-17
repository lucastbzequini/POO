package br.com.starlog.controller;

import br.com.starlog.model.Carga;
import br.com.starlog.view.TelaCadastroCarga;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.List;

public class CargaController {
    private final List<Carga> cargasRegistradas = new ArrayList<>();

    public Carga cadastrarCarga(String codigo, String categoria, String pesoRaw, String valorRaw) {
        conferirPreenchimento(pesoRaw, valorRaw);
        double peso = converterDecimal(pesoRaw);
        double valor = converterDecimal(valorRaw);
        Carga novaCarga = new Carga(codigo, categoria, peso, valor);
        cargasRegistradas.add(novaCarga);
        return novaCarga;
    }

    private void conferirPreenchimento(String peso, String valor) {
        for (String campo : new String[]{peso, valor}) {
            if (campo == null || campo.trim().isEmpty()) {
                throw new IllegalArgumentException("Preencha o peso e o valor declarado.");
            }
        }
    }

    private double converterDecimal(String texto) {
        return Double.parseDouble(texto.trim().replace(',', '.'));
    }

    public void cadastrarCarga(String codigo, String categoria, String peso, String valor,
            TelaCadastroCarga tela) {
        try {
            Carga carga = cadastrarCarga(codigo, categoria, peso, valor);
            tela.exibirMensagemSucesso("Carga cadastrada com sucesso!\n" + carga);
            tela.atualizarRelatorio(gerarTextoRelatorio());
        } catch (NumberFormatException erro) {
            tela.exibirMensagemErro(
                    "Erro de formato numérico: Digite apenas números válidos para Peso e Valor!");
        } catch (IllegalArgumentException erro) {
            tela.exibirMensagemErro("Regra de Negócio Violada: " + erro.getMessage());
        }
    }

    public String gerarTextoRelatorio() {
        if (cargasRegistradas.isEmpty()) {
            return "Nenhuma carga registrada na esteira.";
        }
        StringBuilder relatorio = new StringBuilder("--- RELATÓRIO OFICIAL DE DESPACHO ---\n");
        try (Formatter formato = new Formatter(relatorio)) {
            int numero = 1;
            for (Carga carga : cargasRegistradas) {
                formato.format("%02d. %s%n", numero++, carga);
            }
            double pesoTotal = cargasRegistradas.stream().mapToDouble(Carga::getPeso).sum();
            double seguroTotal = cargasRegistradas.stream().mapToDouble(Carga::calcularSeguro).sum();
            formato.format("Total de Cargas: %d | Peso Total: %.2f kg | Seguro Acumulado: R$ %.2f%n",
                    cargasRegistradas.size(), pesoTotal, seguroTotal);
        }
        return relatorio.toString();
    }

    public List<Carga> getRepositorio() {
        // A lista acompanha novos cadastros, mas não permite alterações externas.
        return Collections.unmodifiableList(cargasRegistradas);
    }
}
