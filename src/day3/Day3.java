package day3;

import main.Day;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day3 implements Day {

    List<Point> symbols = new ArrayList<>();

    Map<Point, Integer> gearRatios = new HashMap<>();

    private boolean isAdjacentToSymbol(int x, int y, int length) {
        for (Point sym : symbols) {
            int symY = sym.y;
            if (symY != y - 1 && symY != y && symY != y + 1) {
                continue;
            }

            int symX = sym.x;
            if (symX < x - 1 || symX > x + length) {
                continue;
            }

            return true;
        }
        return false;
    }

    private Point getAdjacentGear(int x, int y, int length) {
        for (Point sym : symbols) {
            int symY = sym.y;
            if (symY != y - 1 && symY != y && symY != y + 1) {
                continue;
            }

            int symX = sym.x;
            if (symX < x - 1 || symX > x + length) {
                continue;
            }

            return sym;
        }
        return null;
    }

    public void doThing(List<String> in) {
        for (int y = 0; y < in.size(); y++) {
            String line = in.get(y);
            char[] charArray = line.toCharArray();
            for (int x = 0; x < charArray.length; x++) {
//                if (charArray[x] >= '0' && charArray[x] <= '9' || charArray[x] == '.') continue;
                if (charArray[x] == '*') symbols.add(new Point(x, y));
            }
        }

//        int engineSum = 0;
        int gearRatioSum = 0;
        for (int y = 0; y < in.size(); y++) {
            String line = in.get(y);
            char[] charArray = line.toCharArray();
            StringBuilder foundNumber = new StringBuilder();

            int numberStartX = -1;
            for (int x = 0; x < charArray.length; x++) {

                if (charArray[x] >= '0' && charArray[x] <= '9') {
                    if (numberStartX == -1) {
                        numberStartX = x;
                    }
                    foundNumber.append(charArray[x]);
                    if (x != charArray.length - 1) continue;
                }
                if (numberStartX > -1) { // not a number, try end number if it exists
                    Point gear = getAdjacentGear(numberStartX, y, x - numberStartX);
//                    if (isAdjacentToSymbol(numberStartX, y, x - numberStartX)) {
                    if (gear != null) {
                        int engineValue = Integer.parseInt(foundNumber.toString());
                        System.out.printf("value = %d \t\t @ \t %d,%d \t len %d \n", engineValue,numberStartX,y, x-numberStartX);
                        if (gearRatios.containsKey(gear)) {
                            gearRatioSum += (gearRatios.get(gear) * engineValue);
                        } else {
                            gearRatios.put(gear, engineValue);
                        }
//                        engineSum += engineValue;
                    }
                    numberStartX = -1;
                    foundNumber = new StringBuilder();
                }
            }
        }
//        System.out.println("engineSum = " + engineSum);
        System.out.println("gearRatioSum = " + gearRatioSum);
    }


}
