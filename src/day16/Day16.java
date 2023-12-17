package day16;

import main.Day;
import main.Vector2;

import java.util.*;

public class Day16 implements Day {

    char[] puzzle;

    boolean[] energyTracker;

    int countEnergized() {
        int sum = 0;
        for (boolean c : energyTracker) if (c) sum++;
        return sum;
    }

    record Transform(Vector2 position, Vector2 direction) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Transform transform = (Transform) o;
            return Objects.equals(position, transform.position) && Objects.equals(direction, transform.direction);
        }

        @Override
        public int hashCode() {
            return Objects.hash(position, direction);
        }
    }

    Queue<Transform> activeRays;

    Set<Transform> cache;

    int maxY;
    int maxX;

    int getIndex(int x, int y) {
        return maxX * y + x;
    }

    int getIndex(Vector2 t) {
        return getIndex(t.x, t.y);
    }


    void readPuzzle(List<String> in) {
        maxY = in.size();
        maxX = in.get(0).length();
        puzzle = new char[maxX * maxY];
        energyTracker = new boolean[maxX * maxY];

        for (int y = 0; y < maxY; y++) {
            String line = in.get(y);
            for (int x = 0; x < maxX; x++) {
                puzzle[getIndex(x, y)] = line.charAt(x);
            }
        }
    }

    int calcAllStart() {

        int max = 0;

        for (int x = 0; x < maxX; x++) {
            energyTracker = new boolean[maxX * maxY];
            calcRays(new Transform(new Vector2(x, maxY), Vector2.UP));
            max = Math.max(max, countEnergized());
        }

        for (int y = 0; y < maxY; y++) {
            energyTracker = new boolean[maxX * maxY];
            calcRays(new Transform(new Vector2(-1, y), Vector2.RIGHT));
            max = Math.max(max, countEnergized());
        }

        for (int y = 0; y < maxY; y++) {
            energyTracker = new boolean[maxX * maxY];
            calcRays(new Transform(new Vector2(maxX, y), Vector2.LEFT));
            max = Math.max(max, countEnergized());
        }

        for (int x = 0; x < maxX; x++) {
            energyTracker = new boolean[maxX * maxY];
            calcRays(new Transform(new Vector2(x, -1), Vector2.DOWN));
            max = Math.max(max, countEnergized());
        }
        return max;
    }

    void calcRays(Transform start) {
        activeRays = new ArrayDeque<>();
        cache = new HashSet<>();

//        Transform start = new Transform(new Vector2(0, 0), Vector2.DOWN);
        activeRays.add(start);


        while (!activeRays.isEmpty()) {
            Transform activeRay = activeRays.poll();
            if (cache.contains(activeRay)) {
                continue;
            }

            if (!activeRay.equals(start))
                energyTracker[getIndex(activeRay.position)] = true;
            cache.add(activeRay);

            Vector2 lookAhead = activeRay.position.add(activeRay.direction);
            if (!lookAhead.isInBound(0, 0, maxX, maxY)) continue;
            char ahead = puzzle[getIndex(lookAhead)];
            if (ahead == '.'
                    || ahead == '|' && activeRay.direction.isUpDown()
                    || ahead == '-' && activeRay.direction.isLeftRight()) {

                activeRays.add(new Transform(lookAhead, activeRay.direction));
                continue;
            }
            if (ahead == '|') {
                activeRays.add(new Transform(lookAhead, Vector2.UP));
                activeRays.add(new Transform(lookAhead, Vector2.DOWN));
                continue;
            }
            if (ahead == '-') {
                activeRays.add(new Transform(lookAhead, Vector2.RIGHT));
                activeRays.add(new Transform(lookAhead, Vector2.LEFT));
                continue;
            }
            if (ahead == '/') {
                activeRays.add(
                        new Transform(lookAhead,
                                activeRay.direction.isUpDown() ?
                                        activeRay.direction.turnRight() : activeRay.direction.turnLeft()));
                continue;
            }
            if (ahead == '\\') {
                activeRays.add(
                        new Transform(lookAhead,
                                activeRay.direction.isUpDown() ?
                                        activeRay.direction.turnLeft() : activeRay.direction.turnRight()));
                continue;
            }

        }
    }

    public void doThing(List<String> in) {
        readPuzzle(in);

//        count

//        calcRays(new Transform(new Vector2(-1,0), Vector2.RIGHT));
//        System.out.println("countEnergized() = " + countEnergized());

        System.out.println("calcAllStart() = " + calcAllStart());


//        System.out.println("countEnergized() = " + countEnergized());

    }
}
