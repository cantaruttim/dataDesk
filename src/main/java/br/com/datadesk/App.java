
package br.com.datadesk;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
                        
                        /*
                        .readAllLines -> lê o conteúdo do arquivo e devolve uma lista de string
                        Cada elemento representa uma linha do arquivo. 
                        */ 
                        List<String> linhas = Files.readAllLines(
                            arquivo.toPath() // converte o objeto File
                        );

                        if (linhas.isEmpty()) {
                            arquivoSelecionado.setText(
                                "O arquivo está vazio."
                            );
                        } else {

                            /* 
                            String cabecalho = linhas.get(0); // primeira linha como cabecalho

                            int quantidadeRegistros = linhas.size() - 1;

                            // implementando a TableView
                            String primeiraLinhaDeDados = linhas.size() > 1
                                ? linhas.get(1)
                                : "";

                            String[] campos = primeiraLinhaDeDados.split(";", -1);

                            // lendo cada valor como um texto unico -> array de string
                            String[] colunas = cabecalho.split(";", -1);
                            int quantidadeColunas = colunas.length;

                            arquivoSelecionado.setText(
                                "Arquivo: " + arquivo.getName() 
                                + "\nColunas: " + quantidadeColunas
                                + "\nCabeçalho: " + cabecalho
                                + "\nRegistros: " + quantidadeRegistros
                                + "\nPrimeiro registro: " + primeiraLinhaDeDados
                                + "\nCampos encontrados: " + campos.length
                            );
                            */

                            String cabecalho = linhas.get(0);
                            String[] colunas = cabecalho.split(";", -1);
                            int quantidadeRegistros = linhas.size() - 1;
                            int quantidadeColunas = colunas.length;

                            // Criando a TableView
                            TableView<String[]> tabela = new TableView<>();

                            //Criando as colunas dinamicamente
                            for (int i = 0; i < colunas.length; i++) {
                                final int indice = i;

                                TableColumn<String[], String> coluna = 
                                    new TableColumn<>(colunas[i]);

                                coluna.setCellValueFactory(celula ->
                                    new ReadOnlyStringWrapper(
                                        celula.getValue()[indice]
                                    )
                                );

                                tabela.getColumns().add(coluna);
                            }

                            // Preparando os registros
                            ObservableList<String[]> dadosTabela = 
                                FXCollections.observableArrayList();

                            for (int i = 1; i < linhas.size(); i++) {
                                String[] campos = linhas.get(i).split(";", -1);

                                dadosTabela.add(campos);
                            }

                            // Conectando os dados com a tabela
                            tabela.setItems(dadosTabela);

                            // Exibindo o resumo do arquivo
                            arquivoSelecionado.setText(
                                "Arquivo: " + arquivo.getName()
                                + " | Colunas: " + quantidadeColunas
                                + " | Registros: " + quantidadeRegistros
                            );

                            // atualizar a tela de arquivos
                            telaArquivos.getChildren().setAll(
                                tituloArquivos,
                                botaoSelecionar,
                                arquivoSelecionado,
                                tabela  
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
