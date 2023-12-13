package day10;

import main.Day;
import main.Vector2;

import java.util.*;

public class Day10 implements Day {
    Map<Vector2, pipe> pipes = new HashMap<>();
    Map<Vector2, Integer> distances = new HashMap<>();
    Map<Vector2, Vector2> mainPipeDir = new HashMap<>();

    int maxX;
    int maxY;
    Vector2 start = null;


    record tuple(Vector2 left, Vector2 right) {

    }

    enum pipe {
        NS, EW, NE, NW, SW, SE, ground, start;

        boolean goes(Vector2 d) {
            if (d.equals(Vector2.SOUTH)) return this == NS || this == SW || this == SE;
            if (d.equals(Vector2.NORTH)) return this == NS || this == NE || this == NW;
            if (d.equals(Vector2.EAST)) return this == NE || this == SE || this == EW;
            if (d.equals(Vector2.WEST)) return this == NW || this == EW || this == SW;
            return false;
        }

        tuple exits() {
            return switch (this) {
                case NS -> new tuple(Vector2.NORTH, Vector2.SOUTH);
                case EW -> new tuple(Vector2.EAST, Vector2.WEST);
                case NE -> new tuple(Vector2.NORTH, Vector2.EAST);
                case NW -> new tuple(Vector2.WEST, Vector2.NORTH);
                case SW -> new tuple(Vector2.SOUTH, Vector2.WEST);
                case SE -> new tuple(Vector2.EAST, Vector2.SOUTH);
                default -> throw new RuntimeException("Unexpected value: " + this);
            };
        }

    }


    pipe calcStartingPipe() {
        if (start == null) throw new RuntimeException("start is null");
        pipe p;
        p = pipes.get(start.add(Vector2.NORTH));
        boolean north = p != null && p.goes(Vector2.SOUTH); // this one goes north
        p = pipes.get(start.add(Vector2.SOUTH));
        boolean south = p != null && p.goes(Vector2.NORTH); // this one goes south
        p = pipes.get(start.add(Vector2.WEST));
        boolean west = p != null && p.goes(Vector2.EAST); // this one goes west
        p = pipes.get(start.add(Vector2.EAST));
        boolean east = p != null && p.goes(Vector2.WEST); // this one goes east

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
        throw new RuntimeException("couldn't find" + north + south + west + east);
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
            start = new Vector2(x, y);
            return;
        }
        pipes.put(new Vector2(x, y), p);
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
        Set<Vector2> breadthFirst;
        Set<Vector2> nextSet = new HashSet<>();
        nextSet.add(start);
        int dist = -1;
        while (!nextSet.isEmpty()) {
            dist++;
            breadthFirst = nextSet;
            nextSet = new HashSet<>();
            for (Vector2 p : breadthFirst) {
                if (distances.containsKey(p))
                    throw new RuntimeException("distances already covered, this shouldn't be here");
                distances.put(p, dist);
                pipe pipe = pipes.get(p);
                Vector2 left = p.add(pipe.exits().left);
                Vector2 right = p.add(pipe.exits().right);
                if (!distances.containsKey(left)) nextSet.add(left);
                if (!distances.containsKey(right)) nextSet.add(right);
            }
        }
        System.out.println("dist = " + dist);
    }


    void calcMainPipe() {
        Vector2 curr = start;

        Vector2 currentHeading = pipes.get(start).exits().right.flip();
        do {
            pipe pipe = pipes.get(curr);

            tuple exits = pipe.exits();
            Vector2 exit = pipe.exits().left;
            if (exit.equals(currentHeading.flip())) {
                exit = pipe.exits().right;
            }

            Vector2 next = curr.add(exit);
            mainPipeDir.put(curr, exit);

            currentHeading = exit;
            curr = next;
        } while (!curr.equals(start));
        System.out.println("done");
    }


    boolean isLeftTurnIntoPipe(Vector2 approachTowards, Vector2 p) {

        pipe pipe = pipes.get(p);
        Vector2 travel = mainPipeDir.get(p);

        if (travel.isPerpendicularTo(approachTowards)) {
            return approachTowards.isLeftTurn(travel);
        }

        // otherwise its entry that is perpendicular
        Vector2 entry = pipe.exits().left;
        if (entry.equals(travel)) {
            entry = pipe.exits().right;
        }
        entry = entry.flip();

        return approachTowards.isLeftTurn(entry);

    }

    public void doThing(List<String> in) {
        setUpPipes(in);

        calcMainPipe();

//        var mainLoop ;
//        Set<Vector2> keySet = new HashSet<>(pipes.keySet());
//        for (var point : keySet) {
//            if (!mainPipeDir.containsKey(point)) {
//                pipes.remove(point);
//            }
//        }
        // pipes is now only the main loop
        int sumI = 0;
        for (int y = 1; y <= maxY; y++) {
            for (int x = 1; x <= maxX; x++) {
                Vector2 p = new Vector2(x, y);
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

    private boolean isPointInsideLoop(Vector2 p) {
        int x = p.x;
        int y = p.y;

//        if (p.equals(new Vector2(6, 8))) {
//            System.out.print("");
//        }

        while (x > 1) {
            Vector2 forward = new Vector2(x - 1, y);
            if (mainPipeDir.containsKey(forward)) {
                return !isLeftTurnIntoPipe(Vector2.WEST, forward);
            }
            x--;
        }
        return false;
    }

}
