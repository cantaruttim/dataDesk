package br.com.datadesk;

public class RegistroCSV {
    private final String[] campos;
    private final boolean valido;
    
    public RegistroCSV(
        String[] campos, 
        int qtdColunasEsperadas
    ) {
        this.campos = campos;

        boolean qtdCorreta = campos.length == qtdColunasEsperadas;
        boolean camposPreenchidos = true;

        for (String campo : campos) {
            if (campo == null || campo.isBlank()) {
                camposPreenchidos = false;
                break;
            }
        }

        this.valido = qtdCorreta && camposPreenchidos;

    }

    public String[] getCampos() {
        return campos;
    }
    
    public boolean isValido() {
        return valido;
    }

}
