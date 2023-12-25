package day21;

import main.Day;
import main.Vector2;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static main.Vector2.WEST;

public class Day21 implements Day {

    Set<Vector2> depthSearch;

    public void doThing(List<String> in) {
        readInput(in);

        List<Vector2> dirs = List.of(Vector2.NORTH, Vector2.SOUTH, Vector2.EAST, WEST);

        depthSearch = new HashSet<>();
        depthSearch.add(new Vector2(65, 65));
        for (int i = 0; i < 4*131+65; i++) {
            Set<Vector2> newFront = new HashSet<>();
            for (Vector2 front : depthSearch) {
                for (Vector2 dir : dirs) {
                    Vector2 c = front.add(dir);
                    if (
//                            c.isInBound(maxX, maxY) &&
                            map[getIndex(c.modulus(maxX, maxY))] != '#') {
                        newFront.add(c);
                    }
                }
            }
            depthSearch = newFront;
        }
        System.out.println(depthSearch.size());
    }

    char[] map;
    int maxY;
    int maxX;
    Vector2 starting;

    int getIndex(Vector2 v) {
        return getIndex(v.x, v.y);
    }

    int getIndex(int x, int y) {
        return y * maxX + x;
    }

    private void readInput(List<String> in) {
        maxY = in.size();
        maxX = in.get(0).length();
        map = new char[maxY * maxX];
        for (int y = 0; y < maxY; y++) {
            for (int x = 0; x < maxX; x++) {
                char c = in.get(y).charAt(x);
//                if (c == 'S') {
//                    System.out.println("x + \" \" + y = " + x + " " + y);
////                    starting = new Vector2(x, y);
//                }
                map[getIndex(x, y)] = c;
            }
        }
    }

}
