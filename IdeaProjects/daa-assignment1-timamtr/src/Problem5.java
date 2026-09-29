import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

public class Problem5 {

    public static String multBrute(String A, String B) {
        if (A.equals("0") || B.equals("0")) return "0";

        int[] res = new int[A.length() + B.length()];

        for (int i = A.length() - 1; i >= 0; i--) {
            for (int j = B.length() - 1; j >= 0; j--) {
                int mul = (A.charAt(i) - '0') * (B.charAt(j) - '0');
                int p1 = i + j;
                int p2 = i + j + 1;

                int sum = mul + res[p2];
                res[p2] = sum % 10;
                res[p1] += sum / 10;
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int val : res) {
            if (!(sb.length() == 0 && val == 0)) {
                sb.append(val);
            }
        }
        return sb.length() == 0 ? "0" : sb.toString();
    }

    public static String multSmart(String A, String B) {
        if (A.equals("0") || B.equals("0")) return "0";

        int n = Math.max(A.length(), B.length());
        int m = 1;
        while (m < n) m *= 2;

        long[] a = new long[m];
        long[] b = new long[m];

        for (int i = 0; i < A.length(); i++) {
            a[i] = A.charAt(A.length() - 1 - i) - '0';
        }
        for (int i = 0; i < B.length(); i++) {
            b[i] = B.charAt(B.length() - 1 - i) - '0';
        }

        long[] res = karatsuba(a, b);

        long carry = 0;
        StringBuilder sb = new StringBuilder();
        for (long val : res) {
            long sum = val + carry;
            sb.append(sum % 10);
            carry = sum / 10;
        }
        while (carry > 0) {
            sb.append(carry % 10);
            carry /= 10;
        }

        String finalRes = sb.reverse().toString();
        int start = 0;
        while (start < finalRes.length() - 1 && finalRes.charAt(start) == '0') {
            start++;
        }
        return finalRes.substring(start);
    }

    private static long[] karatsuba(long[] a, long[] b) {
        int n = a.length;
        long[] res = new long[2 * n];

        if (n <= 32) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    res[i + j] += a[i] * b[j];
                }
            }
            return res;
        }

        int half = n / 2;
        long[] a0 = Arrays.copyOfRange(a, 0, half);
        long[] a1 = Arrays.copyOfRange(a, half, n);
        long[] b0 = Arrays.copyOfRange(b, 0, half);
        long[] b1 = Arrays.copyOfRange(b, half, n);

        long[] z0 = karatsuba(a0, b0);
        long[] z2 = karatsuba(a1, b1);

        long[] aSum = new long[half];
        long[] bSum = new long[half];
        for (int i = 0; i < half; i++) {
            aSum[i] = a0[i] + a1[i];
            bSum[i] = b0[i] + b1[i];
        }

        long[] z1 = karatsuba(aSum, bSum);

        for (int i = 0; i < n; i++) {
            z1[i] -= (z0[i] + z2[i]);
        }

        for (int i = 0; i < n; i++) {
            res[i] += z0[i];
            res[i + half] += z1[i];
            res[i + n] += z2[i];
        }

        return res;
    }

    public static void main(String[] args) {
        String A = "12345678987654321";
        String B = "98765432123456789";
        System.out.println("Example (Expected: 1219326320073159566072245112635269)");
        System.out.println("Smart Output    : " + multSmart(A, B));
        System.out.println("---------------------------------------------------------------");

        int[] sizes = {100, 500, 1000, 5000, 10000};
        int iterations = 10;
        Random rand = new Random(42);

        System.out.printf("%-12s | %-18s | %-18s | %-10s%n",
                "Size (n)", "Brute-Force (ns)", "Divide&Conquer(ns)", "Speedup");
        System.out.println("---------------------------------------------------------------");

        for (int size : sizes) {
            StringBuilder sbA = new StringBuilder();
            StringBuilder sbB = new StringBuilder();
            for (int i = 0; i < size; i++) {
                sbA.append(rand.nextInt(10));
                sbB.append(rand.nextInt(10));
            }
            String strA = sbA.toString();
            String strB = sbB.toString();

            // Correctness check using BigInteger
            BigInteger bigA = new BigInteger(strA);
            BigInteger bigB = new BigInteger(strB);
            if (!multSmart(strA, strB).equals(bigA.multiply(bigB).toString())) {
                System.out.println("Error in smart multiplication at size " + size);
            }

            // Warmup
            for (int i = 0; i < 5; i++) {
                multBrute(strA, strB);
                multSmart(strA, strB);
            }

            long startBrute = System.nanoTime();
            for (int i = 0; i < iterations; i++) {
                multBrute(strA, strB);
            }
            long avgBrute = (System.nanoTime() - startBrute) / iterations;

            long startSmart = System.nanoTime();
            for (int i = 0; i < iterations; i++) {
                multSmart(strA, strB);
            }
            long avgSmart = (System.nanoTime() - startSmart) / iterations;

            System.out.printf("%-12d | %-18d | %-18d | %.2fx%n",
                    size, avgBrute, avgSmart, (double) avgBrute / Math.max(1, avgSmart));
        }
    }
}
