package day7;

import main.Day;

import java.util.*;

public class Day7 implements Day {

    Map<String, Integer> bids = new HashMap<>();
    public static List<Character> cards = List.of('J', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'Q', 'K', 'A');
    public final static int jIndex = 0;

    static int getType(String hand) {

        int[] cardsOfCard = new int[cards.size()];
        int max = 0;
        int maxCardIndex = -1;
        char[] charArray = hand.toCharArray();
        for (char c : charArray) {
            int indexOfCard = cards.indexOf(c);
//            System.out.println("c = " + c);
            int total = ++cardsOfCard[indexOfCard];
            if (total >= max && indexOfCard != jIndex) {
                maxCardIndex = indexOfCard;

                max = total;
            }
        }

        max += cardsOfCard[jIndex];

        if (max == 5) return 7;
        if (max == 4) return 6;
        if (max == 3) {
            for (int i = 0; i < cardsOfCard.length; i++) {
                if (i == jIndex) continue;
                if (i == maxCardIndex) continue;
                int count = cardsOfCard[i];
                if (count == 2) return 5; // full house
                if (count == 1) return 4; // three of a kind
            }
        }
        if (max == 2) {
            boolean oneFound = false;
            for (int i = 0; i < cardsOfCard.length; i++) {
                if (i == jIndex) continue;
                if (i == maxCardIndex) continue;
                int count = cardsOfCard[i];
                if (count == 2) return 3; // two pair
                if (count == 1) {
                    if (!oneFound) {
                        oneFound = true;
                        continue;
                    }
                    return 2; // one pair
                }
            }
        }
        return 1;
    }

    public void doThing(List<String> in) {
        for (String line : in) {
            Scanner sc = new Scanner(line);
            String hand = sc.next();
            int bid = sc.nextInt();
            bids.put(hand, bid);
        }

        List<String> keys = bids.keySet().stream().toList();
        ArrayList<String> hands = new ArrayList<>(keys);
        hands.sort((o1, o2) -> {
            int o1t = getType(o1);
            int o2t = getType(o2);
            if (o1t < o2t) return -1;
            if (o1t > o2t) return 1;

            for (int i = 0; i < o1.length(); i++) {
                int o1i = cards.indexOf(o1.charAt(i));
                int o2i = cards.indexOf(o2.charAt(i));
                if (o1i == o2i) continue;
                return o1i - o2i;
            }
            throw new RuntimeException("equal");
        });

        System.out.println("hands = " + hands);
        int sum = 0;
        for (int i = 0; i < hands.size(); i++) {
            sum += (i + 1) * bids.get(hands.get(i));
        }
        System.out.println("sum = " + sum);

    }

}
