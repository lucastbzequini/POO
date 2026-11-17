package br.com.nexuscloud.model;

public class Container {
    private final String id;
    private final String nomeApp;
    private final double ramNecessaria;

    public Container(String id, String nomeApp, double ramNecessaria) {
        this.id = exigirIdentificacao(id, "ID obrigatório.");
        this.nomeApp = exigirIdentificacao(nomeApp, "Aplicação obrigatória.");
        if (Double.isFinite(ramNecessaria) && ramNecessaria > 0) {
            this.ramNecessaria = ramNecessaria;
        } else {
            throw new IllegalArgumentException("RAM necessária deve ser positiva.");
        }
    }

    private static String exigirIdentificacao(String valor, String mensagem) {
        if (valor == null || valor.trim().length() == 0) {
            throw new IllegalArgumentException(mensagem);
        }
        return valor;
    }

    public String getId() { return id; }
    public String getNomeApp() { return nomeApp; }
    public double getRamNecessaria() { return ramNecessaria; }

    @Override
    public int hashCode() {
        return 31 + id.hashCode();
    }

    @Override
    public boolean equals(Object outro) {
        return outro instanceof Container container && id.equals(container.id);
    }
}
