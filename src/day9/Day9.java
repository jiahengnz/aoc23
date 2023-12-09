package day9;

import main.Day;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day9 implements Day {

    List<Integer> getDiffs(List<Integer> input) {
        List<Integer> ret = new ArrayList<>();
        for (int i = 1; i < input.size(); i++) {
            ret.add(input.get(i) - input.get(i - 1));
        }
        return ret;
    }

    boolean isAllZero(List<Integer> list) {
        for (int i : list) {
            if (i != 0) return false;
        }
        return true;
    }

    int getExtrapolate(List<Integer> input) {
        if (isAllZero(input)) return 0;
        int diff = getExtrapolate(getDiffs(input));
//        return input.get(input.size() - 1) + diff;
        return input.get(0) - diff;
    }

    List<Integer> getInput(String in) {
        Scanner sc = new Scanner(in);
        List<Integer> a = new ArrayList<>();
        while (sc.hasNextInt()) {
            a.add(sc.nextInt());
        }
        return a;
    }

    public void doThing(List<String> in) {
        int sum = 0;
        for (String line : in) {
            List<Integer> input = getInput(line);
            sum += getExtrapolate(input);
        }
        System.out.println("sum = " + sum);
    }

}
