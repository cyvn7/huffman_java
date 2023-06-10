package com.example.tree;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Tree extends Application {
    int i = 0;

    @Override
    public void start(Stage primaryStage) {
        // Tworzenie kółek


        // Tworzenie siatki (GridPane)
        GridPane gridPane = new GridPane();

        drawCircle(gridPane, 1, 0, 1);
        drawCircle(gridPane, 0, 1, 2);
        drawCircle(gridPane, 1, 2, 3);

        // Tworzenie sceny i ustawianie siatki jako jej korzenia
        Scene scene = new Scene(gridPane);

        // Ustawianie tytułu i wyświetlanie sceny
        primaryStage.setTitle("Circle Line Example");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void drawCircle(GridPane grid, int poziom, int x, int razy) {
        Circle circle = new Circle(30);
        circle.setStroke(Color.BLACK);
        circle.setFill(Color.TRANSPARENT);

        Text number = new Text(Integer.toString(razy));
        number.setFont(Font.font("Arial", 20));
        number.setFill(Color.BLACK);

        grid.add(circle, x, poziom*2);
        grid.add(number, x, poziom*2);
        GridPane.setHalignment(number, HPos.CENTER);
        GridPane.setHalignment(circle, HPos.CENTER);

        Line line = new Line();

        if (this.i < 9) {
            i++;
            this.rysujLinie(grid, x, 0, 0, poziom);
            this.rysujLinie(grid, x, 0, 1, poziom);
        }

    }

    public void rysujLinie(GridPane grid, int x2, int y2, int strona, int poziom) {
        Line line = new Line();
        int znak = strona == 0 ? -1 : 1;
        line.setStartX(0);
        line.setStartY(0);
        line.setEndX(znak*(250 / (poziom+1)));
        line.setEndY(50);
        grid.add(line, x2+strona, 1+poziom*2);
       // GridPane.setMargin(line, new Insets(0, 0, 0, 125));

//        if (strona == 1) {
//            this.rysujKolo(g, x2 - 25, y2, this.i, poziom + 1);
//        } else if (strona == 0) {
//            this.rysujKolo(g, x2 - 25, y2, this.i, poziom + 1);
//        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
