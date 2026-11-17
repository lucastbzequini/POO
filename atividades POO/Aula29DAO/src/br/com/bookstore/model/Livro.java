package br.com.bookstore.model;

public class Livro {
    private Long id;
    private final String titulo;
    private final String autor;
    private final double preco;

    public Livro(String titulo, String autor, double preco) {
        this(null, titulo, autor, preco);
    }

    public Livro(Long id, String titulo, String autor, double preco) {
        this.titulo = validarTexto(titulo, 2, "Título deve ter pelo menos 2 caracteres.");
        this.autor = validarTexto(autor, 1, "Autor não pode ser vazio.");
        this.preco = validarPreco(preco);
        this.id = id;
    }

    private static String validarTexto(String texto, int minimo, String mensagem) {
        if (texto != null && texto.trim().length() >= minimo) {
            return texto.trim();
        }
        throw new IllegalArgumentException(mensagem);
    }

    private static double validarPreco(double preco) {
        if (preco > 0 && Double.isFinite(preco)) {
            return preco;
        }
        throw new IllegalArgumentException("O preço de venda deve ser estritamente positivo.");
    }

    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public double getPreco() { return preco; }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    @Override
    public String toString() {
        String colunas = "%-4d | %-20s | %-22s | R$ %.2f";
        return String.format(colunas, id, titulo, autor, preco);
    }
}
