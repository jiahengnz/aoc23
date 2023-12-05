package day1;

import main.Day;

import java.util.List;

public class Day1 implements Day {

    String[] nums = new String[]{"zero",
            "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"
    };

    public String replaceNumbers(String input) {

        while (true) {
            int foundIndex = input.length();
            int foundNumber = 0;
            for (int i = 1; i <= 9; i++) {
                int index = input.indexOf(nums[i]);
                if (index <= -1 || index >= foundIndex) {
                    continue;
                }
                foundNumber = i;
                foundIndex = index;
            }
            if (foundNumber == 0) return input;
            var charArray = input.toCharArray();
            charArray[foundIndex] = (char) (foundNumber + '0');
            input = String.valueOf(charArray);
        }
    }


    public void doThing(List<String> in) {
        int sum = 0;
        for (String line : in) {

            System.out.println();
            System.out.println("\nline before = " + line);
            line = replaceNumbers(line);
            System.out.println("line after = " + line);

            char[] chars = line.toCharArray();
            StringBuilder value = new StringBuilder();
            for (int i = 0; i < chars.length; i++) {
                if (chars[i] >= '0' && chars[i] <= '9') {
                    System.out.println("" + chars[i]);
                    value.append(chars[i]);
                    break;
                }
            }
            for (int i = chars.length - 1; i >= 0; i--) {
                if (chars[i] >= '0' && chars[i] <= '9') {
                    value.append(chars[i]);
                    break;
                }
            }
            int val = Integer.parseInt(value.toString());
            System.out.println("val = " + val);
            sum += val;
        }
        System.out.println("sum = " + sum);
    }

}
