import java.util.Arrays;
import java.util.Random;

public class Problem2 {

    public static double getMedianBrute(int[] A, int[] B) {
        int m = A.length;
        int n = B.length;
        int[] merged = new int[m + n];

        int i = 0;
        int j = 0;
        int k = 0;

        while (i < m && j < n) {
            if (A[i] < B[j]) {
                merged[k++] = A[i++];
            } else {
                merged[k++] = B[j++];
            }
        }

        while (i < m) {
            merged[k++] = A[i++];
        }

        while (j < n) {
            merged[k++] = B[j++];
        }

        int totalLen = m + n;
        if (totalLen % 2 != 0) {
            return merged[totalLen / 2];
        } else {
            return (merged[totalLen / 2 - 1] + merged[totalLen / 2]) / 2.0;
        }
    }

    public static double getMedianSmart(int[] A, int[] B) {
        if (A.length > B.length) {
            return getMedianSmart(B, A);
        }

        int m = A.length;
        int n = B.length;
        int left = 0;
        int right = m;
        int halfLen = (m + n + 1) / 2;

        while (left <= right) {
            int partitionA = left + (right - left) / 2;
            int partitionB = halfLen - partitionA;

            int maxLeftA = (partitionA == 0) ? Integer.MIN_VALUE : A[partitionA - 1];
            int minRightA = (partitionA == m) ? Integer.MAX_VALUE : A[partitionA];

            int maxLeftB = (partitionB == 0) ? Integer.MIN_VALUE : B[partitionB - 1];
            int minRightB = (partitionB == n) ? Integer.MAX_VALUE : B[partitionB];

            if (maxLeftA <= minRightB && maxLeftB <= minRightA) {
                if ((m + n) % 2 != 0) {
                    return Math.max(maxLeftA, maxLeftB);
                } else {
                    return (Math.max(maxLeftA, maxLeftB) + Math.min(minRightA, minRightB)) / 2.0;
                }
            } else if (maxLeftA > minRightB) {
                right = partitionA - 1;
            } else {
                left = partitionA + 1;
            }
        }

        return 0.0;
    }

    public static void main(String[] args) {
        int[] ex1A = {2, 4};
        int[] ex1B = {3};
        System.out.println("Example 1: " + getMedianSmart(ex1A, ex1B));

        int[] ex2A = {2, 4};
        int[] ex2B = {3, 5};
        System.out.println("Example 2: " + getMedianSmart(ex2A, ex2B));
        System.out.println("---------------------------------------------------------------");

        int[] sizes = {100, 500, 1000, 5000, 10000};
        int iterations = 2000;
        Random rand = new Random(42);

        System.out.printf("%-12s | %-18s | %-18s | %-10s%n",
                "Size (n=m)", "Brute-Force (ns)", "Divide&Conquer(ns)", "Speedup");
        System.out.println("---------------------------------------------------------------");

        for (int size : sizes) {
            int[] arrA = new int[size];
            int[] arrB = new int[size];
            for (int i = 0; i < size; i++) {
                arrA[i] = rand.nextInt(10000);
                arrB[i] = rand.nextInt(10000);
            }
            Arrays.sort(arrA);
            Arrays.sort(arrB);

            for (int i = 0; i < 500; i++) {
                getMedianBrute(arrA, arrB);
                getMedianSmart(arrA, arrB);
            }

            long startBrute = System.nanoTime();
            for (int i = 0; i < iterations; i++) {
                getMedianBrute(arrA, arrB);
            }
            long avgBrute = (System.nanoTime() - startBrute) / iterations;

            long startSmart = System.nanoTime();
            for (int i = 0; i < iterations; i++) {
                getMedianSmart(arrA, arrB);
            }
            long avgSmart = (System.nanoTime() - startSmart) / iterations;

            System.out.printf("%-12d | %-18d | %-18d | %.2fx%n",
                    size, avgBrute, avgSmart, (double) avgBrute / Math.max(1, avgSmart));
        }
    }
}