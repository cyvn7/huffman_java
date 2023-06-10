package com.example.tree;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.File;
import java.util.Optional;

public class GUI extends Application {
    private TextField pathBox;
    private Button pathBtn;
    private Button dekompresujBtn;
    private Button kompresujBtn;
    private CheckBox szyfrBox;
    private String path;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Dekompresor");

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        pathBox = new TextField();
        pathBox.setPrefWidth(300);

        pathBtn = new Button("Wybierz plik");
        pathBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Wybierz plik");
                fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
                File selectedFile = fileChooser.showOpenDialog(primaryStage);
                if (selectedFile != null) {
                    path = selectedFile.getAbsolutePath();
                    pathBox.setText(path);
                }
            }
        });

        dekompresujBtn = new Button("Dekompresuj");
        kompresujBtn = new Button("Kompresuj");

        dekompresujBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                path = pathBox.getText();
                if (path != null && !path.equals("") && path.endsWith(".huffman")) {
                    if (szyfrBox.isSelected()) {
                        TextInputDialog dialog = new TextInputDialog("haslo");
                        dialog.setTitle("Hasło");
                        dialog.setHeaderText("Wymagane jest podanie hasła do deszyfrowania");
                        dialog.setContentText("Podaj hasło:");

                        Optional<String> result = dialog.showAndWait();

                        HuffmanEncryptor.dekoduj(path, result.get());
                    }
                    else
                        HuffmanDecoding.dekoduj(path);
                        //System.out.println(HuffmanDecoding.ilePoziomow()); SPRAWDZENIE POZIOMOW DRZEWKA

                } else if (path == null || path.equals("")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Błąd");
                    alert.setHeaderText("Nie wybrano pliku!");
                    alert.setContentText("Wybierz plik do dekompresji.");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Błąd");
                    alert.setHeaderText("Zły format pliku!");
                    alert.setContentText("Plik do dekompresji musi być w formacie .huffman");
                    alert.showAndWait();
                }
            }
        });

        kompresujBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                path = pathBox.getText();
                if (path != null && !path.equals("")) {
                    if (szyfrBox.isSelected()) {
                        TextInputDialog dialog = new TextInputDialog("haslo");
                        dialog.setTitle("Hasło");
                        dialog.setHeaderText("Wymagane jest podanie hasła do szyfrowania");
                        dialog.setContentText("Podaj hasło:");

                        Optional<String> result = dialog.showAndWait();

                        HuffmanEncryptor.koduj(path, result.get());
                    }
                    else
                        HuffmanCoding.koduj(path);

                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Błąd");
                    alert.setHeaderText("Nie wybrano pliku!");
                    alert.setContentText("Wybierz plik do kompresji.");
                    alert.showAndWait();
                }
            }
        });

        szyfrBox = new CheckBox("Szyfrowanie");

        gridPane.add(pathBox, 0, 0, 3, 1);
        gridPane.add(pathBtn, 3, 0);
        gridPane.add(dekompresujBtn, 1, 1);
        gridPane.add(kompresujBtn, 0, 1);
        gridPane.add(szyfrBox, 2, 1);

        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().add(gridPane);

        Scene scene = new Scene(vbox, 440, 95);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
