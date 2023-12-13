package day0;

import main.Day;

import java.util.List;

public class Day0 implements Day {

    int maxX;
    int maxY;

    void readInput(List<String> in) {

        maxY = in.size();
        maxX = in.get(0).length();
        for (int y = 0; y < maxY; y++) {
            String line = in.get(y);
            for (int x = 0; x < maxX; x++) {

            }
        }
    }

    public void doThing(List<String> in) {
        readInput(in);
    }

}
