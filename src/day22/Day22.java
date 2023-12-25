package day22;

import main.Day;

import java.util.*;

public class Day22 implements Day {


    List<Block> stack;

    Block[] highestStack;


    public void doThing(List<String> in) {
        readInput(in);
        buildStack();
//        countCanErase();
        countChain();
    }


    private int countBlock(Block b) {
        Set<Block> hasFallen = new HashSet<>();
        hasFallen.add(b);
        Queue<Block> needToCheckAgain = new ArrayDeque<>();
        needToCheckAgain.addAll(b.depsBy);

        while (!needToCheckAgain.isEmpty()) {
            Block currCheck = needToCheckAgain.poll();
            Set<Block> allDeps = new HashSet<>();
            allDeps.addAll(currCheck.deps);
            allDeps.removeAll(hasFallen);
            allDeps.remove(b);
            if (allDeps.isEmpty()) {

                hasFallen.add(currCheck);
                needToCheckAgain.addAll(currCheck.depsBy);
            }
        }
//        System.out.println("b = " + b);
//        System.out.println("sum = " + hasFallen.size());
        return hasFallen.size()-1;
    }


    private void countChain() {

        stack.sort((o1, o2) -> o2.startZ - o1.startZ);
        long sum = 0;
        for (Block b : stack) {
            sum += countBlock(b);
        }
        System.out.println("sum = " + sum);
    }

    private void countCanErase() {
        Set<Block> canErase = new HashSet<>();
        canErase.addAll(stack);
        for (Block b : stack) {
            if (b.deps.size() == 1) {
                canErase.remove(b.deps.iterator().next());
            }
        }
        System.out.println("canErase.size() = " + canErase.size());
    }

    private void buildStack() {
        highestStack = new Block[100];
        for (Block b : stack) {
            int dir = b.direction();
            int lowestZ = 0;
            Set<Block> deps = new HashSet<>();

            for (int y = b.startY; y <= b.endY; y++) {
                for (int x = b.startX; x <= b.endX; x++) {
                    Block there = highestStack[y * 10 + x];
                    if (there == null) continue;
                    int maxZThere = there.endZ;
                    if (maxZThere == lowestZ) {
                        deps.add(there);
                    } else if (maxZThere > lowestZ) {
                        lowestZ = maxZThere;
                        deps.clear();
                        deps.add(there);
                    }
                }
            }
            b.deps.addAll(deps);
            for (Block dep : deps) {
                dep.depsBy.add(b);
            }
            b.fallTo(lowestZ);

            for (int y = b.startY; y <= b.endY; y++) {
                for (int x = b.startX; x <= b.endX; x++) {
                    highestStack[y * 10 + x] = b;
                }
            }

        }
    }

    class Block {
        public int startX, startY, startZ, endX, endY, endZ;
        Set<Block> deps;
        Set<Block> depsBy;
        public char name;

        public Block(int startX, int startY, int startZ, int endX, int endY, int endZ) {
            this.startX = startX;
            this.startY = startY;
            this.startZ = startZ;
            this.endX = endX;
            this.endY = endY;
            this.endZ = endZ;
            deps = new HashSet<>();
            depsBy = new HashSet<>();
        }


        void fallTo(int zCount) {
            int fallBy = startZ - (zCount + 1);
            startZ -= fallBy;
            endZ -= fallBy;
        }

        int direction() {
            if (startX != endX) return 0;
            if (startY != endY) return 1;
            if (startZ != endZ) return 2;
            return 3;
        }

        @Override
        public String toString() {
            return "" + name + (deps.isEmpty() ? "" : deps);
//            return "Block{" +
//                    "name=" + name +
//                    ", deps=" + deps +
//                    ", startX=" + startX +
//                    ", startY=" + startY +
//                    ", startZ=" + startZ +
//                    ", endX=" + endX +
//                    ", endY=" + endY +
//                    ", endZ=" + endZ +
//                    '}';
        }
    }

    private void readInput(List<String> in) {
        stack = new ArrayList<>();
        char name = 'A';
        for (String input : in) {
            String inTrim = input.replaceAll("[,~]", " ");
            Scanner sc = new Scanner(inTrim);
            int x1 = sc.nextInt();
            int y1 = sc.nextInt();
            int z1 = sc.nextInt();
            int x2 = sc.nextInt();
            int y2 = sc.nextInt();
            int z2 = sc.nextInt();
            Block b = new Block(
                    Math.min(x1, x2), Math.min(y1, y2), Math.min(z1, z2),
                    Math.max(x1, x2), Math.max(y1, y2), Math.max(z1, z2)
            );
            b.name = name++;
            stack.add(b);
        }
        stack.sort(Comparator.comparingInt(o -> o.startZ));
    }

}
