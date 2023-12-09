package day8;

import main.Day;

import java.util.*;

public class Day8 implements Day {

    record tuple(String left, String right) {
    }

    Map<String, tuple> map = new HashMap<>();

    public void doThing(List<String> in) {

        String route = in.get(0);
        int routeLength = route.length(); // 281 !! the common prime factor of the 6 numbers!!

        for (int i = 2; i < in.size(); i++) {
            String line = in.get(i).replace("=", "").replace("(", "").replace(")", "").replace(",", "");
            Scanner sc = new Scanner(line);
            String key = sc.next();
            String left = sc.next();
            String right = sc.next();
            map.put(key, new tuple(left, right));

        }


        Set<String> starts = new HashSet<>();
        for (String s : map.keySet()) {
            if (s.endsWith("A")) {
                starts.add(s);
            }
        }
        System.out.println("starts = " + starts);

        char[] directions = route.toCharArray();
        for (String start : starts) {
            String curr = start;
            int lastStep = 0;
            int foundOne = 0;
            int steps = 0;
            outer:
            while (true) {
                for (char c : directions) {
//                Set<String> next = new HashSet<>();
                    boolean goLeft = c == 'L';
//                boolean isAllZ = true;
                    String currNode = curr;
//                for (String currNode : curr) {
                    tuple t = map.get(currNode);
                    curr = goLeft ? t.left : t.right;


//                    next.add(nextStep);
//                }
                    steps++;

                    if (curr.endsWith("Z")) {
                        if (0 == foundOne) {
                            System.out.print("" + steps);
                            lastStep = steps;
                            foundOne = 1;
                        } else {
                            System.out.print(" + n*" + (steps - lastStep));
//                            if(foundOne == 3) {
                            System.out.println();
                            break outer;
//                            }
//                            lastStep = steps;
//                            foundOne++;

                        }
                    }
//                System.out.println("steps = " + steps);
//                if (isAllZ) {
//                    break outer;
//                }
//                curr = next;
//                System.out.println("curr = " + curr);
                }
            }
        }
//        System.out.println("steps = " + steps);

    }

}
