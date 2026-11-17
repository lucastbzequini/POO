package br.com.nexuscloud.model;

import br.com.nexuscloud.exception.CapacidadeExcedidaException;
import br.com.nexuscloud.exception.ServidorOfflineException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Servidor {
    private final String ip;
    private final double ramTotal;
    private final double precoPorHora;
    private final List<Container> containers = new ArrayList<>();
    private boolean statusLigado;

    public Servidor(String ip, double ramTotal, double precoPorHora) {
        if (ip == null || ip.trim().isEmpty()) {
            throw new IllegalArgumentException("IP obrigatório.");
        }
        this.ip = ip;
        this.ramTotal = conferirRecurso(ramTotal, false, "RAM total deve ser positiva.");
        this.precoPorHora = conferirRecurso(precoPorHora, true, "Preço inválido.");
    }

    private static double conferirRecurso(double valor, boolean aceitaZero, String mensagem) {
        boolean faixaCorreta = aceitaZero ? valor >= 0 : valor > 0;
        if (Double.isFinite(valor) && faixaCorreta) {
            return valor;
        }
        throw new IllegalArgumentException(mensagem);
    }

    public String getIp() { return ip; }
    public double getRamTotal() { return ramTotal; }
    public double getPrecoPorHora() { return precoPorHora; }
    public boolean isStatusLigado() { return statusLigado; }
    public List<Container> getContainers() { return List.copyOf(containers); }
    public void ligar() { statusLigado = true; }
    public void desligar() { statusLigado = false; }

    public void alocarContainer(Container container) throws CapacidadeExcedidaException {
        verificarDisponibilidade();
        Objects.requireNonNull(container, "Container obrigatório.");
        double consumoAposAlocacao = calcularRamEmUso() + container.getRamNecessaria();
        if (consumoAposAlocacao > ramTotal) {
            throw new CapacidadeExcedidaException(
                    "Falha Crítica: Memória RAM insuficiente no servidor [" + ip + "].");
        }
        containers.add(container);
    }

    private void verificarDisponibilidade() {
        if (!statusLigado) {
            throw new ServidorOfflineException(
                    "Erro: Não é possível alocar containers em um servidor desligado.");
        }
    }

    public double calcularRamEmUso() {
        return containers.stream().mapToDouble(item -> item.getRamNecessaria()).sum();
    }

    @Override
    public boolean equals(Object outro) {
        return outro instanceof Servidor servidor && ip.equals(servidor.ip);
    }

    @Override
    public int hashCode() {
        return 31 + ip.hashCode();
    }
}
