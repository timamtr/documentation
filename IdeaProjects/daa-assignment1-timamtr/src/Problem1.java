import java.util.Arrays;
import java.util.Random;

public class Problem1 {

    public static int countFreqBrute(int[] A, int key) {
        if (A == null || A.length == 0) return 0;

        int count = 0;
        for (int i = 0; i < A.length; i++) {
            if (A[i] == key) {
                count++;
            }
        }
        return count;
    }

    public static int countFreqSmart(int[] A, int key) {
        if (A == null || A.length == 0) return 0;

        int firstIndex = findFirst(A, key);

        if (firstIndex == -1) {
            return 0;
        }

        int lastIndex = findLast(A, key);

        return lastIndex - firstIndex + 1;
    }

    private static int findFirst(int[] A, int key) {
        int left = 0;
        int right = A.length - 1;
        int result = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (A[mid] == key) {
                result = mid;
                right = mid - 1;
            } else if (A[mid] < key) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return result;
    }

    private static int findLast(int[] A, int key) {
        int left = 0;
        int right = A.length - 1;
        int result = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (A[mid] == key) {
                result = mid;
                left = mid + 1;
            } else if (A[mid] < key) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        int[] example1 = {1, 1, 1, 2, 2, 2, 2, 2, 2, 4, 4, 4, 5, 5, 5, 5};
        System.out.println("Example 1 (key=4): " + countFreqSmart(example1, 4)); // Ожидаем 3
        System.out.println("Example 2 (key=3): " + countFreqSmart(example1, 3)); // Ожидаем 0
        System.out.println("---------------------------------------------------------------");

        int[] sizes = {100, 1000, 10_000, 50_000, 100_000};
        int iterations = 1000;
        Random rand = new Random(42);

        System.out.printf("%-12s | %-18s | %-18s | %-10s%n",
                "Size (n)", "Brute-Force (ns)", "Divide&Conquer(ns)", "Speedup");
        System.out.println("---------------------------------------------------------------");

        for (int n : sizes) {
            int[] arr = new int[n];
            for (int i = 0; i < n; i++) arr[i] = rand.nextInt(100);
            Arrays.sort(arr);
            int key = 50;

            for (int i = 0; i < 200; i++) {
                countFreqBrute(arr, key);
                countFreqSmart(arr, key);
            }

            long startBrute = System.nanoTime();
            for (int i = 0; i < iterations; i++) countFreqBrute(arr, key);
            long avgBrute = (System.nanoTime() - startBrute) / iterations;

            long startSmart = System.nanoTime();
            for (int i = 0; i < iterations; i++) countFreqSmart(arr, key);
            long avgSmart = (System.nanoTime() - startSmart) / iterations;

            System.out.printf("%-12d | %-18d | %-18d | %.2fx%n",
                    n, avgBrute, avgSmart, (double) avgBrute / Math.max(1, avgSmart));
        }
    }
}