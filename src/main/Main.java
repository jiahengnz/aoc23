package main;

public class Main {

    public static void main(String[] args) throws Exception {

        int day = 18;
        boolean testInput = true;
//        boolean testInput = false;

        Day d = (Day) Class.forName("day" + day + ".Day" + day).getDeclaredConstructor().newInstance();

        d.doThing(testInput ? Util.getTestInput(day) : Util.getInput(day));

    }
}
