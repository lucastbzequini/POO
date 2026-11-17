package br.com.starlog.model;

public class Carga {
    private final String codigoRastreio;
    private String categoria;
    private double pesoKg;
    private double valorSeguro;

    public Carga(String codigoRastreio, String categoria, double pesoKg, double valorSeguro) {
        this.codigoRastreio = exigirTexto(codigoRastreio,
                "Codigo de rastreio da carga nao pode ser nulo ou vazio.");
        this.pesoKg = exigirPeso(pesoKg);
        this.categoria = exigirTexto(categoria, "Categoria obrigatória.");
        this.valorSeguro = exigirSeguro(valorSeguro);
    }

    private static String exigirTexto(String texto, String erro) {
        if (texto != null && !texto.trim().isEmpty()) {
            return texto;
        }
        throw new IllegalArgumentException(erro);
    }

    private static double exigirPeso(double valor) {
        if (Double.isFinite(valor) && valor > 0) {
            return valor;
        }
        throw new IllegalArgumentException("Peso deve ser positivo.");
    }

    private static double exigirSeguro(double valor) {
        if (Double.isFinite(valor) && valor >= 0) {
            return valor;
        }
        throw new IllegalArgumentException("Seguro não pode ser negativo.");
    }

    public String getCodigoRastreio() { return codigoRastreio; }
    public String getCategoria() { return categoria; }
    public double getPesoKg() { return pesoKg; }
    public double getValorSeguro() { return valorSeguro; }

    public void setCategoria(String categoria) {
        this.categoria = exigirTexto(categoria, "Categoria obrigatória.");
    }

    public void setPesoKg(double pesoKg) {
        this.pesoKg = exigirPeso(pesoKg);
    }

    public void setValorSeguro(double valorSeguro) {
        this.valorSeguro = exigirSeguro(valorSeguro);
    }

    @Override
    public boolean equals(Object outro) {
        return outro instanceof Carga carga && codigoRastreio.equals(carga.codigoRastreio);
    }

    @Override
    public int hashCode() {
        return 31 + codigoRastreio.hashCode();
    }

    @Override
    public String toString() {
        return new StringBuilder("Carga [rastreio=")
                .append(codigoRastreio).append(", categoria=").append(categoria)
                .append(", peso=").append(pesoKg).append("kg, seguro=R$ ")
                .append(valorSeguro).append("]").toString();
    }
}
