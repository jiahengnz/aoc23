package day6;

import main.Day;

import java.util.List;

public class Day6 implements Day {
    public record race(int time, int dist) {
        int marginOfError() {
            int held = 0;
            for (; held < time / 2 + 1; held++) {
                if (totalDistance(held, time) > dist) {
                    return (time - (held - 1) - (held));
                }
            }
            return -1;
        }
    }

    static long totalDistance(long held, long total) {
        return (total - held) * held;
    }

    public void doThing(List<String> in) {

        long t = 46689866;
        long d = 358105418071080L;

//        long t = 71530;
//        long d = 940200;

        // want (t-x)*x > d
        // -x*x + t*x -d > 0

        int a = -1;
        long b = t;
        long c = -d;

        long det = b * b - (4 * a * c);
        System.out.println("det = " + det);

        double root = Math.sqrt(det);
        System.out.println("root = " + root);

        double negd = (-b + root) / (2 * a);
        long neg = (long) Math.ceil(negd);
        double posd = (-b - root) / (2 * a);
        long pos = (long) Math.floor(posd);
        System.out.println("neg = " + neg);
        System.out.println("pos = " + pos);

        System.out.println("pos-neg+1 = " + (pos - neg+1));

    }


//        Scanner sc1 = new Scanner(in.get(0));
//        Scanner sc2 = new Scanner(in.get(1));
//
//        sc1.next();
//        sc2.next();
//
//       // List<race> races = new ArrayList<>();
//int product = 1;
//        while (sc1.hasNextInt()) {
//            int t = sc1.nextInt();
//            int d = sc2.nextInt();
//            int moe = new race(t,d).marginOfError();
//            System.out.println("moe = " + moe);
//            product *= moe;
//         //   races.add(new race(t, d));
//        }
//        System.out.println("product = " + product);


}
