package com.example.tree;

import javafx.scene.control.Alert;

import java.io.*;
import java.util.Dictionary;
import java.util.Hashtable;

public class HuffmanDecoding {
    static Dictionary<String, Character> kody = new Hashtable<>();  // Słownik przechowujący kody dla poszczególnych znaków
    static int ileWezlow = 0, koniec = 1;  // Liczniki węzłów i końca kolejki
    static Wezel[] wszystkie = new Wezel[256];  // Tablica przechowująca wszystkie węzły

    static {
        for (int i = 0; i < 256; i++) {
            wszystkie[i] = new Wezel();  // Inicjalizacja tablicy węzłów
        }
    }

    static Wezel[] qqq = new Wezel[255];  // Kolejka priorytetowa węzłów
    static Wezel[] q = qqq;

    static int poziomy=0;

    public static void dekoduj(String nazwaPliku) {
        // Dekodowanie pliku
        // Budowanie ścieżki pliku wejściowego
        String propertyout = nazwaPliku;
        propertyout = propertyout.replace(".txt.huffman", "O.txt");
        try {
            FileInputStream plikin = new FileInputStream(nazwaPliku);
            BufferedInputStream pinh = new BufferedInputStream(plikin);
            BufferedWriter pout = new BufferedWriter(new FileWriter(propertyout));
            decode(pinh, pout);  // Wywołanie funkcji dekodującej
            System.out.println(kody);
            printTree();
            pout.close();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Zakończono dekompresję");
            alert.setHeaderText("Plik został pomyślnie zdekompresowany");
            alert.setContentText("Plik zapakowany został do "+propertyout+"");
            alert.showAndWait();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static Wezel nowyWezel(int czesto, char c, Wezel a, Wezel b) {
        // Tworzenie nowego węzła drzewa
        Wezel n = wszystkie[ileWezlow++];
        if (czesto != 0) {
            n.c = c;
            n.czesto = czesto;
        } else {
            n.left = a;
            n.right = b;
            n.czesto = a.czesto + b.czesto;
        }
        return n;
    }

    static void dodajK(Wezel n) {
        int j, i = koniec++;
        while ((j = i / 2) != 0) {
            if (q[j].czesto <= n.czesto) {
                break;
            }
            q[i] = q[j];
            i = j;
        }
        q[i] = n;
    }

    static Wezel usunK() {
        int i, l;
        Wezel n = q[i = 1];
        if (koniec < 2) {
            return null;
        }
        koniec--;
        while ((l = i * 2) < koniec) {
            if (l + 1 < koniec && q[l + 1].czesto < q[l].czesto) {
                l++;
            }
            q[i] = q[l];
            i = l;
        }
        q[i] = q[koniec];
        return n;
    }

    public static String byteBits(byte c) {
        // Konwersja bajtu na ciąg bitów
        String slowo = "";
        for (int i = 7; i >= 0; i--) {
            int bit = (c >>> i) & 1;
            slowo += Integer.toString(bit);
        }
        return slowo;
    }

    public static Dictionary<String, Character> dajKody(){
        return(kody);
    }

    public static int ilePoziomow(){
        return poziomy;
    }

    static void znajdzKody(Wezel node, String code) {
        // Rekurencyjne znajdowanie kodów dla węzłów drzewa
        if(code.length()>poziomy){
            poziomy=code.length();
        }
        if (node.c != 0) {
            kody.put(code, node.c);  // Dodanie kodu do słownika
            return;
        }
        znajdzKody(node.left, code + "1");
        znajdzKody(node.right, code + "0");
    }

    public static boolean isByteNewLine(byte b) {
        // Sprawdzenie, czy bajt reprezentuje znak nowej linii
        return (b == '\n');
    }

    static int[] znajdzCzestotliwosci(BufferedInputStream inputStream) throws IOException {
        // Znajdowanie częstotliwości wystąpień znaków w pliku
        int[] frequencies = new int[128];
        StringBuilder lineBuilder = new StringBuilder();
        int c;
        while ((c = inputStream.read()) != '\n') {
            lineBuilder.append((char) c);
        }
        frequencies['\n'] = Integer.parseInt(lineBuilder.toString());
        lineBuilder.setLength(0);
        while ((c = inputStream.read()) != -1) {
            if ((char) c == '\n') {
                String linia = lineBuilder.toString();
                if (linia.equals("123456789")) {
                    break;
                }
                int ile = Integer.parseInt(linia.substring(1));
                frequencies[linia.charAt(0)] = ile;
                lineBuilder.setLength(0);
            } else {
                lineBuilder.append((char) c);
            }
        }
        return frequencies;
    }

    static void decode(BufferedInputStream pinh, BufferedWriter pout) throws IOException {
        // Dekodowanie danych
        int i;
        int lim;
        int[] czesto = znajdzCzestotliwosci(pinh);  // Znajdowanie częstotliwości
        for (i = 0; i < 128; i++) {
            if (czesto[i] != 0) {
                dodajK(nowyWezel(czesto[i], (char) i, null, null));  // Dodawanie węzłów do kolejki priorytetowej
            }
        }
        while (koniec > 2) {
            dodajK(nowyWezel(0, (char) 0, usunK(), usunK()));  // Tworzenie drzewa
        }
        Wezel n = q[1];
        znajdzKody(n, "");  // Znajdowanie kodów dla węzłów drzewa
        byte c;
        String slowo = "";
        c = (byte) pinh.read();
        while (!isByteNewLine(c)) {
            slowo += Integer.toString((int) c - 48);
            c = (byte) pinh.read();
        }
        lim = Integer.parseInt(slowo);
        String bity = "";
        while (pinh.available() > 0) {
            c = (byte) pinh.read();
            bity += byteBits(c);  // Konwersja danych na ciąg bitów
        }
        bity = bity.substring(0, lim);
        String kod = "";
        char[] tabelaBitow = bity.toCharArray();
        for (char znak : tabelaBitow) {
            kod += znak;
            if ((((Hashtable) kody)).containsKey(kod)) {
                pout.write(kody.get(kod));  // Zapisywanie zdekodowanego znaku
                kod = "";
            }
        }
        if (q[1].czesto != n.czesto) {
            System.out.println("Błąd na wejściu!");
        }
    }

    public static void printTree() {
        Wezel root = HuffmanDecoding.q[1];
        printTreeNode(root, 0);
    }

    private static void printTreeNode(Wezel node, int level) {
        if (node == null) {
            return;
        }

        for (int i = 0; i < level; i++) {
            System.out.print("  ");
        }

        if (node.c != 0) {
            System.out.println("[" + node.c + " - " + node.czesto + " + level " + level + "]");
        } else {
            System.out.println("Węzeł");
        }

        printTreeNode(node.left, level + 1);
        printTreeNode(node.right, level + 1);
    }

}
