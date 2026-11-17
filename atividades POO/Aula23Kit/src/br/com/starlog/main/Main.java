package br.com.starlog.main;

import br.com.starlog.controller.TransmissaoController;
import br.com.starlog.view.TelaTransmissao;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        if (args.length != 0 && args[0].equals("--console")) {
            demonstrarTransmissoes();
        } else {
            SwingUtilities.invokeLater(Main::abrirJanela);
        }
    }

    private static void demonstrarTransmissoes() {
        TransmissaoController transmissoes = new TransmissaoController();
        String[][] exemplos = {{"BASE", "OLA", "2"}, {"FROTA", "XYZ", "1"}};
        for (String[] exemplo : exemplos) {
            System.out.println(transmissoes.processarTransmissao(
                    exemplo[0], exemplo[1], exemplo[2]));
        }
        System.out.println("Transmissões: " + transmissoes.getTotalTransmissoes());
    }

    private static void abrirJanela() {
        TelaTransmissao janela = new TelaTransmissao(new TransmissaoController());
        janela.setVisible(true);
    }
}
