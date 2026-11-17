package br.com.starlog.main;

import br.com.starlog.controller.CargaController;
import br.com.starlog.view.TelaCadastroCarga;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        if (args.length != 0 && args[0].equals("--console")) {
            mostrarExemplo();
        } else {
            SwingUtilities.invokeLater(Main::abrirCadastro);
        }
    }

    private static void mostrarExemplo() {
        CargaController cadastro = new CargaController();
        String[][] cargas = {
            {"CARG-01", "GERAL", "50", "1000"},
            {"CARG-02", "PERIGOSA", "20", "1000"},
            {"CARG-03", "ALIMENTAR", "120", "1000"}
        };
        for (String[] dados : cargas) {
            cadastro.cadastrarCarga(dados[0], dados[1], dados[2], dados[3]);
        }
        System.out.println(cadastro.gerarTextoRelatorio());
    }

    private static void abrirCadastro() {
        TelaCadastroCarga tela = new TelaCadastroCarga(new CargaController());
        tela.setVisible(true);
    }
}
