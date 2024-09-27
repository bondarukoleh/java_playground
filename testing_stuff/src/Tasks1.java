package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Tasks1 {
    public static void main(String[] args) {
//        System.out.println(mergeAlternately("asdasd", "qqq"));
//        System.out.println(gcdOfStrings("ABABAB", "ABAB"));
        System.out.println(Arrays.toString(kidsWithCandies(new int[]{12,1,12}, 3).toArray()));
    }


    public static String gcdOfStrings(String str1, String str2) {
        if (!(str1 + str2).equals(str2 + str1)) {
            return "";
        }

        int substringIndex = findDivider(str1.length(), str2.length());
        return str1.substring(0, substringIndex);
    }

    private static int findDivider(int a, int b) {
        if (b == 0) {
            return a;
        } else {
            return findDivider(b, a % b);
        }
    }

    public static String mergeAlternately(String word1, String word2) {
        var firstWordArr = word1.toCharArray();
        var secondWordArr = word2.toCharArray();
        var longestWordArr = firstWordArr.length > secondWordArr.length ? firstWordArr : secondWordArr;
        ArrayList<Character> resultArr = new ArrayList<>();
        for (int i = 0; i < longestWordArr.length; i++) {
            if (firstWordArr.length > i) {
                resultArr.add(firstWordArr[i]);
            }
            if (secondWordArr.length > i) {
                resultArr.add(secondWordArr[i]);
            }
        }
        return resultArr.stream().map(Object::toString)
                .collect(Collectors.joining(""));
    }

    public static List<Boolean> kidsWithCandies1(int[] candies, int extraCandies) {
        var candiesInt = Arrays.stream(candies).boxed().toArray(Integer[]::new);
        Arrays.sort(candiesInt, (a, b) -> b - a);
        var sortedCandies = Arrays.stream(candiesInt).mapToInt(Integer::intValue).toArray();
        var result = new ArrayList<Boolean>();

        IntStream.range(0, candies.length).forEach((i) -> {
            if (candies[i] + extraCandies >= sortedCandies[0]) {
                result.add(true);
            } else {
                result.add(false);
            }
        });

        return result;
    }

    public static List<Boolean> kidsWithCandies(int[] candies, int extraCandies) {
        var sortedCandies = Arrays.stream(candies).sorted().toArray();
        var result = new ArrayList<Boolean>();

        IntStream.range(0, candies.length).forEach((i) -> {
            if (candies[i] + extraCandies >= sortedCandies[candies.length - 1]) {
                result.add(true);
            } else {
                result.add(false);
            }
        });

        return result;
    }
}
