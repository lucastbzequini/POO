package br.com.starlog.view;

import br.com.starlog.controller.CargaController;
import java.awt.*;
import javax.swing.*;

public class TelaCadastroCarga extends JFrame {
    private final CargaController controller;
    private final JTextField campoCodigo = new JTextField();
    private final JTextField campoPeso = new JTextField();
    private final JTextField campoValor = new JTextField();
    private final JComboBox<String> categorias =
            new JComboBox<>(new String[]{"GERAL", "PERIGOSA", "ALIMENTAR", "TECNOLOGICA"});
    private final JTextArea relatorio = new JTextArea();

    public TelaCadastroCarga(CargaController controller) {
        super("StarLog Logistics - Sistema de Despacho Operacional");
        this.controller = controller;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(650, 480);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        add(criarCabecalho(), BorderLayout.NORTH);
        add(criarConteudo(), BorderLayout.CENTER);
        add(criarAcoes(), BorderLayout.SOUTH);
        atualizarRelatorio(controller.gerarTextoRelatorio());
    }

    private JPanel criarCabecalho() {
        JLabel titulo = new JLabel("PAINEL DE CONTROLE DE CARGAS • STARLOG-ZERO");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 15));
        JPanel painel = new JPanel();
        painel.setBackground(new Color(24, 43, 73));
        painel.add(titulo);
        return painel;
    }

    private JPanel criarFormulario() {
        JPanel formulario = new JPanel(new GridLayout(4, 2, 8, 8));
        formulario.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
        adicionarCampo(formulario, "Código de Rastreio (ex: CARG-01):", campoCodigo);
        adicionarCampo(formulario, "Categoria da Carga:", categorias);
        adicionarCampo(formulario, "Peso da Carga (kg):", campoPeso);
        adicionarCampo(formulario, "Valor Declarado (R$):", campoValor);
        return formulario;
    }

    private void adicionarCampo(JPanel painel, String titulo, JComponent campo) {
        painel.add(new JLabel(titulo));
        painel.add(campo);
    }

    private JPanel criarConteudo() {
        relatorio.setEditable(false);
        relatorio.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane rolagem = new JScrollPane(relatorio);
        rolagem.setBorder(BorderFactory.createTitledBorder(
                "Cargas Registradas na Memória da Estação:"));
        JPanel conteudo = new JPanel(new BorderLayout(5, 5));
        conteudo.add(criarFormulario(), BorderLayout.NORTH);
        conteudo.add(rolagem, BorderLayout.CENTER);
        return conteudo;
    }

    private JPanel criarAcoes() {
        JButton limpar = new JButton("Limpar Campos");
        JButton cadastrar = new JButton("Cadastrar no Sistema");
        cadastrar.setBackground(new Color(40, 167, 69));
        cadastrar.setForeground(Color.WHITE);
        cadastrar.setFont(new Font("SansSerif", Font.BOLD, 12));
        limpar.addActionListener(evento -> limparCampos());
        cadastrar.addActionListener(evento -> enviarCadastro());
        JPanel acoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        acoes.add(limpar);
        acoes.add(cadastrar);
        return acoes;
    }

    private void enviarCadastro() {
        controller.cadastrarCarga(campoCodigo.getText(), (String) categorias.getSelectedItem(),
                campoPeso.getText(), campoValor.getText(), this);
    }

    public void exibirMensagemSucesso(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        limparCampos();
    }

    public void exibirMensagemErro(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erro de Validação", JOptionPane.ERROR_MESSAGE);
    }

    public void atualizarRelatorio(String textoCompleto) {
        relatorio.setText(textoCompleto);
    }

    private void limparCampos() {
        for (JTextField campo : new JTextField[]{campoCodigo, campoPeso, campoValor}) {
            campo.setText("");
        }
        categorias.setSelectedIndex(0);
        campoCodigo.requestFocus();
    }
}
