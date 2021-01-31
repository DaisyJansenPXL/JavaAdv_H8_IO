package be.pxl.ja.phonebook;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class PhoneApp {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        Map<String, String> phoneBook = new HashMap<>();

        try (BufferedReader reader = Files.newBufferedReader(Paths.get("resources/phonedirectory.txt"), Charset.defaultCharset())) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] contact = line.split(";");
                String[] numbers = Arrays.stream(contact).skip(1).toArray(String[]::new);
                phoneBook.put(contact[0], Arrays.stream(numbers).map(n -> n).collect(Collectors.joining(";")));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        phoneBook.forEach((k, v) -> System.out.println("Naam: " + k + " Nummer(s): " + v));

        boolean running = true;
        while (running) {
            System.out.println("Geef een naam: ");
            String naam = input.nextLine();
            if (phoneBook.containsKey(naam)) {
                System.out.println("Geef een telefoonnummer: ");
                String telnr = input.nextLine();
                if (phoneBook.get(naam).contains(telnr)) {
                    throw new RuntimeException("Dit nummer is reeds toegevoegd");
                }
                else {
                    phoneBook.put(naam, phoneBook.get(naam) + ";" + telnr);
                }
            }
            else {
                System.out.println("Geef een telefoonnummer: ");
                String telnr = input.nextLine();
            }

            System.out.println("Wilt u nog namen toevoegen? (j/n)");
            running = input.nextLine().equals("j");
        }

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("resources/phonedirectory.txt"))) {
            for (Map.Entry<String, String> entry : phoneBook.entrySet()) {
                StringBuilder s= new StringBuilder();
                s.append(entry.getKey() + ";" + entry.getValue());
                writer.write(String.valueOf(s) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        input.close();
    }
}
