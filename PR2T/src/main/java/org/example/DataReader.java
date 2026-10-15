package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public class DataReader {
    public List<String> readLines(String filePath) {
        List<String> lines = new ArrayList<>();
        // Відкриваємо файл із використанням UTF-8 кодування
            try (BufferedReader br = new BufferedReader(new FileReader(filePath, StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    lines.add(line);
                }
            } catch (Exception e) {
                System.err.println("Помилка під час зчитування файлу: " + e.getMessage());
            }

            return lines;
        }
    }
