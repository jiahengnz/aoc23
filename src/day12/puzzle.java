package day12;

import java.util.*;

public class puzzle {
    static final char dmg = '#';
    static final char op = '.';
    static final char unknown = '?';

    String states;
    List<Integer> numbers;

    puzzle(String states, List<Integer> numbers) {

        StringBuilder tempState = new StringBuilder();
        List<Integer> tempnumbers = new ArrayList<>();
        for (int i = 5; i > 0; i--) {
            tempState.append(states);
            if (i != 1) tempState.append(unknown);
            tempnumbers.addAll(numbers);
        }
//        System.out.println("tempnumbers = " + tempnumbers);
        this.states = tempState.toString();
//        System.out.println("this.states = " + this.states);
        this.numbers = tempnumbers;
    }

    record PuzzleState(String states, List<Integer> numbers, int currentCount, int currentExpected) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PuzzleState that = (PuzzleState) o;
            return currentCount == that.currentCount && currentExpected == that.currentExpected && Objects.equals(states, that.states) && Objects.equals(numbers, that.numbers);
        }

        @Override
        public int hashCode() {
            return Objects.hash(states, numbers, currentCount, currentExpected);
        }
    }

    Map<PuzzleState, Long> cache = new HashMap<>();

    long getArrangements(String states, List<Integer> numbers, int currentCount, int currentExpected, int depth) {
        PuzzleState ps = new PuzzleState(states, numbers, currentCount, currentExpected);
        if (cache.containsKey(ps)) {
            return cache.get(ps);
        }
        //    System.out.println(" ".repeat(depth) + " states = " + states + " " + numbers + " " + currentCount + " " + currentExpected);

        if (states.isEmpty()) {
            if (!numbers.isEmpty()) return 0;
            if (currentExpected == -1 || currentCount == currentExpected) { // only if no chain or end chain
                return 1;
            }
            return 0;
        }

        // at this point we have string left

        long sum = 0;

        if (states.charAt(0) == op || states.charAt(0) == unknown) {
            if (currentExpected == -1 || currentCount == currentExpected) { // only if no chain or end chain
                sum += getArrangements(states.substring(1), numbers, 0, -1, depth + 1);
            } else if (states.charAt(0) == op) {
                return 0;  // we are in the middle of a chain yet op, invalid route
            }
        }

        if (states.charAt(0) == dmg || states.charAt(0) == unknown) {
            if (currentExpected == -1) {
                if (!numbers.isEmpty()) { // new chain
                    List<Integer> goingDown = new ArrayList<>(numbers);
                    int newCE = goingDown.remove(0);
                    sum += getArrangements(states.substring(1), goingDown, 1, newCE, depth + 1);
//                    System.out.println("sum = " + sum);
                }
            } else if (currentCount < currentExpected) { // continue chain
                sum += getArrangements(states.substring(1), numbers, currentCount + 1, currentExpected, depth + 1);
//                System.out.println("sum = " + sum);
            } else {
                // currentCount >= currentExpected, and we have another dmg, invalid route
                // return 0;
            }
        }

        cache.put(ps, sum);
        return sum;
    }


}
