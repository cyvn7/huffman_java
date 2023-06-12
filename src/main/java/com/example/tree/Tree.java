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

import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;

public class Tree extends Application {
    int i = 0;
    int pietra;
    int szerokosc;
    Dictionary<String, Character> kody;
    boolean[][] czyNarysowano;
    public Tree(Dictionary<String, Character> kody, int poziomy) {
        this.kody = kody;
        this.pietra = poziomy + 1;
        this.szerokosc = (int) Math.pow(2.0, Double.valueOf(pietra-2));  //pietra*2 - 1;
        System.out.println("szerokosc: " + szerokosc);
        czyNarysowano = new boolean[szerokosc][pietra];
        System.out.println(czyNarysowano.length);
    }


    @Override
    public void start(Stage primaryStage) {
        // Tworzenie kółek


        // Tworzenie siatki (GridPane)
        GridPane gridPane = new GridPane();

        drawCircle(gridPane, pietra, 0, 's');
//        for (Enumeration e = kody.keys(); e.hasMoreElements();) {
//            //rysujGalaz(gridPane, e.nextElement().toString(), kody.get(e.nextElement()));
//            System.out.println("==========================");
//        }
//        char[] kody2 = kody.keys().toString().toCharArray();
//        for (String kod : kody.ke) {
//            //rysujGalaz(gridPane, e.nextElement().toString(), kody.get(e.nextElement()));
//            System.out.println("==========================");
//        }
        System.out.println(kody.keys());
        // Tworzenie sceny i ustawianie siatki jako jej korzenia
        Scene scene = new Scene(gridPane);

        // Ustawianie tytułu i wyświetlanie sceny
        primaryStage.setTitle("Circle Line Example");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void rysujGalaz(GridPane gridPane, String s, Character c) {
        int x = szerokosc/2;
        int y = 0;
        int i = 0;
        for (char ch: s.toCharArray()) {
            int kierunek = ch == '0' ? -1 : 1;
            System.out.println("x: " + x + "+" + kierunek*(szerokosc/(y+1)) + " y: " + y);
            y = y + 1;
            x = x + kierunek*(szerokosc/(y+1));
            if(!czyNarysowano[x][y]) {
                char znak = i == s.length() - 1 ? c : ' ';
                drawCircle(gridPane, x, y, znak);
                czyNarysowano[x][y] = true;
            }
            i++;
        }
    }
    public void drawCircle(GridPane grid, int x, int y, char znak) {
        Circle circle = new Circle(20);
        circle.setStroke(Color.BLACK);
        circle.setFill(Color.TRANSPARENT);

        Text number = new Text(Character.toString(znak));
        number.setFont(Font.font("Arial", 20));
        number.setFill(Color.BLACK);

        grid.add(circle, x, y);
        grid.add(number, x, y);
        GridPane.setHalignment(number, HPos.CENTER);
        GridPane.setHalignment(circle, HPos.CENTER);

//        Line line = new Line();
//
//        if (poziom < 15 && x > 0) {
//            i=i+1;
//            System.out.println("x w kole: " + poziom);
//            this.rysujLinie(grid, x, 0, poziom);
//            this.rysujLinie(grid, x, 1, poziom);
//        }

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

