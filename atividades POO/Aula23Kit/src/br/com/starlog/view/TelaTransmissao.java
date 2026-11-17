package br.com.starlog.view;

import br.com.starlog.controller.TransmissaoController;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.*;

public class TelaTransmissao extends JFrame {
    private final TransmissaoController controller;
    private final JTextField campoRemetente = new JTextField("ALPHA");
    private final JTextField campoMensagem = new JTextField("SEGREDO");
    private final JTextField campoChave = new JTextField("3");
    private final JTextArea saida = new JTextArea(12, 48);

    public TelaTransmissao(TransmissaoController controller) {
        super("StarLog - Transmissão cifrada");
        this.controller = controller;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        add(criarFormulario(), BorderLayout.NORTH);
        saida.setEditable(false);
        add(new JScrollPane(saida), BorderLayout.CENTER);
        add(criarBotao(), BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel criarFormulario() {
        JPanel formulario = new JPanel(new GridLayout(3, 2, 8, 8));
        formulario.setBorder(BorderFactory.createEmptyBorder(12, 12, 0, 12));
        incluirCampo(formulario, "Remetente:", campoRemetente);
        incluirCampo(formulario, "Mensagem:", campoMensagem);
        incluirCampo(formulario, "Chave (1 a 25):", campoChave);
        return formulario;
    }

    private void incluirCampo(JPanel painel, String rotulo, JTextField campo) {
        painel.add(new JLabel(rotulo));
        painel.add(campo);
    }

    private JButton criarBotao() {
        JButton botao = new JButton("Cifrar e transmitir");
        botao.addActionListener(evento -> enviarMensagem());
        return botao;
    }

    private void enviarMensagem() {
        try {
            String resultado = controller.processarTransmissao(campoRemetente.getText(),
                    campoMensagem.getText(), campoChave.getText());
            saida.append(resultado + System.lineSeparator());
        } catch (IllegalArgumentException erro) {
            JOptionPane.showMessageDialog(this, erro.getMessage(),
                    "Dados inválidos", JOptionPane.ERROR_MESSAGE);
        }
    }
}
