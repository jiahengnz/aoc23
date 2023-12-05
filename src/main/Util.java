package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Util {

    public static List<String> readFile(File f) {
        try (Scanner scanner = new Scanner(f)) {
            List<String> data = new ArrayList<>();
            while (scanner.hasNextLine()) {
                data.add(scanner.nextLine());
            }
            return data;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> getInput(int day) {
        return readFile(new File("src/day" + day +"/input.txt"));
    }

    public static List<String> getTestInput(int day) {
        return readFile(new File("src/day" + day +"/test.txt"));
    }
}
