package day10;

import main.Day;

import java.awt.*;
import java.util.List;
import java.util.*;

public class Day10 implements Day {
    Map<Point, pipe> pipes = new HashMap<>();
    Map<Point, Integer> distances = new HashMap<>();
    Map<Point, dir> mainPipeDir = new HashMap<>();

    int maxX;
    int maxY;
    Point start = null;

    enum dir {
        N, S, E, W;

        boolean isPerpendicular(dir other) {
            if (other == N || other == S) return this == E || this == W;
            return this == N || this == S;
        }


        public boolean isLeftOf(dir approachTowards) {
            return this == N && approachTowards == E
                    || this == E && approachTowards == N
                    || this == S && approachTowards == W
                    || this == W && approachTowards == S;
        }

        public dir flip() {
            if (this == N) return S;
            if (this == S) return N;
            if (this == E) return W;
            if (this == W) return E;
            throw new RuntimeException();
        }
    }

    record tuple(dir left, dir right) {

    }

    enum pipe {
        NS, EW, NE, NW, SW, SE, ground, start;

        boolean goes(dir d) {
            if (d == dir.S) return this == NS || this == SW || this == SE;
            if (d == dir.N) return this == NS || this == NE || this == NW;
            if (d == dir.E) return this == NE || this == SE || this == EW;
            if (d == dir.W) return this == NW || this == EW || this == SW;
            return false;
        }

        tuple exits() {
            return switch (this) {
                case NS -> new tuple(dir.N, dir.S);
                case EW -> new tuple(dir.E, dir.W);
                case NE -> new tuple(dir.N, dir.E);
                case NW -> new tuple(dir.N, dir.W);
                case SW -> new tuple(dir.S, dir.W);
                case SE -> new tuple(dir.S, dir.E);
                default -> throw new RuntimeException("Unexpected value: " + this);
            };
        }

    }

    Point go(Point in, dir d) {
        if (d == dir.S) return new Point(in.x, in.y + 1);
        if (d == dir.W) return new Point(in.x - 1, in.y);
        if (d == dir.E) return new Point(in.x + 1, in.y);
        if (d == dir.N) return new Point(in.x, in.y - 1);
        throw new RuntimeException("what dir do you call this");
    }


    pipe calcStartingPipe() {
        if (start == null) throw new RuntimeException("start is null");
        pipe p;
        p = pipes.get(go(start, dir.N));
        boolean north = p != null && p.goes(dir.S); // this one goes north
        p = pipes.get(go(start, dir.S));
        boolean south = p != null && p.goes(dir.N); // this one goes south
        p = pipes.get(go(start, dir.W));
        boolean west = p != null && p.goes(dir.E); // this one goes west
        p = pipes.get(go(start, dir.E));
        boolean east = p != null && p.goes(dir.W); // this one goes east

        if (north) {
            if (south) return pipe.NS;
            if (west) return pipe.NW;
            if (east) return pipe.NE;
        }
        if (south) {
            if (west) return pipe.SW;
            if (east) return pipe.SE;
        }
        if (east && west) return pipe.EW;
        throw new RuntimeException("couldn't find");
    }

    char getReplacement(char c) {
        return switch (c) {
            case '|' -> '┃';
            case '-' -> '─';
            case 'L' -> '┕';
            case 'J' -> '┙';
            case '7' -> '┓';
            case 'F' -> '┏';
            case 'S' -> 'S';
            default -> throw new IllegalStateException("Unexpected value: " + c);
        };
    }

    pipe getPipe(char c) {
        return switch (c) {
            case '|' -> pipe.NS;
            case '-' -> pipe.EW;
            case 'L' -> pipe.NE;
            case 'J' -> pipe.NW;
            case '7' -> pipe.SW;
            case 'F' -> pipe.SE;
            case '.' -> pipe.ground;
            case 'S' -> pipe.start;
            default -> throw new IllegalStateException("Unexpected value: " + c);
        };
    }

    void addNode(char c, int x, int y) {
        pipe p = getPipe(c);
        if (p == pipe.ground) return;
        if (p == pipe.start) {
            start = new Point(x, y);
            return;
        }
        pipes.put(new Point(x, y), p);
    }

