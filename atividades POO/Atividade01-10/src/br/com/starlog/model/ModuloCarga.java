package br.com.starlog.model;

import br.com.starlog.exception.CapacidadeExcedidaException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.DoubleStream;

public class ModuloCarga {
    private final List<Carga> cargas = new ArrayList<>();
    private final String idModulo;
    private final int capacidadeMaxima;

    public ModuloCarga(String idModulo, int capacidadeMaxima) {
        boolean possuiId = idModulo != null && !idModulo.trim().isEmpty();
        if (!possuiId) {
            throw new IllegalArgumentException("ID obrigatório.");
        }
        if (capacidadeMaxima < 1) {
            throw new IllegalArgumentException("Capacidade deve ser positiva.");
        }
        this.idModulo = idModulo;
        this.capacidadeMaxima = capacidadeMaxima;
    }

    public String getIdModulo() { return idModulo; }
    public int getCapacidadeMaxima() { return capacidadeMaxima; }
    public List<Carga> getCargas() { return List.copyOf(cargas); }

    public void carregarCarga(Carga carga) throws CapacidadeExcedidaException {
        verificarEspaco();
        cargas.add(Objects.requireNonNull(carga, "Carga obrigatória."));
    }

    private void verificarEspaco() throws CapacidadeExcedidaException {
        if (cargas.size() < capacidadeMaxima) {
            return;
        }
        throw new CapacidadeExcedidaException("Capacidade maxima de " + capacidadeMaxima
                + " atingida no modulo " + idModulo);
    }

    private DoubleStream segurosDe(Predicate<Carga> criterio) {
        return cargas.stream().filter(criterio).mapToDouble(Carga::getValorSeguro);
    }

    public double calcularSeguroTotal() {
        return segurosDe(carga -> true).sum();
    }

    public long contarPorCategoria(String categoria) {
        Predicate<Carga> pertenceCategoria = carga -> carga.getCategoria().equals(categoria);
        return cargas.stream().filter(pertenceCategoria).count();
    }

    public double calcularSeguroPesadas(double pesoCorte) {
        return segurosDe(carga -> carga.getPesoKg() > pesoCorte).sum();
    }
}
