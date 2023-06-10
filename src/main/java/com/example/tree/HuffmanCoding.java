package com.example.tree;

import javafx.scene.control.Alert;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Dictionary;
import java.util.Hashtable;

class Wezel {
    Wezel left, right;
    int czesto;
    char c;
}

public class HuffmanCoding {
    static int ileWezlow = 0, koniec = 1;
    static Wezel[] wszystkie = new Wezel[256];

    // Tworzenie tablicy wszystkie o rozmiarze 256, która przechowuje obiekty Wezel
    static {
        for (int i = 0; i < 256; i++) {
            wszystkie[i] = new Wezel();
        }
    }

    // Tworzenie tablicy qqq o rozmiarze 255, która przechowuje obiekty Wezel
    static Wezel[] qqq = new Wezel[255];
    static Wezel[] q = qqq;
    static String[] kod = new String[128];
    static char[] bufor = new char[1024];

    // Tworzenie nowego obiektu Wezel i zwracanie go
    static Wezel nowyWezel(int czesto, char c, Wezel a, Wezel b) {
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

    // Dodawanie obiektu Wezel do tablicy q
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

    // Usuwanie i zwracanie obiektu Wezel z tablicy q
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

    // Tworzenie kodów dla znaków na podstawie drzewa Huffmana
    static void znajdzKody(Wezel n, char[] sl, int dl) {
        char[] out = bufor;
        if (n.c != 0) {
            sl[dl] = '\0';
            String s = new String(sl);
            kod[n.c] = s;
            out = new char[out.length + dl + 1];
            System.arraycopy(bufor, 0, out, 0, bufor.length);
            bufor = out;
            return;
        }

        sl[dl] = '1';
        znajdzKody(n.left, sl, dl + 1);

        sl[dl] = '0';
        znajdzKody(n.right, sl, dl + 1);
    }

    // Przetwarzanie pliku, obliczanie częstości znaków i tworzenie drzewa Huffmana
    static void przetworzPlik(BufferedReader pin, int[] czesto) throws IOException {
        char[] sl = new char[16];
        int i;
        String line;
        char znaknl= '\n';
        czesto[znaknl]=-1;
        while((line= pin.readLine())!=null){
            char[] znaki=line.toCharArray();
            for(char znak: znaki){
                czesto[znak]++;
            }
            czesto[znaknl]++;
        }
        for (i = 0; i < 128; i++) {
            if (czesto[i] != 0) {
                dodajK(nowyWezel(czesto[i], (char) i, null, null));
            }
        }
        while (koniec > 2) {
            dodajK(nowyWezel(0, (char)0, usunK(), usunK()));
        }
        znajdzKody(q[1],sl,0);
    }

    // Zwraca długość ciągu znaków bez znaku '\0'
    public static int notNull(String slowo){
        char[] sl=slowo.toCharArray();
        int licznik=0;
        for(char znak:sl){
            if(znak!='\0'){
                licznik++;
            }
            else return licznik;
        }
        return licznik;
    }

    // Kodowanie pliku za pomocą algorytmu Huffmana
    static void koduj2(BufferedReader pin, BufferedOutputStream pout, int[] czesto) throws IOException {
        char in;
        char c = 0;
        char[] temp = new char[20];
        int i, j = 0, k = 0, lim = 0;
        for (i = 0; i < 128; i++) {
            if (czesto[i] != 0) {
                lim+=(czesto[i]*notNull(kod[i]));
            }
        }
        pout.write(String.format("%d", czesto['\n']).getBytes(StandardCharsets.UTF_8));
        pout.write("\n".getBytes());
        for(i=0;i<128;i++){
            if(czesto[i]!=0 && i!=(int)'\n'){
                pout.write(i);
                pout.write(String.format("%d", czesto[i]).getBytes(StandardCharsets.UTF_8));
                pout.write("\n".getBytes());
            }
        }
        pout.write("123456789\n".getBytes());
        pout.write(String.format("%04d\n", lim).getBytes(StandardCharsets.UTF_8));
        for (i = 0; i < lim; i++) {
            if (temp[j] == '\0') {
                in = (char) pin.read();
                if((int)in!=65535 && kod[in]!=null){
                    temp=kod[in].toCharArray();
                }
                j = 0;
            }
            if (temp[j] == '1') {
                c |= (1 << (7 - k));
            } else if (temp[j] == '0') {
                c |= (0);
            } else {
                System.out.println("ERROR: Błąd w pliku wejściowym. Kompresja niemożliwa");
            }
            k++;
            j++;
            if (((i + 1) % 8 == 0) || (i == lim - 1)) {
                k = 0;
                pout.write(c);
                c = 0;
            }
        }
    }

    // Metoda kodująca plik
    public static void koduj(String nazwaPliku) {
        try {
            // Ustalenie ścieżki do pliku wejściowego i utworzenie strumieni
            FileInputStream pinrob = new FileInputStream(nazwaPliku);
            BufferedReader pin = new BufferedReader(new InputStreamReader(pinrob));
            BufferedOutputStream pout = new BufferedOutputStream(new FileOutputStream(nazwaPliku+".huffman"));
            int[] czesto = new int[256];
            // Przetworzenie pliku, obliczenie częstości znaków i utworzenie drzewa Huffmana
            przetworzPlik(pin, czesto);

            // Powrót do początku pliku wejściowego i utworzenie nowego strumienia
            pinrob.getChannel().position(0);
            pin = new BufferedReader(new InputStreamReader(pinrob));

            // Kodowanie pliku za pomocą algorytmu Huffmana i zapis do pliku wyjściowego
            koduj2(pin, pout, czesto);

            // Zamknięcie strumieni
            pin.close();
            pout.close();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Zakończono kompresję");
            alert.setHeaderText("Plik został pomyślnie skompresowany");
            alert.setContentText("Plik zapakowany został do "+nazwaPliku+".huffman");
            alert.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
