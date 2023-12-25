package day19;

import main.Day;

import java.util.*;

public class Day19 implements Day {

    List<part> parts = new ArrayList<>();

    record part(int x, int m, int a, int s) {
        int get(char type) {
            return switch (type) {
                case 'x' -> this.x;
                case 'm' -> this.m;
                case 'a' -> this.a;
                case 's' -> this.s;
                default -> throw new IllegalStateException("Unexpected value: " + type);
            };
        }

        int sum() {
            return x + m + a + s;
        }
    }

    Map<String, String> workflows = new HashMap<>();

    boolean applyRule(part p, String rule) {
        System.out.print("{" + rule + "}");

        String[] rules = rule.split(",");
        String nextNode = null;
        for (int i = 0; i < rules.length - 1; i++) {
            String r = rules[i];
            String[] rs = r.split(":");
            boolean isLessThan = rs[0].charAt(1) == '<';
            int value = Integer.parseInt(rs[0].substring(2));
            int pValue = p.get(rs[0].charAt(0));
//            if(value == pValue) throw new RuntimeException();
            if (value == pValue || isLessThan != pValue < value) {
                continue;
            }

            nextNode = rs[1];
            break;

        }
        if (nextNode == null) nextNode = rules[rules.length - 1];


        System.out.print(" ->\t" + nextNode);
        if ("A".equals(nextNode)) {
            return true;
        }
        if ("R".equals(nextNode)) {
            return false;
        }
        return applyRule(p, workflows.get(nextNode));
    }


    public void doThing(List<String> in) {
        readInput(in);

//        long sum = 0;
//        for (part p : parts) {
//            System.out.print(p.sum() + "\t" + p + "   \t");
//
//            if (applyRule(p, workflows.get("in"))) {
//                sum += p.sum();
//            }
//            System.out.println();
//        }
//        System.out.println("sum = " + sum);

        long count = calcCombinations(new Ranges(1, 4000, 1, 4000, 1, 4000, 1, 4000), workflows.get("in"));
        System.out.println("count = " + count);
    }

    record Ranges(int minX, int maxX, int minM, int maxM, int minA, int maxA, int minS, int maxS) {
        long getCombini() {
            long prod = maxX - minX + 1;
            prod *= maxM - minM + 1;
            prod *= maxA - minA + 1;
            prod *= maxS - minS + 1;
            return prod;
        }

        Ranges[] elim(char type, boolean isLessThan, int value) {
            Ranges[] r = new Ranges[2];
            if (type == 'x') {
                if (isLessThan) {
                    r[0] = new Ranges(minX, value - 1, minM, maxM, minA, maxA, minS, maxS);
                    r[1] = new Ranges(value, maxX, minM, maxM, minA, maxA, minS, maxS);
                    return r;
                } else {
                    r[0] = new Ranges(value + 1, maxX, minM, maxM, minA, maxA, minS, maxS);
                    r[1] = new Ranges(minX, value, minM, maxM, minA, maxA, minS, maxS);
                    return r;
                }
            } else if (type == 'm') {
                if (isLessThan) {
                    r[0] = new Ranges(minX, maxX, minM, value - 1, minA, maxA, minS, maxS);
                    r[1] = new Ranges(minX, maxX, value, maxM, minA, maxA, minS, maxS);
                    return r;
                } else {
                    r[0] = new Ranges(minX, maxX, value + 1, maxM, minA, maxA, minS, maxS);
                    r[1] = new Ranges(minX, maxX, minM, value, minA, maxA, minS, maxS);
                    return r;
                }
            } else if (type == 'a') {
                if (isLessThan) {
                    r[0] = new Ranges(minX, maxX, minM, maxM, minA, value - 1, minS, maxS);
                    r[1] = new Ranges(minX, maxX, minM, maxM, value, maxA, minS, maxS);
                    return r;
                } else {
                    r[0] = new Ranges(minX, maxX, minM, maxM, value + 1, maxA, minS, maxS);
                    r[1] = new Ranges(minX, maxX, minM, maxM, minA, value, minS, maxS);
                    return r;
                }
            } else if (type == 's') {
                if (isLessThan) {
                    r[0] = new Ranges(minX, maxX, minM, maxM, minA, maxA, minS, value - 1);
                    r[1] = new Ranges(minX, maxX, minM, maxM, minA, maxA, value, maxS);
                    return r;
                } else {
                    r[0] = new Ranges(minX, maxX, minM, maxM, minA, maxA, value + 1, maxS);
                    r[1] = new Ranges(minX, maxX, minM, maxM, minA, maxA, minS, value);
                    return r;
                }
            }
            throw new IllegalStateException("Unexpected value: " + type);

        }
    }

    private long calcCombinations(Ranges range, String rule) {

        long sum = 0;
        String[] rules = rule.split(",");

        Ranges remainingRange = range;

        for (int i = 0; i < rules.length - 1; i++) {
            String r = rules[i];
            String[] rs = r.split(":");
            boolean isLessThan = rs[0].charAt(1) == '<';
            int value = Integer.parseInt(rs[0].substring(2));
            char type = rs[0].charAt(0);


            var split = remainingRange.elim(type, isLessThan, value);
            Ranges accepted = split[0];
            remainingRange = split[1];

            if ("A".equals(rs[1]))
                sum += accepted.getCombini();
            else if ("R".equals(rs[1])) {
            } else {
                sum += calcCombinations(accepted, workflows.get(rs[1]));
            }
            new Object();
        }

        String nextNode = rules[rules.length - 1];
        if ("A".equals(nextNode)) {
            sum += remainingRange.getCombini();
        } else if ("R".equals(nextNode)) {
        } else {
            sum += calcCombinations(remainingRange, workflows.get(nextNode));
        }
        return sum;
    }

    private void readInput(List<String> in) {
        int i = 0;
        for (; i < in.size(); i++) {
            String line = in.get(i);
            if (line.isEmpty()) {
                break;
            }

            String wfName = line.substring(0, line.indexOf("{"));
            String rule = line.substring(line.indexOf("{") + 1, line.length() - 1);

            workflows.put(wfName, rule);
        }
        i++;
        for (; i < in.size(); i++) {
            String line = in.get(i);
            line = line.replaceAll("[{=,xmas}]", " ");
            Scanner sc = new Scanner(line);
            parts.add(new part(sc.nextInt(), sc.nextInt(), sc.nextInt(), sc.nextInt()));
        }
    }

}
