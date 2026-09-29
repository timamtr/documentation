import java.util.Random;

public class Problem3 {

    public static long maxSumBrute(int[] A) {
        if (A == null || A.length == 0) return 0;

        long maxSum = Long.MIN_VALUE;
        for (int i = 0; i < A.length; i++) {
            long currentSum = 0;
            for (int j = i; j < A.length; j++) {
                currentSum += A[j];
                if (currentSum > maxSum) {
                    maxSum = currentSum;
                }
            }
        }
        return maxSum;
    }

    public static long maxSumSmart(int[] A) {
        if (A == null || A.length == 0) return 0;
        return findMaxSubarray(A, 0, A.length - 1);
    }

    private static long findMaxSubarray(int[] A, int low, int high) {
        if (low == high) {
            return A[low];
        }

        int mid = low + (high - low) / 2;

        long leftMax = findMaxSubarray(A, low, mid);
        long rightMax = findMaxSubarray(A, mid + 1, high);
        long crossMax = findMaxCrossingSubarray(A, low, mid, high);

        return Math.max(Math.max(leftMax, rightMax), crossMax);
    }

    private static long findMaxCrossingSubarray(int[] A, int low, int mid, int high) {
        long leftSum = Long.MIN_VALUE;
        long sum = 0;
        for (int i = mid; i >= low; i--) {
            sum += A[i];
            if (sum > leftSum) {
                leftSum = sum;
            }
        }

        long rightSum = Long.MIN_VALUE;
        sum = 0;
        for (int j = mid + 1; j <= high; j++) {
            sum += A[j];
            if (sum > rightSum) {
                rightSum = sum;
            }
        }

        return leftSum + rightSum;
    }

    public static void main(String[] args) {
        int[] example = {-17, 5, 3, -10, 6, 1, 4, -3, 8, 1, -13, 4};
        System.out.println("Example: " + maxSumSmart(example));
        System.out.println("---------------------------------------------------------------");

        int[] sizes = {100, 1000, 5000, 10000, 50000};
        int iterations = 100;
        Random rand = new Random(42);

        System.out.printf("%-12s | %-18s | %-18s | %-10s%n",
                "Size (n)", "Brute-Force (ns)", "Divide&Conquer(ns)", "Speedup");
        System.out.println("---------------------------------------------------------------");

        for (int size : sizes) {
            int[] arr = new int[size];
            for (int i = 0; i < size; i++) {
                arr[i] = rand.nextInt(20001) - 10000;
            }

            for (int i = 0; i < 50; i++) {
                maxSumBrute(arr);
                maxSumSmart(arr);
            }

            long startBrute = System.nanoTime();
            for (int i = 0; i < iterations; i++) {
                maxSumBrute(arr);
            }
            long avgBrute = (System.nanoTime() - startBrute) / iterations;

            long startSmart = System.nanoTime();
            for (int i = 0; i < iterations; i++) {
                maxSumSmart(arr);
            }
            long avgSmart = (System.nanoTime() - startSmart) / iterations;

            System.out.printf("%-12d | %-18d | %-18d | %.2fx%n",
                    size, avgBrute, avgSmart, (double) avgBrute / Math.max(1, avgSmart));
        }
    }
}
