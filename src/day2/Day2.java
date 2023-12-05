package day2;

import main.Day;

import java.util.List;
import java.util.Scanner;

public class Day2 implements Day {

    int maxRed = 12;
    int maxGreen = 13;
    int maxBlue = 14;


    @Override
    public void doThing(List<String> input) {
        int sum = 0;
        for (int i = 0; i < input.size(); i++) {
            int game = i + 1;
            String line = input.get(i);
            line = line.substring(line.indexOf(":") + 2);
//            if (lineViolatesMax(line)) continue;
//            System.out.println("line = " + line);
//            System.out.println("game = " + game);
            int pow = powerOfGame(line);
            System.out.println("pow = " + pow);
            sum += pow;
        }
        System.out.println("sum = " + sum);

    }

    private int powerOfGame(String line) {
        Scanner sc = new Scanner(line);

        int minR = 0;
        int minG = 0;
        int minB = 0;

        while (sc.hasNextInt()) {
            int cubes = sc.nextInt();
            String color = sc.next();
            if (color.contains("blue")) {
                if (cubes > minB) minB = cubes;
            }
            if (color.contains("red")) {
                if (cubes > minR) minR = cubes;
            }
            if (color.contains("green")) {
                if (cubes > minG) minG = cubes;
            }
        }
        System.out.println("minB = " + minB);
        System.out.println("minG = " + minG);
        System.out.println("minR = " + minR);
        return minR * minG * minB;
    }

    private boolean lineViolatesMax(String line) {
        Scanner sc = new Scanner(line);
        while (sc.hasNextInt()) {
            int cubes = sc.nextInt();
            String color = sc.next();
            if (color.contains("blue")) {
                if (cubes > maxBlue) return true;
            } else if (color.contains("red")) {
                if (cubes > maxRed) return true;
            } else if (color.contains("green")) {
                if (cubes > maxGreen) return true;
            }

        }
        return false;
    }
}