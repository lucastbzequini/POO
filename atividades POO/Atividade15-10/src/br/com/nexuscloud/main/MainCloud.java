package br.com.nexuscloud.main;

import br.com.nexuscloud.exception.CapacidadeExcedidaException;
import br.com.nexuscloud.model.Cliente;
import br.com.nexuscloud.model.CloudManager;
import br.com.nexuscloud.model.Container;
import br.com.nexuscloud.model.Servidor;
import java.util.Locale;

public class MainCloud {
    public static void main(String[] args) throws CapacidadeExcedidaException {
        Cliente titular = new Cliente("111.222.333-44", "Wayne Enterprises");
        Servidor maquina = new Servidor("192.168.0.1", 16.0, 5.50);
        CloudManager nuvem = new CloudManager();
        nuvem.registrarCliente(titular);
        nuvem.provisionarServidor(titular, maquina);
        Container aplicacao = new Container("C-01", "Aplicação Web", 10.0);
        Container banco = new Container("C-02", "Banco de Dados", 8.0);
        boolean testarDesligado = args.length != 0 && args[0].equals("--teste-offline");
        if (testarDesligado) {
            maquina.alocarContainer(aplicacao);
        } else {
            executarAlocacoes(maquina, aplicacao, banco);
            System.out.printf(Locale.US, "Fatura ativa: R$ %.2f%n",
                    nuvem.calcularFaturaAtiva(titular));
        }
    }

    private static void executarAlocacoes(Servidor maquina, Container aplicacao, Container banco)
            throws CapacidadeExcedidaException {
        maquina.ligar();
        maquina.alocarContainer(aplicacao);
        System.out.println("C-01 alocado. RAM em uso: " + maquina.calcularRamEmUso() + " GB");
        try {
            maquina.alocarContainer(banco);
        } catch (CapacidadeExcedidaException erro) {
            System.out.println("Alocação recusada: " + erro.getMessage());
        }
    }
}
