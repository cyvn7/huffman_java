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

        drawCircle(gridPane, 0, 1, 1);

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

        if (poziom < 15 && x > 0) {
            i=i+1;
            System.out.println("x w kole: " + poziom);
            this.rysujLinie(grid, x, 0, poziom);
            this.rysujLinie(grid, x, 1, poziom);
        }

    }

    public void rysujLinie(GridPane grid, int x2, int strona, int poziom) {
        Line line = new Line();
        int znak = strona == 0 ? -1 : 1;
        line.setStartX(0);
        line.setStartY(0);
        line.setEndX(znak*((double) 250 / (poziom+1)));
        line.setEndY(50);

        Text number = new Text(Integer.toString(strona));
        number.setFont(Font.font("Arial", 20));
        number.setFill(Color.BLACK);

        grid.add(number, x2+znak, 1+poziom*2);
        GridPane.setHalignment(number, HPos.CENTER);
        GridPane.setMargin(number, new Insets(0, 0, 30, 0));

        System.out.println("x w linii: " + x2+strona);
        grid.add(line, x2+znak, 1+poziom*2);

//        if (strona == 1) {
//            rysujLinie(grid, x2 + 1, 0, poziom + 1);
//        } else if (strona == 0) {
//            rysujLinie(grid, x2 - 1, 1, poziom + 1);
//        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}