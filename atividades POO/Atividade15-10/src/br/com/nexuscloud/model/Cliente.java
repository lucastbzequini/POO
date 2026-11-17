package br.com.nexuscloud.model;

public class Cliente {
    private final String cpf;
    private String nome;

    public Cliente(String cpf, String nome) {
        this.cpf = conferirTexto(cpf, "CPF obrigatório.");
        setNome(nome);
    }

    private static String conferirTexto(String valor, String mensagem) {
        if (valor != null && !valor.trim().isEmpty()) {
            return valor;
        }
        throw new IllegalArgumentException(mensagem);
    }

    public String getCpf() { return cpf; }
    public String getNome() { return nome; }

    public void setNome(String nome) {
        this.nome = conferirTexto(nome, "Nome obrigatório.").trim();
    }

    @Override
    public int hashCode() {
        return 31 + cpf.hashCode();
    }

    @Override
    public boolean equals(Object outro) {
        return outro instanceof Cliente cliente && cpf.equals(cliente.cpf);
    }
}
