package day17;

import main.Day;
import main.Vector2;

import java.util.*;

public class Day17 implements Day {
    int[] heatLoss;

    record state(Vector2 position, Vector2 direction, int straight) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            state state = (state) o;
            return
                    straight == state.straight &&
                            Objects.equals(position, state.position)
                            && Objects.equals(direction, state.direction)
                    ;
        }

        @Override
        public int hashCode() {
            return Objects.hash(position, direction, straight);
//            return Objects.hash(position);
        }
    }

    Vector2 goal;
    //    Map<state, Long> cache = new HashMap<>();
    static Map<state, Long> fScore = new HashMap<>();

    long findHeatLossToEnd() {
        PriorityQueue<state> openSet = new PriorityQueue<>((o1, o2) -> {
            long o1f = fScore.computeIfAbsent(o1, a -> 1000000000L);
            long o2f = fScore.computeIfAbsent(o2, a -> 1000000000L);
            return (int) (o1f - o2f);
        });
        state start = new state(Vector2.RIGHT, Vector2.RIGHT, 1);
        state start2 = new state(Vector2.DOWN, Vector2.DOWN, 1);
        openSet.add(start);
        openSet.add(start2);

        Map<state, state> cameFrom = new HashMap<>();

        Map<state, Long> gScore = new HashMap<>();
        gScore.put(start, (long) heatLoss[getIndex(start.position)]);
        gScore.put(start2, (long) heatLoss[getIndex(start2.position)]);

// For node n, fScore[n] := gScore[n] + h(n). fScore[n] represents our current best guess as to
        // how cheap a path could be from start to finish if it goes through n.
        fScore = new HashMap<>(); // map with default value of Infinity
//        fScore[start] := h(start)
        fScore.put(start, (long) start.position.taxicabDist(goal));
        fScore.put(start2, (long) start2.position.taxicabDist(goal));

        while (!openSet.isEmpty()) {
            // This operation can occur in O(Log(N)) time if openSet is a min-heap or a priority queue
            state current = openSet.poll();
            if (current.position.equals(goal))
                return reconstruct_path(cameFrom, current);

            List<state> neighbours = new ArrayList<>();
            if (current.straight < 10 && current.position.add(current.direction).isInBound(0, 0, maxX, maxY)) {
                neighbours.add(new state(current.position.add(current.direction), current.direction, current.straight + 1));
            }
            if (current.straight >= 4 && current.position.add(current.direction.turnLeft()).isInBound(0, 0, maxX, maxY)) {
                neighbours.add(new state(current.position.add(current.direction.turnLeft()), current.direction.turnLeft(), 1));
            }
            if (current.straight >= 4 && current.position.add(current.direction.turnRight()).isInBound(0, 0, maxX, maxY)) {
                neighbours.add(new state(current.position.add(current.direction.turnRight()), current.direction.turnRight(), 1));
            }

            if (!gScore.containsKey(current)) {
                gScore.put(current, 1000000000L);
            }
            for (state neighbor : neighbours) {
                // d(current,neighbor) is the weight of the edge from current to neighbor
                // tentative_gScore is the distance from start to the neighbor through current
                if (!gScore.containsKey(neighbor)) {
                    gScore.put(neighbor, 1000000000L);
                }

                long tentative_gScore = gScore.get(current) + heatLoss[getIndex(neighbor.position)];
                if (tentative_gScore < gScore.get(neighbor)) {
                    // This path to neighbor is better than any previous one. Record it!
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tentative_gScore);
                    fScore.put(neighbor, tentative_gScore + neighbor.position.taxicabDist(goal));
                    if (!openSet.contains(neighbor)) {
                        //   neighbor not in openSet
                        openSet.add(neighbor);
                    }
                }
            }
        }
        // Open set is empty but goal was never reached
        throw new RuntimeException("failed to find");

    }

    private long reconstruct_path(Map<state, state> cameFrom, state current) {
        System.out.println("current = " + current);
        long totalPath = heatLoss[getIndex(current.position)];
        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            System.out.println("current = " + current);
            totalPath += heatLoss[getIndex(current.position)];
        }
        return totalPath;
    }


    public void doThing(List<String> in) {
        readPuzzle(in);

        long heatloss = findHeatLossToEnd();

        System.out.println("heatloss = " + heatloss);

    }

    int getIndex(int x, int y) {
        return y * maxX + x;
    }

    int getIndex(Vector2 v) {
        return v.y * maxX + v.x;
    }

    int maxY;
    int maxX;

    private void readPuzzle(List<String> in) {
        maxX = in.get(0).length();
        maxY = in.size();
        goal = new Vector2(maxX - 1, maxY - 1);
        heatLoss = new int[maxX * maxY];

        for (int y = 0; y < maxY; y++) {
            for (int x = 0; x < maxX; x++) {
                heatLoss[getIndex(x, y)] = Integer.parseInt(String.valueOf(in.get(y).charAt(x)));
            }
        }
    }

}
