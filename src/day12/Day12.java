package day12;

import main.Day;

import java.util.ArrayList;
import java.util.List;

import static day12.puzzle.op;

public class Day12 implements Day {


    static puzzle getPuzzle(String in) {

        List<Integer> numbers = new ArrayList<>();

        String[] parse = in.split(" ");
        List<Character> states = new ArrayList<>();
        for (char c : parse[0].toCharArray()) {
            if (c == op) {
                if (!states.isEmpty() && states.get(states.size() - 1) == op) continue;
            }
            states.add(c);
        }
//        if (states.get(states.size() - 1) == op) {
//            states.remove(states.size() - 1);
//        }

        String[] ints = parse[1].split(",");
        for (String s : ints) {
            numbers.add(Integer.parseInt(s));
        }

        StringBuilder sb = new StringBuilder();
        for (Character ch : states) {
            sb.append(ch);
        }
        return new puzzle(sb.toString(), numbers);
    }


    public void doThing(List<String> in) {
        long sum = 0;
        for (String input : in) {
            puzzle p = getPuzzle(input);
            var a = p.getArrangements(p.states, p.numbers, 0,-1,0);
//            System.out.println("a = " + a);
            sum += a;
//            break;
        }
        System.out.println("sum = " + sum);
    }

}
