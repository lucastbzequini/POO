package br.com.starlog.model;

import java.util.Locale;

public class Carga {
    private static final double TAXA_SEGURO = 0.02;
    private static final double ADICIONAL_RISCO = 150.0;
    private final String codigoRastreio;
    private final String categoria;
    private final double peso;
    private final double valorDeclarado;

    public Carga(String codigoRastreio, String categoria, double peso, double valorDeclarado) {
        this.codigoRastreio = normalizar(codigoRastreio,
                "Código de rastreio não pode ser nulo ou vazio.");
        this.categoria = normalizar(categoria, "Categoria não pode ser nula ou vazia.");
        this.peso = positivo(peso, "Peso deve ser estritamente positivo (maior que zero).");
        this.valorDeclarado = positivo(valorDeclarado, "Valor declarado deve ser maior que zero.");
    }

    private static String normalizar(String texto, String mensagemErro) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException(mensagemErro);
        }
        return texto.trim().toUpperCase(Locale.ROOT);
    }

    private static double positivo(double valor, String mensagemErro) {
        if (Double.isFinite(valor) && valor > 0) {
            return valor;
        }
        throw new IllegalArgumentException(mensagemErro);
    }

    public String getCodigoRastreio() { return codigoRastreio; }
    public String getCategoria() { return categoria; }
    public double getPeso() { return peso; }
    public double getValorDeclarado() { return valorDeclarado; }

    private boolean exigeAdicional() {
        return "PERIGOSA".equals(categoria) || peso > 100;
    }

    public double calcularSeguro() {
        double parcelaBasica = valorDeclarado * TAXA_SEGURO;
        return exigeAdicional() ? parcelaBasica + ADICIONAL_RISCO : parcelaBasica;
    }

    @Override
    public String toString() {
        String formato = "[%s] Categoria: %s | Peso: %.2f kg | Valor: R$ %.2f | Seguro: R$ %.2f";
        return String.format(formato, codigoRastreio, categoria, peso,
                valorDeclarado, calcularSeguro());
    }
}
