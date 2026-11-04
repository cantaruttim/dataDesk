
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
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.css.PseudoClass;
import javafx.geometry.Pos;

public class App extends Application {

    @Override
    public void start(Stage stage) {

        // 1. Layout principal
        BorderPane layoutPrincipal = new BorderPane();

        // 2. Tela Inicial
        Label tituloInicial = new Label("Visão Geral");
        tituloInicial.getStyleClass().add("page-title");

        Label descricaoInicio = new Label(
            "Acompanhe seus arquivos e processos de dados"
        );
        descricaoInicio.getStyleClass().add("page-description");

        VBox telaInicio = new VBox(
            10,
            tituloInicial,
            descricaoInicio
        );

        telaInicio.getStyleClass().add("page-content"); 


        // 3. Menu lateral
        Button botaoInicio = new Button("Início");
        Button botaoArquivos = new Button("Arquivos");
        Button botaoProcessamento = new Button("Processamento");

        VBox menuLateral = new VBox(12);

        menuLateral.getStyleClass().add("sidebar");

        // Identidade visual
        Label nomeAplicacao = new Label("DataDesk");
        nomeAplicacao.getStyleClass().add("sidebar-title");

        Label descricao = new Label("DATA ENGINEERING");
        descricao.getStyleClass().add("sidebar-subtitle");

        // Estilização dos botões
        Button[] botoesMenu = {
            botaoInicio, botaoArquivos, botaoProcessamento
        };
        
        for (Button botao: botoesMenu) {
            botao.getStyleClass().add("sidebar-button");
            botao.setMaxWidth(Double.MAX_VALUE);
            botao.setAlignment(Pos.CENTER_LEFT);
        }

        // Conteúdo do Menu
        menuLateral.getChildren().addAll(
            nomeAplicacao,
            descricao,
            botaoInicio,
            botaoArquivos,
            botaoProcessamento
        );

        layoutPrincipal.setCenter(telaInicio);
        atualizaMenuAtivo(
            botaoInicio, botaoInicio, botaoArquivos, botaoProcessamento
        );

        layoutPrincipal.setLeft(menuLateral);

        botaoInicio.setOnAction(event -> {
            layoutPrincipal.setCenter(
                new Label("Bem-vindo à tela Inicial!")
            );

            atualizaMenuAtivo(botaoInicio, botaoInicio, botaoArquivos, botaoProcessamento);
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
                            TableView<RegistroCSV> tabela = new TableView<>();

                            //Criando as colunas dinamicamente
                            for (int i = 0; i < colunas.length; i++) {
                                final int indice = i;

                                TableColumn<RegistroCSV, String> coluna = 
                                    new TableColumn<>(colunas[i]);

                                coluna.setCellValueFactory(celula ->
                                    new ReadOnlyStringWrapper(
                                        // acesso ao array por meio do getCampos
                                        celula.getValue().getCampos()[indice]
                                    )
                                );

                                tabela.getColumns().add(coluna);
                            }

                            // Coluna de Status
                            TableColumn<RegistroCSV, String> colunaStatus = 
                                new TableColumn<>("Status");
                            
                            colunaStatus.setCellValueFactory(celula -> {
                                RegistroCSV registro = celula.getValue();

                                String status = registro.isValido()
                                    ? "Válido"
                                    : "Inválido";
                                    
                                return new ReadOnlyStringWrapper(status);
                            });    

                            tabela.getColumns().add(colunaStatus);

                            // Criando a coluna de Erros
                            TableColumn<RegistroCSV, String> colunaErros = new TableColumn<>("Erros");
                            colunaErros.setCellValueFactory(celula -> {
                                RegistroCSV registro = celula.getValue();

                                String erros = registro.getErros().isEmpty()
                                    ? "-"
                                                            // método getErros retorna uma List<String> que posteriormente é transformada em uma única String
                                    : String.join(" | ", registro.getErros());

                                return new ReadOnlyStringWrapper(erros);
                            });

                            tabela.getColumns().add(colunaErros);

                            // Preparando os registros
                            ObservableList<RegistroCSV> dadosTabela = 
                                FXCollections.observableArrayList();

                            for (int i = 1; i < linhas.size(); i++) {
                                String[] campos = linhas.get(i).split(";", -1);

                                RegistroCSV registros = new RegistroCSV(
                                    campos, quantidadeColunas
                                );

                                System.out.println(
                                    "Registro: " + linhas.get(i)
                                    + " | Válido: " + registros.isValido()
                                );

                                dadosTabela.add(registros);
                            }

                            // Conectando os dados com a tabela
                            tabela.setItems(dadosTabela);

                            PseudoClass registroInvalido = PseudoClass.getPseudoClass("registro-invalido");

                            tabela.setRowFactory(tv -> new TableRow<RegistroCSV>() {

                                @Override 
                                protected void updateItem(RegistroCSV registro, boolean vazio) {
                                    super.updateItem(registro, vazio);
                                    
                                    boolean linhaInvalida = !vazio && registro != null && !registro.isValido();

                                    pseudoClassStateChanged(
                                        registroInvalido, 
                                        linhaInvalida
                                    );
                                }
                            });

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


            atualizaMenuAtivo(botaoArquivos, botaoInicio, botaoArquivos, botaoProcessamento);

        });

        botaoProcessamento.setOnAction(event -> {
            layoutPrincipal.setCenter(
                new Label("Tela de Processamento - em desenvolvimento")
            );

            atualizaMenuAtivo(botaoProcessamento, botaoInicio, botaoArquivos, botaoProcessamento);

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

        // add estilo ao sistema
        var caminhoCss = getClass().getResource("/br/com/datadesk/datadesk.css");

        if (caminhoCss == null) {
            throw new IllegalStateException(
                "Arquivo css não encontrado."
            );
        }

        cena.getStylesheets().add(
            caminhoCss.toExternalForm()
        );

        stage.setTitle("DataDesk - Data Engineering Tool");
        stage.setScene(cena);
        stage.show();

    }

    private void atualizaMenuAtivo(Button botaoAtivo, Button... botoes) {
        for (Button botao: botoes) {
            botao.getStyleClass().remove("menu-button-active");
        }

        botaoAtivo.getStyleClass().add("menu-button-active");
    }

}
