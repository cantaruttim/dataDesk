
package br.com.datadesk;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {

        // 1. Layout principal
        BorderPane layoutPrincipal = new BorderPane();

        // 2. Cabeçalho
        Label titulo = new Label("DataDesk");
        layoutPrincipal.setTop(titulo);

        // 3. Menu lateral
        Button botaoInicio = new Button("Início");
        Button botaoArquivos = new Button("Arquivos");
        Button botaoProcessamento = new Button("Processamento");

        VBox menuLateral = new VBox(
            10,
            botaoInicio,
            botaoArquivos,
            botaoProcessamento
        );
        layoutPrincipal.setLeft(menuLateral);

        botaoInicio.setOnAction(event -> {
            layoutPrincipal.setCenter(
                new Label("Bem-vindo à tela Inicial!")
            );
        });

        botaoArquivos.setOnAction(event -> {
            
            Label tituloArquivos = new Label("Importação de Arquivos CSV");

            Button botaoSelecionar = new Button("Selecionar CSV");

            Label arquivoSelecionado = new Label("Nenhum arquivo selecionado");

            VBox telaArquivos = new VBox(
                15,
                tituloArquivos,
                botaoSelecionar,
                arquivoSelecionado
            );
            
            layoutPrincipal.setCenter(telaArquivos);

            botaoSelecionar.setOnAction(selecaoEvent -> {
                
                FileChooser seletor = new FileChooser();

                seletor.setTitle("Selecionar arquivo CSV");

                FileChooser.ExtensionFilter filtroCSV = 
                    new FileChooser.ExtensionFilter(
                        "Arquivo CSV (*.csv)",
                        "*.csv"
                    );

                seletor.getExtensionFilters().add(filtroCSV);

                File arquivo = seletor.showOpenDialog(stage);

                // if (arquivos != null) {
                //     arquivoSelecionado.setText(
                //         "Arquivo selecionado: " + 
                //         arquivos.getName()
                //     );
                // }

                if (arquivo != null) {
                    try {

                        List<String> linhas = Files.readAllLines(
                            arquivo.toPath()
                        );

                        if (linhas.isEmpty()) {
                            arquivoSelecionado.setText(
                                "O arquivo está vazio."
                            );
                        } else {
                            String cabecalho = linhas.get(0);

                            int quantidadeRegistros = linhas.size() - 1;

                            arquivoSelecionado.setText(
                                "Arquivo: " + arquivo.getName() 
                                + "\nColunas: " + cabecalho
                                + "\nRegistros: " + quantidadeRegistros
                            );
                        }
                        
                    } catch (IOException erro) {
                        arquivoSelecionado.setText(
                            "Erro ao ler o arquivo: " + erro.getMessage()
                        );
                    }
                }

            });



        });

        botaoProcessamento.setOnAction(event -> {
            layoutPrincipal.setCenter(
                new Label("Tela de Processamento - em desenvolvimento")
            );
        });


        // 4. Área central
        Label mensagemInicial = new Label(
            "Bem-vindo ao DataDesk! Selecione uma opção no menu."
        );

        layoutPrincipal.setCenter(mensagemInicial);

        // 5. Barra de status
        Label status = new Label("Status: aplicação pronta");
        layoutPrincipal.setBottom(status);

        // 6. Cena e janela
        Scene cena = new Scene(layoutPrincipal, 900, 600);

        stage.setTitle("DataDesk - Data Engineering Tool");
        stage.setScene(cena);
        stage.show();

    }
}
