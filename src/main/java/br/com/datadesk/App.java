
package br.com.datadesk;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
