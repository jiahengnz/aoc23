package day14;

import main.Day;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day14 implements Day {

    //    List<Vector2> cubes = new ArrayList<>();
    final char round = 'O';
    final char square = '#';
    final char dirt = '.';

    char[] gridState;

//    class Round {
//        Vector2 pos;
//
//        void tilt()
//    }

    int maxY;
    int maxX;

//    List<Integer> stopperAtEachCol = new ArrayList<>();

    public void doThing(List<String> in) {
        maxY = in.size();
        maxX = in.get(0).length();

        gridState = new char[maxY * maxX];

//        for (int i = 0; i < maxX; i++) {
//            stopperAtEachCol.add(-1);
//        }


        for (int y = 0; y < in.size(); y++) {
            String line = in.get(y);
            for (int x = 0; x < line.toCharArray().length; x++) {
                char c = line.charAt(x);
                // if (c != '.')
//                if (c == '#') {
//                    stopperAtEachCol.set(x, y);
//                    cubes.add(new Vector2(x, y));
                gridState[y * maxX + x] = c;
//                } else if (c == 'O') {
//                    int rollToRow = stopperAtEachCol.get(x) + 1;
//                    stopperAtEachCol.set(x, rollToRow);
//                    weightSum += (maxY - rollToRow);
//                    gridState[y * maxX + x] = 2;
            }
        }


        for (int i = 1; i <= 20+35+35+35; i++) {
            System.out.println("i = " + (i));
            spinCycle();

        }
//        tiltGridNS(true);

//        print();

        int weightSum = calcWeightSumFromGridState();
        System.out.println(weightSum);

    }

    void print() {
        System.out.println("grid state:");
        for (int y = 0; y < maxY; y++) {
            for (int x = 0; x < maxX; x++) {
                System.out.print(gridState[y * maxX + x]);
            }
            System.out.println();
        }
    }


    Map<String, String> cache = new HashMap<>();

    void tiltGridNS(boolean goingNorth) {
        final int calcDirection = goingNorth ? 1 : -1;
        final int startY = goingNorth ? 0 : maxY - 1;
        final int endY = goingNorth ? maxY : -1;

        for (int x = 0; x < maxX; x++) {
            int yBlocker = startY - calcDirection;
            for (int y = startY; y != endY; y += calcDirection) {
                if (gridState[y * maxX + x] == square) {
                    yBlocker = y;
                } else if (gridState[y * maxX + x] == round) {
                    yBlocker += calcDirection;
                    gridState[y * maxX + x] = dirt;
                    gridState[yBlocker * maxX + x] = round;
                }
            }
        }
    }

    void spinCycle() {
        String key = new String(gridState);
        if (cache.containsKey(key)) {
            System.out.println("HIT! " + key.hashCode());
            gridState = (cache.get(key).toCharArray());
        } else {
            System.out.println("NO HIT! " +key.hashCode());
            tiltGridNS(true);
            tiltGridWE(true);
            tiltGridNS(false);
            tiltGridWE(false);
            cache.put(key, new String(gridState));
        }
    }

    void tiltGridWE(boolean goingWest) {
        final int calcDirection = goingWest ? 1 : -1;
        final int startX = goingWest ? 0 : maxX - 1;
        final int endX = goingWest ? maxX : -1;

        for (int y = 0; y < maxY; y++) {
            int xBlocker = startX - calcDirection;
            for (int x = startX; x != endX; x += calcDirection) {
                if (gridState[y * maxX + x] == square) {
                    xBlocker = x;
                } else if (gridState[y * maxX + x] == round) {
                    xBlocker += calcDirection;
                    gridState[y * maxX + x] = dirt;
                    gridState[y * maxX + xBlocker] = round;
                }
            }
        }
    }

    private int calcWeightSumFromGridState() {
        int weightSum = 0;
        for (int x = 0; x < maxX; x++) {
            for (int y = 0; y < maxY; y++) {
                if (gridState[y * maxX + x] == round) {
                    weightSum += (maxY - y);
                }
            }
        }
        return weightSum;
    }

}
