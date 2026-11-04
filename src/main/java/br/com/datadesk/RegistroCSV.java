package br.com.datadesk;

import java.util.ArrayList;
import java.util.List;

public class RegistroCSV {
    private final String[] campos;
    private final List<String> erros;
    
    public RegistroCSV(
        String[] campos, 
        int qtdColunasEsperadas
    ) {
        this.campos = campos;

        List<String> errosEncontrados = new ArrayList<>();
        
        // Valida a quantidade de colunas
        if (campos.length != qtdColunasEsperadas) {
            errosEncontrados.add(
                "Quantidade de colunas incorreta: esperado "
                + qtdColunasEsperadas
                + ", encontrado "
                + campos.length
            );
        }

        // Validação dos campos vazios
        for (int i = 0; i < campos.length; i++) {

            if (campos[i] == null || campos[i].isBlank()) {
                errosEncontrados.add(
                    "Campo " + (i + 1) + " está vazio."
                );
            }
        }

        this.erros = List.copyOf(errosEncontrados); // lista imutável com os erros encontrados

    }

    public String[] getCampos() {
        return campos;
    }
    
    public boolean isValido() {
        return erros.isEmpty();
    }

    public List<String> getErros() {
        return erros;
    }

}
