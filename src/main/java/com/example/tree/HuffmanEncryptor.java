package com.example.tree;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class HuffmanEncryptor {
    private byte[] key;

    public HuffmanEncryptor(byte[] key) {
        this.key = key;
    }

    public static void koduj(String nazwaPliku, String haslo){
        if(haslo.equals("")){
            haslo="0";
        }
        HuffmanCoding.koduj(nazwaPliku);
        HuffmanEncryptor.szyfruj(nazwaPliku+".huffman",haslo);
    }
    public static void dekoduj(String nazwaPliku, String haslo){
        if(haslo.equals("")){
            haslo="0";
        }
        HuffmanEncryptor.szyfruj(nazwaPliku,haslo);
        HuffmanDecoding.dekoduj(nazwaPliku);
    }

    private static void szyfruj(String inputFile, String haslo) {
        byte[] key = haslo.getBytes();
        HuffmanEncryptor encryptor = new HuffmanEncryptor(key);
        String encryptedFile = inputFile+".rob";
        try {
            encryptor.encrypt(inputFile, encryptedFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Files.deleteIfExists(Path.of(inputFile));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        File file = new File(encryptedFile);
        File filenew = new File(inputFile);
        file.renameTo(filenew);
    }

    private void encrypt(String inputFilePath, String outputFilePath) throws IOException {
        FileInputStream inputFile = new FileInputStream(inputFilePath);
        FileOutputStream outputFile = new FileOutputStream(outputFilePath);

        byte[] buffer = new byte[4096];
        int bytesRead;
        int keyLength = key.length;

        while ((bytesRead = inputFile.read(buffer)) != -1) {
            for (int i = 0; i < bytesRead; i++) {
                buffer[i] ^= key[i % keyLength];
            }
            outputFile.write(buffer, 0, bytesRead);
        }

        inputFile.close();
        outputFile.close();
    }
}