    void setUpPipes(List<String> in) {
        maxY = in.size();
        maxX = in.get(0).length();
        for (int i = 0; i < in.size(); i++) {
            int y = i + 1;
            char[] line = in.get(i).toCharArray();
            for (int j = 0; j < line.length; j++) {
                char c = line[j];
                int x = j + 1;
                addNode(c, x, y);
            }
        }
        var startPipe = calcStartingPipe();
        System.out.println("startPipe = " + startPipe);
        pipes.put(start, startPipe); // manually looked at puzzle input
    }

    void calcDistance() {
        Set<Point> breadthFirst;
        Set<Point> nextSet = new HashSet<>();
        nextSet.add(start);
        int dist = -1;
        while (!nextSet.isEmpty()) {
            dist++;
            breadthFirst = nextSet;
            nextSet = new HashSet<>();
            for (Point p : breadthFirst) {
                if (distances.containsKey(p))
                    throw new RuntimeException("distances already covered, this shouldn't be here");
                distances.put(p, dist);
                pipe pipe = pipes.get(p);
                Point left = go(p, pipe.exits().left);
                Point right = go(p, pipe.exits().right);
                if (!distances.containsKey(left)) nextSet.add(left);
                if (!distances.containsKey(right)) nextSet.add(right);
            }
        }
        System.out.println("dist = " + dist);
    }


    void calcMainPipe() {
        Point curr = start;

        dir currentHeading = pipes.get(start).exits().left;
        do {
            pipe pipe = pipes.get(curr);


            dir exit = pipe.exits().left;
            if (exit == currentHeading.flip()) {
                exit = pipe.exits().right;
            }

            Point next = go(curr, exit);
            mainPipeDir.put(curr, exit);

            currentHeading = exit;
            curr = next;
        } while (!curr.equals(start));
        System.out.println("done");
    }


    boolean isLeftOf(Point p, dir approachTowards) {

        pipe pipe = pipes.get(p);
        dir travel = mainPipeDir.get(p);

        if (travel.isPerpendicular(approachTowards)) {
            return travel.isLeftOf(approachTowards);
        }

        // otherwise its entry that is perpendicular
        dir entry = pipe.exits().left;
        if (entry == travel) {
            entry = pipe.exits().right;
        }
        entry = entry.flip();

        return entry.isLeftOf(approachTowards);

    }

    public void doThing(List<String> in) {
        setUpPipes(in);

        calcMainPipe();

//        var mainLoop ;
//        Set<Point> keySet = new HashSet<>(pipes.keySet());
//        for (var point : keySet) {
//            if (!mainPipeDir.containsKey(point)) {
//                pipes.remove(point);
//            }
//        }
        // pipes is now only the main loop
        int sumI = 0;
        for (int y = 1; y <= maxY; y++) {
            for (int x = 1; x <= maxX; x++) {
                Point p = new Point(x, y);
                if (mainPipeDir.containsKey(p)) {
//                    System.out.print(getReplacement(in.get(y - 1).charAt(x - 1)));
//                    System.out.print(" ");
                    System.out.print(mainPipeDir.get(p).name());
                    continue;
                }

                boolean a = isPointInsideLoop(p);
                if (a) sumI++;
                System.out.print(a ? "I" : "O");

//                if (pipes.containsKey(p)) {
//                    if (mainPipeDir.containsKey(p)) System.out.print(getReplacement(in.get(y - 1).charAt(x - 1)));
//                    else System.out.print("I");
//                } else System.out.print(" ");


            }
            System.out.println();
        }


        System.out.println("sumI = " + sumI);
    }

    private boolean isPointInsideLoop(Point p) {
        int x = p.x;
        int y = p.y;

        if (p.equals(new Point(11, 2))) {
            System.out.print("");
        }

        while (x > 1) {
            Point forward = new Point(x - 1, y);
            if (mainPipeDir.containsKey(forward)) {
                return !isLeftOf(forward, dir.W);
            }
            x--;
        }

        while (y > 1) {
            Point forward = new Point(x, y - 1);
            if (mainPipeDir.containsKey(forward)) {
                return isLeftOf(forward, dir.N);
            }
            y--;
        }
        return false;
    }

}
