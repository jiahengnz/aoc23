package day5;

import main.Day;

import java.util.*;

public class Day5 implements Day {

    List<Mapping> seedToSoil = new ArrayList<>();

    long getDest(long src, List<Mapping> map) {
        for (Mapping mapping : map) {
            if (mapping.isInRange(src)) {
                return src + mapping.offsetToDestination;
            }
        }
        return src;
    }

    List<Mapping> soilToFert = new ArrayList<>();
    List<Mapping> fertToWater = new ArrayList<>();
    List<Mapping> waterToLight = new ArrayList<>();
    List<Mapping> lightToTemp = new ArrayList<>();
    List<Mapping> tempToHumid = new ArrayList<>();
    List<Mapping> humidToLoc = new ArrayList<>();
    List<Long> seedsToBePlanted = new ArrayList<>();
    List<Long> seedRanges = new ArrayList<>();

    public record Mapping(long sourceStart, long range, long offsetToDestination) {
        public boolean isInRange(long value) {
            return value >= sourceStart && value < sourceStart + range;
        }
    }

    public void doThing(List<String> in) {
        List<Mapping> currentList = null;

        Scanner sc = new Scanner(in.get(0));
        sc.next();
        while (sc.hasNextLong()) {
            seedsToBePlanted.add(sc.nextLong());
            seedRanges.add(sc.nextLong());
        }

        for (int i = 2; i < in.size(); i++) {
            String line = in.get(i);
            if (line.isEmpty()) {
                currentList = null;
                continue;
            }
            if (line.startsWith("seed")) {
                currentList = seedToSoil;
                continue;
            }
            if (line.startsWith("soil")) {
                currentList = soilToFert;
                continue;
            }

            if (line.startsWith("fert")) {
                currentList = fertToWater;
                continue;
            }
            if (line.startsWith("water")) {
                currentList = waterToLight;
                continue;
            }
            if (line.startsWith("light")) {
                currentList = lightToTemp;
                continue;
            }
            if (line.startsWith("temp")) {
                currentList = tempToHumid;
                continue;
            }
            if (line.startsWith("humid")) {
                currentList = humidToLoc;
                continue;
            }

            sc = new Scanner(line);
            long destStart = sc.nextLong();
            long srcStart = sc.nextLong();
            long range = sc.nextLong();


            currentList.add(new Mapping(srcStart, range, destStart - srcStart));
        }

        long minLocation = Long.MAX_VALUE;

        for (int j = 0; j < seedsToBePlanted.size(); j++) {
            long seed = seedsToBePlanted.get(j);
            System.out.println("seed = " + seed);
            for (long i = seed; i < seed + seedRanges.get(j); i++) {
                long soil = getDest(i, seedToSoil);
                long fert = getDest(soil, soilToFert);
                long water = getDest(fert, fertToWater);
                long light = getDest(water, waterToLight);
                long temp = getDest(light, lightToTemp);
                long humid = getDest(temp, tempToHumid);
                long loc = getDest(humid, humidToLoc);
                if (loc < minLocation) minLocation = loc;
            }
        }
        System.out.println("minLocation = " + minLocation);

    }

}
