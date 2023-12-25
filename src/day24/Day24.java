package day24;

import main.Day;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day24 implements Day {


    record Hailstone(long px, long py, long pz, long vx, long vy, long vz) {
        boolean isParallel(Hailstone other) {
            return vy * other.vx == vx * other.vy();
        }

        static boolean equal(double a, double b) {
            return Math.abs((a - b) / a) < 0.0001f;
        }

        boolean intersectWithinBounds(Hailstone other, long lowerBound, long upperBound) {
            System.out.println("Hailstone A: " + this);
            System.out.println("Hailstone B: " + other);
            if (isParallel(other)) {
                System.out.println("Hailstones' paths are parallel; they never intersect.");
                return false;
            }
            double t = (double) (py * other.vx - other.py * other.vx + other.px * other.vy - px * other.vy) / (vx * other.vy - vy * other.vx);
            double s = (py + t * vy - other.py) / other.vy;

            if (t < 0 && s < 0) {
                System.out.println("Hailstones' paths crossed in the past for both hailstones.");
                return false;
            }
            if (t < 0) {
                System.out.println("Hailstones' paths crossed in the past for hailstone A.");
                return false;
            }
            if (s < 0) {
                System.out.println("Hailstones' paths crossed in the past for hailstone B.");
                return false;
            }

            if (!equal(px + t * vx, other.px + s * other.vx))
                throw new RuntimeException((px + t * vx) + " " + (other.px + s * other.vx));
            if (!equal(py + t * vy, other.py + s * other.vy))
                throw new RuntimeException(py + t * vy + " " + (other.py + s * other.vy));

            // d = (py' - py) / (vy - vy')

            double y1 = py + t * vy;
            double x1 = px + t * vx;
            if (y1 > upperBound || y1 < lowerBound || x1 > upperBound || x1 < lowerBound) {
                System.out.printf("Hailstones' paths will cross outside the test area (at px=%.3f, py=%.3f).\n", x1, y1);
                return false;
            }

            System.out.printf("Hailstones' paths will cross inside the test area (at px=%.3f, py=%.3f).\n", x1, y1);
            return true;
        }

        @Override
        public String toString() {
            return String.format("%d, %d, %d @ %d, %d, %d", px, py, pz, vx, vy, vz);
        }
    }

    List<Hailstone> hailstones;

    public void doThing(List<String> in) {
        readInput(in);
        // check2Dintersections();
        printEquations();
        System.out.println("(a+b+c) = " + (a + b + c));
    }

    long a = 324764920956014L;
    long b = 100697955736353L;
    long x = -97;
    long y = 311;
    long c = 270369299931782L;
    long z = 11;

    /**
     * px + t * vx = a + t * x
     * py + t * vy = b + t * y
     * pz + t * vz = c + t * z
     *
     *
     * t = (a - px)/(vx - x) = (b - py)/(vy - y)
     * t = (a - px)/(vx - x) = (c - pz)/(vz - z)
     * t = (b - py)/(vy - y) = (c - pz)/(vz - z)
     *
     * (a-px)(vy-y) = (b-py)(vx-x)
     *
     * (324764920956014-px)(vz-z) = (c-pz)(vx+97)
     * (100697955736353-py)(vz-z) = (c-pz)(vy-311)
     *
     *
     * (324764920956014-212744973399902)(-57-z) = (c-323267608499946)(47+97)
     * (100697955736353-318514520428793)(-57-z) = (c-323267608499946)(31-311)
     *
     *
     * (a-19)(1-y) = (b-13)(-2-x), (a-18)(-1-y) = (b-19)(-1-x), (a-20)(-2-y) = (b-25)(-2-x)
     *
     * (a-12)(-2-y) == (b-31)(-1-x)
     *
     * Solve[(a-414821420890930)(-234-y)=(b-384401958421493)(-270-x)&&(a-299988158973189)(204-y)=(b-206742497022844)(-72-x)&&(a-291572026635042)(145-y)=(b-204660605873737)(-44-x)&&(a-296930849713999)(-5-y)=(b-287837668767773)(-50-x),{a,b}]
     *
     * Solve[{(a-19)(1-y) == (b-13)(-2-x) && (a-19)(-2-z) == (c-30)(-2-x) && (b-13)(-2-z) == (c-30)(1-y) && (a-18)(-1-y) == (b-19)(-1-x) && (a-18)(-2-z) == (c-22)(-1-x) && (b-19)(-2-z) == (c-22)(-1-y) && (a-20)(-2-y) == (b-25)(-2-x) && (a-20)(-4-z) == (c-34)(-2-x) && (b-25)(-4-z) == (c-34)(-2-y)},{a,b,c,x,y,z}]
     *
     * x = -97 ∧ y = 311 ∧ b = 100697955736353 ∧ a = 324764920956014
     * z = 11 and c = 270369299931782
     */
    private void printEquations() {
        for (int i = 5; i < 7; i++) {
            Hailstone h = hailstones.get(i);
//            System.out.printf("(a-%d)(%d-y)=(b-%d)(%d-x)&&", h.px, h.vy, h.py, h.vx);
            System.out.printf("(%d)(%d-z)=(c-%d)(%d)\n", a-h.px, h.vz, h.pz, h.vx-x);
            System.out.printf("(%d)(%d-z)=(c-%d)(%d)\n", b-h.py, h.vz, h.pz, h.vy-y);
        }
        // fed into wolfram alpha
    }


    private void check2Dintersections() {
        long lowerBound = 7;
        long upperBound = 27;
        long count = 0;
        for (int a = 0; a < hailstones.size(); a++) {
            Hailstone ha = hailstones.get(a);
            for (int b = a + 1; b < hailstones.size(); b++) {
                Hailstone hb = hailstones.get(b);
                if (ha.intersectWithinBounds(hb, 200000000000000L, 400000000000000L)) count++;
                System.out.println();
            }
        }
        System.out.println(count);
    }


    private void readInput(List<String> in) {
        hailstones = new ArrayList<>();
        for (String line : in) {
            line = line.replaceAll("[,@]", "");
            Scanner sc = new Scanner(line);
            hailstones.add(new Hailstone(sc.nextLong(), sc.nextLong(), sc.nextLong()
                    , sc.nextLong(), sc.nextLong(), sc.nextLong()));
        }
    }

}
