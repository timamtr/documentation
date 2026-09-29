import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class Problem4 {

    public static double minDistBrute(double[][] P) {
        if (P == null || P.length < 2) return Double.MAX_VALUE;
        double min = Double.MAX_VALUE;
        for (int i = 0; i < P.length; i++) {
            for (int j = i + 1; j < P.length; j++) {
                double d = dist(P[i], P[j]);
                if (d < min) {
                    min = d;
                }
            }
        }
        return min;
    }

    public static double minDistSmart(double[][] P) {
        if (P == null || P.length < 2) return Double.MAX_VALUE;
        double[][] Px = P.clone();
        Arrays.sort(Px, Comparator.comparingDouble(a -> a[0]));
        return closestRec(Px, 0, Px.length - 1);
    }

    private static double closestRec(double[][] Px, int low, int high) {
        if (high - low + 1 <= 3) {
            double min = Double.MAX_VALUE;
            for (int i = low; i <= high; i++) {
                for (int j = i + 1; j <= high; j++) {
                    min = Math.min(min, dist(Px[i], Px[j]));
                }
            }
            return min;
        }

        int mid = low + (high - low) / 2;
        double[] midPoint = Px[mid];

        double dl = closestRec(Px, low, mid);
        double dr = closestRec(Px, mid + 1, high);
        double d = Math.min(dl, dr);

        double[][] strip = new double[high - low + 1][];
        int j = 0;
        for (int i = low; i <= high; i++) {
            if (Math.abs(Px[i][0] - midPoint[0]) < d) {
                strip[j++] = Px[i];
            }
        }

        Arrays.sort(strip, 0, j, Comparator.comparingDouble(a -> a[1]));

        for (int i = 0; i < j; i++) {
            for (int k = i + 1; k < j && (strip[k][1] - strip[i][1]) < d; k++) {
                d = Math.min(d, dist(strip[i], strip[k]));
            }
        }
        return d;
    }

    private static double dist(double[] p1, double[] p2) {
        double dx = p1[0] - p2[0];
        double dy = p1[1] - p2[1];
        return Math.sqrt(dx * dx + dy * dy);
    }

    public static void main(String[] args) {
        double[][] example = {{0, 0}, {3, 4}, {-5, -3}};
        System.out.println("Example (Expected 5.0): " + minDistSmart(example));
        System.out.println("---------------------------------------------------------------");

        int[] sizes = {100, 500, 1000, 5000, 10000, 20000};
        int iterations = 20;
        Random rand = new Random(42);

        System.out.printf("%-12s | %-18s | %-18s | %-10s%n",
                "Size (n)", "Brute-Force (ns)", "Divide&Conquer(ns)", "Speedup");
        System.out.println("---------------------------------------------------------------");

        for (int size : sizes) {
            double[][] arr = new double[size][2];
            for (int i = 0; i < size; i++) {
                arr[i][0] = rand.nextDouble() * 20000 - 10000;
                arr[i][1] = rand.nextDouble() * 20000 - 10000;
            }

            for (int i = 0; i < 5; i++) {
                minDistBrute(arr);
                minDistSmart(arr);
            }

            long startBrute = System.nanoTime();
            for (int i = 0; i < iterations; i++) {
                minDistBrute(arr);
            }
            long avgBrute = (System.nanoTime() - startBrute) / iterations;

            long startSmart = System.nanoTime();
            for (int i = 0; i < iterations; i++) {
                minDistSmart(arr);
            }
            long avgSmart = (System.nanoTime() - startSmart) / iterations;

            System.out.printf("%-12d | %-18d | %-18d | %.2fx%n",
                    size, avgBrute, avgSmart, (double) avgBrute / Math.max(1, avgSmart));
        }
    }
}
