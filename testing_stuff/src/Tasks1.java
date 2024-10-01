package src;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Tasks1 {
    public static void main(String[] args) {
//        System.out.println(mergeAlternately("asdasd", "qqq"));
//        System.out.println(gcdOfStrings("ABABAB", "ABAB"));
//        System.out.println(Arrays.toString(kidsWithCandies(new int[]{12,1,12}, 3).toArray()));
//        System.out.println(canPlaceFlowers(new int[]{1,0,0,0,1,0,1}, 1));
//        System.out.println(reverseVowels("IceCreAm"));
        System.out.println(reverseWords("  IceCreAm as  kl "));
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

    public static boolean canPlaceFlowers(int[] flowerbed, int n) {
        if (n == 0) {
            return true;
        }

        var placeCounter = 0;

        for (int i = 0; i < flowerbed.length; i++) {
            var previousElem = i > 0 ? flowerbed[i - 1] : 0;
            var nextElem = i < flowerbed.length - 1 ? flowerbed[i + 1] : 0;
            if (flowerbed[i] == 0) {
                if (previousElem == 0 && nextElem == 0) {
                    flowerbed[i] = 1;
                    System.out.println(Arrays.toString(flowerbed));
                    placeCounter++;
                    i++;
                }
            }
        }

        return placeCounter >= n;
    }

    public static String reverseVowels(String s) {
        var vowelsRex = "[aeiou]";
        var patternCompiled = Pattern.compile(vowelsRex, Pattern.CASE_INSENSITIVE);
        var vowelIndexes = new ArrayList<Integer>();
        var foundVowels = new ArrayList<String>();
        var foundVowelAndIn = new HashMap<Integer, String>();
        var chars = s.toCharArray();

        Matcher matcher = patternCompiled.matcher(s);

        while (matcher.find()) {
            vowelIndexes.add(matcher.start());
            foundVowels.add(matcher.group());
        }

        Collections.reverse(foundVowels);

        for (int i = 0; i < vowelIndexes.size(); i++) {
            foundVowelAndIn.put(vowelIndexes.get(i), foundVowels.get(i));
        }

        foundVowelAndIn.forEach((k, v) -> {
            chars[k] = v.charAt(0);
        });


        return String.valueOf(chars);
    }


    public static String reverseWords(String s) {
        var regex = " +";
        var strings = s.trim().split(regex);
        var list = Arrays.asList(strings);

        Collections.reverse(list);

        System.out.println(Arrays.toString(strings));

        return String.join(" ", strings);
    }
}
