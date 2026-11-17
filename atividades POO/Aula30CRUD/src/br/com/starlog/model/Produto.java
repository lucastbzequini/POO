package br.com.starlog.model;

public class Produto {
    private Long id;
    private final String nome;
    private double preco;
    private int estoque;

    public Produto(String nome, double preco, int estoque) {
        this(null, nome, preco, estoque);
    }

    public Produto(Long id, String nome, double preco, int estoque) {
        this.nome = conferirNome(nome);
        this.id = id;
        setPreco(preco);
        setEstoque(estoque);
    }

    private static String conferirNome(String nome) {
        if (nome != null && nome.trim().length() >= 3) {
            return nome.trim();
        }
        throw new IllegalArgumentException("Nome deve ter pelo menos 3 caracteres.");
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public double getPreco() { return preco; }
    public int getEstoque() { return estoque; }
    public void setId(Long id) { this.id = id; }

    public void setPreco(double preco) {
        boolean permitido = Double.isFinite(preco) && preco > 0;
        if (!permitido) {
            throw new IllegalArgumentException("Preço deve ser maior que zero.");
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
        String colunas = "%-4d | %-25s | R$ %8.2f | %d un";
        return String.format(colunas, id, nome, preco, estoque);
    }
}
