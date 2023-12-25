package day23;

import main.Day;
import main.Vector2;

import java.util.*;

import static main.Vector2.*;

public class Day23 implements Day {

    class Intersection {
        List<Corridor> corridors = new ArrayList<>();
    }

    class Corridor {
        public int length;
        Vector2 start, end;
    }


    Map<Vector2, Intersection> intersectionMap = new HashMap<>();
    Map<Vector2, Corridor> corridor = new HashMap<>();

    public void doThing(List<String> in) {
        readInput(in);
//        System.out.println("calcLongestPath() = " + calcLongestPath(start, DOWN));
        buildNodes();
        validateNodes();
        int longest = calcLongestPath2(start, new ArrayList<>());
        System.out.println("longest = " + longest);
    }

    private void validateNodes() {
        for (var i : intersectionMap.entrySet()) {
            if (i.getKey().equals(start)) continue;
            if (i.getKey().equals(goal)) continue;
            if (i.getKey().equals(lastIntersection)) continue;
            if (i.getValue().corridors.size() != 3 && i.getValue().corridors.size() != 4)
                throw new RuntimeException("too many corridors in " + i.getKey());
        }

        System.out.println("validate done");
    }

    private int calcLongestPath2(Vector2 pos, List<Vector2> seenIntersections) {
        if (pos.equals(goal)) {
            return 0;
        }
        int max = -1;

        Intersection thisInter = intersectionMap.get(pos);
        List<Vector2> newSeen = new ArrayList<>(seenIntersections);
        newSeen.add(pos);

        for (Corridor corr : thisInter.corridors) {
            Vector2 lookAhead = (corr.start.equals(pos)) ? corr.end : corr.start;
            if (seenIntersections.contains(lookAhead)) {
                continue;
            }
            int newPath = corr.length + calcLongestPath2(lookAhead, newSeen);
            if (newPath > -1) {
                max = Math.max(max, newPath + 1);
            }
        }
        return max;
    }

    public int countExits(int x, int y) {
        int exits = 0;
        if (x > 0) exits += map[ind(x - 1, y)] != '#' ? 1 : 0;
        if (x < maxX - 1) exits += map[ind(x + 1, y)] != '#' ? 1 : 0;
        if (y > 0) exits += map[ind(x, y - 1)] != '#' ? 1 : 0;
        if (y < maxY - 1) exits += map[ind(x, y + 1)] != '#' ? 1 : 0;
        return exits;
    }

    record ConstructionThing(Vector2 intersection, Vector2 corrStart, Vector2 startingDir) {

    }

    private void buildNodes() {
        Intersection startIntersection = new Intersection();
        this.intersectionMap.put(new Vector2(1, 0), startIntersection);

        Queue<ConstructionThing> corridorsToBuild = new ArrayDeque<>();
        corridorsToBuild.add(new ConstructionThing(new Vector2(1, 0), start.add(SOUTH), SOUTH));

        while (!corridorsToBuild.isEmpty()) {
            ConstructionThing constructionThing = corridorsToBuild.poll();
            Vector2 corrStart = constructionThing.corrStart;

            if (corridor.containsKey(corrStart)) {
                continue;
            }

            Vector2 currPos = corrStart;
            Vector2 currDir = constructionThing.startingDir;

            // find the end of the corridor
            Vector2 endIntersection = null;
            int length = 1;
            while (endIntersection == null) {
                for (Vector2 dir : directions) {
                    if (dir.equals(currDir.flip())) continue;
                    Vector2 lookAhead = currPos.add(dir);
                    if (lookAhead.equals(goal)) {
                        endIntersection = lookAhead;
                        break;
                    }
                    if (!lookAhead.isInBound(maxX, maxY)) continue;
                    char lookAheadChar = map[ind(lookAhead.x, lookAhead.y)];
                    if (lookAheadChar == '#') {
                        continue;
                    }

                    int lookAheadExits = countExits(lookAhead.x, lookAhead.y);
                    if (lookAheadExits > 2) {
                        endIntersection = lookAhead;
                        break;
                    }
                    currPos = lookAhead;
                    currDir = dir;
                    length++;

                    break;
                }
            }
            Vector2 corrEnd = currPos;
            Corridor newCorridor = new Corridor();

            if (!intersectionMap.containsKey(endIntersection)) {
                intersectionMap.put(endIntersection, new Intersection());
            }
            newCorridor.length = length;
            newCorridor.start = constructionThing.intersection;
            newCorridor.end = endIntersection;

            intersectionMap.get(constructionThing.intersection).corridors.add(newCorridor);
            intersectionMap.get(endIntersection).corridors.add(newCorridor);
            corridor.put(corrStart, newCorridor);
            corridor.put(corrEnd, newCorridor);


            for (Vector2 dir : directions) {
                Vector2 lookAhead = endIntersection.add(dir);
                if (!lookAhead.isInBound(maxX, maxY)) continue;
                char lookAheadChar = map[ind(lookAhead.x, lookAhead.y)];
                if (lookAheadChar == '#') {
                    continue;
                }
                if (corridor.containsKey(lookAhead)) continue;
                corridorsToBuild.add(new ConstructionThing(endIntersection, lookAhead, dir));
            }

        }

        Corridor endCorridor = intersectionMap.get(goal).corridors.get(0);
        lastIntersection = endCorridor.start;
        Intersection last = intersectionMap.get(lastIntersection);
        last.corridors = List.of(endCorridor);

    }

    Vector2 lastIntersection;

    private int calcLongestPath(Vector2 pos, Vector2 approach) {
        if (pos.equals(goal)) {
            return 0;
        }
        int max = -1;
        for (Vector2 dir : directions) {
            if (dir.equals(approach.flip())) continue;
            Vector2 lookAhead = pos.add(dir);
            if (!lookAhead.isInBound(maxX, maxY)) continue;
            char lookAheadChar = map[ind(lookAhead.x, lookAhead.y)];
            if ((lookAheadChar == '>' && !dir.equals(EAST)) ||
                    (lookAheadChar == 'v' && !dir.equals(SOUTH)) ||
                    (lookAheadChar == '<' && !dir.equals(WEST)) ||
                    (lookAheadChar == '^' && !dir.equals(NORTH)) || lookAheadChar == '#') {
                continue;
            }

            int newPath = calcLongestPath(lookAhead, dir);
            if (newPath > -1) {
                max = Math.max(max, newPath + 1);
            }
        }
        return max;
    }

    int maxX, maxY;
    int maxScore;
    char[] map;

    int ind(int x, int y) {
        return y * maxX + x;
    }

    Vector2 start = new Vector2(1, 0);
    Vector2 goal;

    private void readInput(List<String> in) {
        maxY = in.size();
        maxX = in.get(0).length();
        goal = new Vector2(maxX - 2, maxY - 1);
        maxScore = maxY * maxX;
        map = new char[maxX * maxY];
        for (int y = 0; y < maxY; y++) {
            String line = in.get(y);
            for (int x = 0; x < maxX; x++) {
                map[ind(x, y)] = line.charAt(x);
            }
        }
    }

}
