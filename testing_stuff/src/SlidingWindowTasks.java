package src;

public class SlidingWindowTasks {

    public static void main(String[] args) {
        System.out.println("maxOperations: " + maximumAverageSubArrayV1(new int[]{1,12,-5,-6,50,3}, 4));
        System.out.println("maxOperations: " + maximumAverageSubArrayV1(new int[]{5}, 1));
    }

    public static double maximumAverageSubArrayV1(int[] nums, int k) {
        if(nums.length == 0 || k == 0) {
            return 0;
        }

        double currentAverage = 0;
        double maxAverage = 0;

        for (int i = 0; i < k; i++) {
            currentAverage += nums[i];
        }
        maxAverage = currentAverage;

        for (int i = k; i < nums.length; i++) {
            currentAverage += nums[i] - nums[i - k];
            maxAverage = Math.max(maxAverage, currentAverage);
        }

        return maxAverage / k;
    }
}
