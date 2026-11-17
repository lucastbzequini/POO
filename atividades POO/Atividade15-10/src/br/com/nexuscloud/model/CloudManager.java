package br.com.nexuscloud.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class CloudManager {
    private final Map<Cliente, Set<Servidor>> servidoresPorCliente = new HashMap<>();

    public void registrarCliente(Cliente cliente) {
        Cliente titular = Objects.requireNonNull(cliente);
        servidoresPorCliente.computeIfAbsent(titular, ignorado -> new HashSet<>());
    }

    public void provisionarServidor(Cliente cliente, Servidor servidor) {
        registrarCliente(cliente);
        Set<Servidor> servidores = servidoresPorCliente.get(cliente);
        servidores.add(Objects.requireNonNull(servidor));
    }

    public double calcularFaturaAtiva(Cliente cliente) {
        Set<Servidor> servidores = servidoresPorCliente.get(cliente);
        if (servidores == null) {
            return 0.0;
        }
        return servidores.stream()
                .filter(servidor -> servidor.isStatusLigado())
                .mapToDouble(servidor -> servidor.getPrecoPorHora()).sum();
    }
}
