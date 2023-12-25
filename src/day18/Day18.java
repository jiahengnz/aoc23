package day18;

import main.Day;
import main.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day18 implements Day {

    List<Vector2> vertices;

    public void doThing(List<String> in) {
        readInput(in);
        calcVertices();
        calcArea();
    }

    private void calcVertices() {
        Vector2 curr = Vector2.ZERO;
        vertices = new ArrayList<>();
        for (Vector2 dp : digPlans) {
            curr = curr.add(dp);
            vertices.add(curr);
        }
    }

    private void calcArea() {
        long sum = 0;
        for (int i = 0; i < vertices.size(); i++) {
            Vector2 in1 = vertices.get(i);
            Vector2 in2 = vertices.get((i + 1) % vertices.size());

            long x1 = in1.x;
            long x2 = in2.x;
            long y1 = in1.y;
            long y2 = in2.y;
            long a = (x1 * y2 - x2 * y1);
//            System.out.println("a = " + a);
            sum += a + in1.taxicabDist(in2);
//            System.out.println((i+1)+"sum = " + sum);
        }
        sum = sum / 2 + 1;
        System.out.println("sum = " + sum);
    }

    List<Vector2> digPlans = new ArrayList<>();

    private void readInput(List<String> in) {
        for (String line : in) {
            Scanner sc = new Scanner(line);
            Vector2 dir = null;

            if (line.charAt(0) == 'R') dir = Vector2.RIGHT;
            else if (line.charAt(0) == 'D') dir = Vector2.DOWN;
            else if (line.charAt(0) == 'U') dir = Vector2.UP;
            else if (line.charAt(0) == 'L') dir = Vector2.LEFT;

            sc.next();
            int length = sc.nextInt();

//            digPlans.add(dir.mult(length));


            String col = sc.next();

            col = col.replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("#", "");
            int hexNumDist = Integer.parseInt(col.substring(0, 5), 16);

            if (col.charAt(5) == '0') dir = Vector2.RIGHT;
            else if (col.charAt(5) == '1') dir = Vector2.DOWN;
            else if (col.charAt(5) == '3') dir = Vector2.UP;
            else if (col.charAt(5) == '2') dir = Vector2.LEFT;


            digPlans.add(dir.mult(hexNumDist));
        }
    }

}
