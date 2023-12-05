package day4;

import main.Day;

import java.util.*;

public class Day4 implements Day {


    public void doThing(List<String> in) {
//        int sum = 0;
        List<Integer> totalCards = new ArrayList<>();
        for (int i = 0; i < in.size() + 1; i++) {
            totalCards.add(0);
        }

        for (int i = 0; i < in.size(); i++) {
            int card = i + 1;
            totalCards.set(card, totalCards.get(card) + 1);
            String line = in.get(i);
            line = line.substring(line.indexOf(":") + 2);
            var split = line.split("\\|");
            Scanner sc = new Scanner(split[0]);
            Set<Integer> winningNumers = new HashSet<>();
            while (sc.hasNextInt()) {
                winningNumers.add(sc.nextInt());
            }

            sc = new Scanner(split[1]);
            int numberOfMatches = 0;
            while (sc.hasNextInt()) {
                int num = sc.nextInt();
                if (winningNumers.contains(num)) {
                    numberOfMatches++;
                }
            }

            System.out.println("card = " + card);
            System.out.println("numberOfMatches = " + numberOfMatches);
            int numberOfThisCard = totalCards.get(card);
            for (int forward = 1; forward <= numberOfMatches; forward++) {
                totalCards.set(card + forward, totalCards.get(card + forward) + numberOfThisCard);
            }
//            int points =0;
//            if(numberOfMatches > 0){
//                points = (int) Math.pow(2,numberOfMatches-1);
//            }
//            System.out.println("points = " + points);
//            sum += points;
        }

        System.out.println("totalCards = " + totalCards);
        int totalCardsGained = 0;
        for(Integer cards : totalCards){
            totalCardsGained += cards;
        }
        System.out.println("totalCardsGained = " + totalCardsGained);
//        System.out.println("sum = " + sum);
    }

}
