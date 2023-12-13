package day13;

import main.Day;

import java.util.List;

public class Day13 implements Day {

    public void doThing(List<String> in) {

        Puzzle p = new Puzzle();

        long sum = 0;
        for (String line : in) {
            if (line.isEmpty()) {
                var a =  p.calculate();
//                System.out.println("" + a);
                sum += a;
                p = new Puzzle();
                continue;
            }
            p.addLine(line);
        }
        var a =  p.calculate();
//        System.out.println("" + a);
        sum += a;


        System.out.println("sum = " + sum);

    }

}
