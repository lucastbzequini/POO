package br.com.autotech.model;

import java.util.Locale;

public class Peca {
    private Long id;
    private String codigo;
    private String nome;
    private double preco;
    private int estoque;

    public Peca(String codigo, String nome, double preco, int estoque) {
        this(null, codigo, nome, preco, estoque);
    }

    public Peca(Long id, String codigo, String nome, double preco, int estoque) {
        this.id = id;
        setCodigo(codigo);
        setNome(nome);
        setPreco(preco);
        setEstoque(estoque);
    }

    private static String textoObrigatorio(String texto, String erro) {
        if (texto != null && !texto.trim().isEmpty()) {
            return texto.trim();
        }
        throw new IllegalArgumentException(erro);
    }

    public Long getId() { return id; }
    public String getCodigo() { return codigo; }
    public String getNome() { return nome; }
    public double getPreco() { return preco; }
    public int getEstoque() { return estoque; }
    public void setId(Long id) { this.id = id; }

    public void setCodigo(String codigo) {
        this.codigo = textoObrigatorio(codigo, "Código obrigatório.").toUpperCase(Locale.ROOT);
    }

    public void setNome(String nome) {
        this.nome = textoObrigatorio(nome, "Nome obrigatório.");
    }

    public void setPreco(double preco) {
        boolean valorAceito = Double.isFinite(preco) && preco > 0;
        if (!valorAceito) {
            throw new IllegalArgumentException("Preço deve ser positivo.");
        }
        this.preco = preco;
    }

    public void setEstoque(int estoque) {
        if (estoque >= 0) {
            this.estoque = estoque;
            return;
        }
        throw new IllegalArgumentException("Estoque não pode ser negativo.");
    }

    @Override
    public String toString() {
        String formato = "ID: %d | %s | %-35s | R$ %.2f | Estoque: %d";
        return String.format(formato, id, codigo, nome, preco, estoque);
    }
}
