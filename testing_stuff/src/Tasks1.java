package src;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Tasks1 {
    public static void main(String[] args) {
//        System.out.println(mergeAlternately("asdasd", "qqq"));
//        System.out.println(gcdOfStrings("ABABAB", "ABAB"));
//        System.out.println(Arrays.toString(kidsWithCandies(new int[]{12,1,12}, 3).toArray()));
//        System.out.println(canPlaceFlowers(new int[]{1,0,0,0,1,0,1}, 1));
//        System.out.println(reverseVowels("IceCreAm"));
//        System.out.println(reverseWords("  IceCreAm as  kl "));
//        System.out.println(Arrays.toString(productExceptSelf(new int[]{1,2,3,4})));
//        System.out.println(increasingTriplet(new int[]{1,1,1,1,1,1,1,1,1,1,1,}));
//        System.out.println(compress(new char[]{'a', 'a', 'b', 'b', 'c', 'c', 'c'}));
//        System.out.println(Arrays.toString(moveZeroes(new int[]{5, 0, 5, 0, 5})));
//        System.out.println("Array after moving zeros to end: " + Arrays.toString(moveZerosToEnd(new int[]{0, 1, 0, 0, 3, 12})));
//        System.out.println("Array after moving zeros to end: " + Arrays.toString(moveZerosToEnd(new int[]{0, 1, 0, 0, 3, 12})));
//        System.out.println(isSubsequence("b", ""));
//        System.out.println("maxArea: " + maxArea(new int[]{1,8,6,2,5,4,8,3,7}));
        System.out.println("maxOperations: " + maxOperations(new int[]{1,8,2,2,5,1,8,3,7}, 3));
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

    public static int[] productExceptSelf(int[] nums) {
        int arrLength = nums.length;
        int[] res = new int[arrLength];

        int leftProduct = 1;
        for (int i = 0; i < arrLength; i++) {
            res[i] = leftProduct;
            leftProduct *= nums[i];
        }

        int rightProduct = 1;
        for (int i = arrLength - 1; i >= 0; i--) {
            res[i] *= rightProduct;
            rightProduct *= nums[i];
        }

        return res;
    }

    public static boolean increasingTriplet(int[] nums) {
        if (nums == null && nums.length < 3) {
            return false;
        }

        int smallest = Integer.MAX_VALUE;
        int secondSmallest = Integer.MAX_VALUE;

        for (int num : nums) {
            if (num <= smallest) {
                smallest = num;
            } else if (num <= secondSmallest) {
                secondSmallest = num;
            } else {
                return true;
            }
        }

        return false;
    }

    public static int compress(char[] chars) {
        if (chars == null || chars.length == 1) {
            return 1;
        }


        var lastCharGroupIndex = 0;
        var elementIndex = 0;

        while (elementIndex < chars.length) {
            var currentCharGroup = chars[elementIndex];
            var currentCharGroupCounter = 0;

            while (elementIndex < chars.length && currentCharGroup == chars[elementIndex]) {
                currentCharGroupCounter++;
                elementIndex++;
            }

            chars[lastCharGroupIndex++] = currentCharGroup;

            if (currentCharGroupCounter > 1) {
                for (char counter : Integer.toString(currentCharGroupCounter).toCharArray()) {
                    chars[lastCharGroupIndex++] = counter;
                }
            }
        }
        return lastCharGroupIndex;
    }

    public static int[] moveZeroes(int[] nums) {
        int nextInsertPosition = 0;

        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != 0) {
                nums[nextInsertPosition] = nums[i];
                nextInsertPosition++;
            }
        }

        int zeroInsertPosition = nextInsertPosition;
        for (int i = zeroInsertPosition; i < nums.length; i++) {
            nums[i] = 0;
        }

        return nums;
    }

    public static int[] moveZerosToEnd(int[] arr) {
        int left = 0;

        for (int right = 0; right < arr.length; right++) {
            if (arr[right] != 0) {
                int temp = arr[left];
                arr[left] = arr[right];
                arr[right] = temp;
                left++;
            }
        }

        return arr;
    }

    public static boolean isSubsequence(String s, String t) {
        int left = 0;

        if (s.isEmpty()) {
            return true;
        }

        for (int i = 0; i < t.length(); i++) {
            if(left < s.length() && s.charAt(left) == t.charAt(i)) {
                left++;
            }
        }

        return left == s.length();
    }

    public static int maxArea(int[] height) {
        int maxArea = 0;

        int left = 0;
        int right = height.length - 1;

        while (left < right) {
            maxArea = max(maxArea, (min(height[left], height[right]) * (right - left)));
            if(height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }
        return maxArea;
    }

    public static int maxOperations(int[] nums, int k) {
        Arrays.sort(nums);

        int left = 0;
        int right = nums.length - 1;
        int operationCount = 0;

        while (left < right) {
            int sum = nums[left] + nums[right];

            if (sum == k) {
                operationCount++;
                left++;
                right--;
            } else if (sum < k) {
                left++;
            } else {
                right--;
            }
        }

        return operationCount;
    }
}
