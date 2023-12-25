package day20;

import main.Day;

import java.util.*;

public class Day20 implements Day {
    long lowCount = 0;
    long highCount = 0;

    class Module {
        List<String> destinations;
        List<String> inputs;
        Set<String> highPulseReceives = new HashSet<>();
        boolean isOn = false;
        String name;
        boolean isFlipFlop;

        void sendAll(boolean high) {
            for (String dest : destinations) {
                queue.add(new Pulse(dest, high, name));
            }
        }

        void sendPulse(boolean highPulse, String sender) {
            if (name.equals("broadcaster")) {
                sendAll(highPulse);
                return;
            }
            if (isFlipFlop) {
                if (!highPulse) {
                    isOn = !isOn;
                    sendAll(isOn);
                }
                return;
            }

            // conjunction module
            boolean b = highPulse ? highPulseReceives.add(sender) : highPulseReceives.remove(sender);

            sendAll(highPulseReceives.size() != inputs.size());

        }
    }

    record Pulse(String dest, boolean isHigh, String sender) {

    }

    void printStatus() {
//        for (var mod : modules.values()) {
        var mod = modules.get("rs");

        if (!mod.highPulseReceives.isEmpty())
            System.out.print("\t" + mod.highPulseReceives);
        System.out.println();

//        }
    }

    Queue<Pulse> queue;
    Map<String, Module> modules;

    boolean hasCycled() {
        for (var mod : modules.values()) {
            if (mod.isOn || !mod.highPulseReceives.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    long buttonPushes = 0;

    void pushButton() {
//        if (queue != null && !queue.isEmpty()) throw new RuntimeException();
        buttonPushes++;
        queue = new ArrayDeque<>();
        queue.add(new Pulse("broadcaster", false, "button"));
        while (!queue.isEmpty()) {
            var pop = queue.poll();


            if (pop.dest.equals("rx") && !pop.isHigh) {
                System.out.println("buttonPushes = " + buttonPushes);
                System.exit(0);
            }


            if (pop.isHigh) highCount++;
            else lowCount++;
            if (modules.get(pop.dest) != null)
                modules.get(pop.dest).sendPulse(pop.isHigh, pop.sender);
        }
    }

    public void doThing(List<String> in) {
        readInputs(in);
        lowCount = 0;
        highCount = 0;
//        printDependencies();
        pushButton();
        while (true) {
//            System.out.print("i = " + buttonPushes);
//            printStatus();
////        while (!hasCycled()) {
//            if (buttonPushes == 8193) break;
            pushButton();
        }
//        System.out.println("buttonPushes = " + buttonPushes);
//        System.out.println("lowCount = " + lowCount);
//        System.out.println("highCount = " + highCount);
//
//        System.out.println("(lowCount+highCount)*needCycles = " + (lowCount * highCount));
    }

    private void printDepd(Module mod, int depth) {
        System.out.println("\t".repeat(depth) + mod.name + " " + mod.inputs);
        if (mod.inputs != null)
            for (String d : mod.inputs) {
                printDepd(modules.get(d), depth + 1);
            }
    }

    private void printDependencies() {
        var mod = modules.get("dn");
        printDepd(mod, 0);

//        for (var mod : modules.values()) {
//          if(!mod.isFlipFlop){
//              System.out.println(mod.name + "\t" + mod.inputs);
//          }
//        }

    }

    private void readInputs(List<String> in) {
        modules = new HashMap<>();
        for (String line : in) {
            String[] sp = line.split(" -> ");
            String[] outs = sp[1].split(", ");

            String name = sp[0];

            Module newm = new Module();
            newm.destinations = Arrays.stream(outs).toList();

            if ("broadcaster".equals(name)) {
                newm.name = name;
                modules.put(name, newm);
                continue;
            }
            newm.name = name.substring(1);
            newm.isFlipFlop = sp[0].charAt(0) == '%';
            modules.put(name.substring(1), newm);
        }
        for (var e : modules.entrySet()) {
            for (String dest : e.getValue().destinations) {
                if (modules.get(dest) == null) continue;
                if (!modules.get(dest).isFlipFlop) {
                    if (modules.get(dest).inputs == null) {
                        modules.get(dest).inputs = new ArrayList<>();
                    }
                    modules.get(dest).inputs.add(e.getKey());
                }
            }
        }
    }

}
