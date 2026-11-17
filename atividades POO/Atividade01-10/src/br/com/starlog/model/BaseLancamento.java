package br.com.starlog.model;

import java.util.HashMap;
import java.util.Map;

public class BaseLancamento {
    private final Map<String, ModuloCarga> cadastro = new HashMap<>();

    public ModuloCarga buscarModulo(String idModulo) {
        return cadastro.get(idModulo);
    }

    public void cadastrarModulo(ModuloCarga modulo) {
        if (modulo == null) {
            throw new NullPointerException("Módulo obrigatório.");
        }
        String chave = modulo.getIdModulo();
        cadastro.put(chave, modulo);
    }
}
