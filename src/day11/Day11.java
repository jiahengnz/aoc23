package day11;

import main.Day;
import main.Vector2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day11 implements Day {

    List<Vector2> galaxyPos = new ArrayList<>();

    int maxY;
    int maxX;

    Set<Integer> emptyCols = new HashSet<>();
    Set<Integer> emptyRows = new HashSet<>();

    void generateGalaxy(List<String> in) {
        maxY = in.size();
        maxX = in.get(0).toCharArray().length;

        fillEmpties();

        for (int y = 0; y < maxY; y++) {
            String line = in.get(y);
            for (int x = 0; x < maxX; x++) {
                if (line.charAt(x) == '#') {
                    galaxyPos.add(new Vector2(x, y));
                    emptyCols.remove(x);
                    emptyRows.remove(y);
                }
            }
        }
    }

    void fillEmpties() {
        for (int x = 0; x < maxX; x++) {
            emptyCols.add(x);
        }
        for (int y = 0; y < maxY; y++) {
            emptyRows.add(y);
        }
    }

    int calcManhattenDistance(Vector2 a, Vector2 b) {
        int length = 0;
        int mult = 1_000_000;
        int startX = Math.min(a.x, b.x);
        int startY = Math.min(a.y, b.y);
        int endX = Math.max(a.x, b.x);
        int endY = Math.max(a.y, b.y);

        for (int x = startX; x < endX; x++) {
            length += emptyCols.contains(x) ? mult : 1;
        }
        for (int y = startY; y < endY; y++) {
            length += emptyRows.contains(y) ? mult : 1;
        }
        return length;
    }

    public void doThing(List<String> in) {
        generateGalaxy(in);
        long sum = 0;
        for (int i = 0; i < galaxyPos.size(); i++) {
            for (int j = i + 1; j < galaxyPos.size(); j++) {
                int dist = calcManhattenDistance(galaxyPos.get(i), galaxyPos.get(j));
                System.out.println("i = " + (i + 1));
                System.out.println("j = " + (j + 1));
                System.out.println("dist = " + dist);
                sum += dist;
            }
        }
        System.out.println("sum = " + sum);
    }

}
