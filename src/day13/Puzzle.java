package day13;

import java.util.ArrayList;
import java.util.List;

public class Puzzle {

    List<String> inputs = new ArrayList<>();

    List<Long> rows = new ArrayList<>();
    List<Long> cols = new ArrayList<>();

    public void addLine(String line) {
        inputs.add(line);
    }

    public long calculate() {
        processRows();
        processCols();

        int col = findMirror(cols);
        int row = findMirror(rows);

        long index = 0;
        if (col >= 0) index += col;
        if (row >= 0) index += 100L * (long) row;
        if (index <= 0) throw new RuntimeException();

        return index;
    }

    static boolean oneBitEquals(long a, long b) {
        if (a == b) throw new RuntimeException();
        long diff = a ^ b;
        return (diff & (diff - 1)) == 0;
    }

    static int findMirror(List<Long> in) {
        outer:
        for (int i = 0; i < in.size() - 1; i++) {
            int a = i;
            int b = i + 1;
            if (!in.get(a).equals(in.get(b))) {
                if (!oneBitEquals(in.get(a), in.get(b))) {
                    continue;
                }
            }
            boolean smudgeFound = false;
            // we found a match at i and i+1

            while (a >= 0 && b < in.size()) {
                if (!in.get(a).equals(in.get(b))) {
                    if (smudgeFound || !oneBitEquals(in.get(a), in.get(b))) {
                        continue outer;
                    }
                    smudgeFound = true;
                }
                a--;
                b++;
            }
            if (!smudgeFound) continue;

//            System.out.println("in = " + in);
            // its a match
            return i + 1;
        }
        return -1;
    }


    private void processRows() {
        for (String in : inputs) {
            String rep = in.replace(".", "0").replace("#", "1");
            rows.add(Long.valueOf(rep, 2));
        }
    }

    private void processCols() {
        int maxX = inputs.get(0).length();
        for (int x = 0; x < maxX; x++) {
            StringBuilder a = new StringBuilder();
            for (String input : inputs) {
                a.append(((input.charAt(x) == '#') ? '1' : '0'));
            }
            cols.add(Long.valueOf(a.toString(), 2));
        }
    }
}