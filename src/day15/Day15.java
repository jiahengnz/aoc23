package day15;

import main.Day;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Day15 implements Day {
    record lens(String label, int focalLength) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            lens lens = (lens) o;
            return Objects.equals(label, lens.label);
        }

        @Override
        public String toString() {
            return  label + " " + focalLength ;
        }

        @Override
        public int hashCode() {
            return Objects.hash(label);
        }
    }

    List<List<lens>> boxes = new ArrayList<>();

    long focusingPower() {
        long sum = 0;

        for (int box = 0; box < 256; box++) {
            for (int lens = 0; lens < boxes.get(box).size(); lens++) {
                long prod = box + 1;
                prod *= lens + 1;
                prod *= boxes.get(box).get(lens).focalLength;
                sum += prod;
            }
        }
        return sum;
    }

    void printBoxes() {
        for (int box = 0; box < 256; box++) {
            if (!boxes.get(box).isEmpty()) {
                System.out.println("Box " + box + ": " + boxes.get(box));
            }
        }
    }

    void doLens(String input) {
        if (input.charAt(input.length() - 1) == '-') {
            String label = input.substring(0, input.length() - 1);
//            System.out.println("label = " + label);
            int hash = hash(label);
            boxes.get(hash).remove(new lens(label, 0));
        } else {
            String[] spl = input.split("=");
            String label = spl[0];
            lens len = new lens(label, Integer.parseInt(spl[1]));
            int hash = hash(label);
            int index = boxes.get(hash).indexOf(len);
            if(label.equals("ot")){
                new Object();
            }
            if (index > -1) {
                boxes.get(hash).set(index, len);
            } else {
                boxes.get(hash).add(len);
            }
        }
    }

    int hash(String input) {
        int value = 0;
        for (char c : input.toCharArray()) {
            value += c;
            value *= 17;
            value = value % 256;

        }
        return value;
    }

    public void doThing(List<String> in) {
        for (int i = 0; i < 256; i++) {
            boxes.add(new ArrayList<>());
        }

        long sum = 0;

        String[] parts = in.get(0).split(",");
        for (String line : parts) {
//            sum += hash(line);
            System.out.println("After " + line);
            doLens(line);
            printBoxes();
            System.out.println();
        }

        sum = focusingPower();
        System.out.println("sum = " + sum);

    }

}
