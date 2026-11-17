package br.com.starlog.main;

import br.com.starlog.exception.CapacidadeExcedidaException;
import br.com.starlog.model.BaseLancamento;
import br.com.starlog.model.Carga;
import br.com.starlog.model.ModuloCarga;
import java.util.HashSet;
import java.util.Set;

public class MainResgate {
    public static void main(String[] args) {
        verificarCodigoObrigatorio();
        verificarRastreiosRepetidos();
        ModuloCarga modulo = demonstrarLimite();
        apresentarConsultas(modulo);
    }

    private static void verificarCodigoObrigatorio() {
        try {
            new Carga("    ", "CRIOGENICA", 10.0, 500.0);
        } catch (IllegalArgumentException erro) {
            System.out.println("SUCESSO ATAQUE 1 (Fail-Fast ativado): " + erro.getMessage());
            return;
        }
        throw new AssertionError("Aceitou código vazio.");
    }

    private static void verificarRastreiosRepetidos() {
        Set<Carga> triagem = new HashSet<>();
        Carga[] recebidas = {
            new Carga("ORB-999", "ALIMENTOS", 10.0, 100.0),
            new Carga("ORB-999", "DADOS_ALTERADOS", 999.0, 8000.0)
        };
        for (Carga carga : recebidas) {
            triagem.add(carga);
        }
        if (triagem.size() != 1) {
            throw new AssertionError("O clone entrou no Set.");
        }
        System.out.println("SUCESSO ATAQUE 2 (Hash imune a clones): Tamanho do Set = 1");
    }

    private static ModuloCarga demonstrarLimite() {
        ModuloCarga modulo = new ModuloCarga("MOD-TESTE", 2);
        try {
            for (String codigo : new String[]{"C-01", "C-02", "C-03"}) {
                modulo.carregarCarga(new Carga(codigo, "GERAL", 5.0, 50.0));
            }
        } catch (CapacidadeExcedidaException erro) {
            System.out.println("SUCESSO ATAQUE 3 (Trava de Limite operante): " + erro.getMessage());
            return modulo;
        }
        throw new AssertionError("Ultrapassou a capacidade.");
    }

    private static void apresentarConsultas(ModuloCarga modulo) {
        double total = modulo.calcularSeguroTotal();
        if (Double.compare(total, 100.0) != 0) {
            throw new AssertionError("Seguro incorreto.");
        }
        System.out.println("SUCESSO ATAQUE 4 (Streams Calculado): R$ " + total);
        BaseLancamento base = new BaseLancamento();
        base.cadastrarModulo(modulo);
        System.out.println("Busca pelo Map: " + base.buscarModulo("MOD-TESTE").getIdModulo());
        System.out.println("Cargas GERAL: " + modulo.contarPorCategoria("GERAL"));
        System.out.println("Seguro com peso > 4 kg: " + modulo.calcularSeguroPesadas(4));
    }
}
