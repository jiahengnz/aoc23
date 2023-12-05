package main;

public class Main {

    public static void main(String[] args) throws Exception {

        int day = 6;
        boolean testInput = true;
//        boolean testInput = false;

        Day d = (Day) Class.forName("day" + day + ".Day" + day).getDeclaredConstructor().newInstance();

        if (testInput) {
            d.doThing(Util.getTestInput(day));
        } else {
            d.doThing(Util.getInput(day));
        }
    }
}
