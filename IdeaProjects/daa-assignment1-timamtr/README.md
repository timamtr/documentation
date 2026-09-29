# Design and Analysis of Algorithms - Assignment 1
**Author:** Темірлан Таткен  
**University:** Astana IT University

This repository contains implementations and complexity analyses for five algorithmic problems, focusing on the transition from Brute-Force $O(n^2)$ / $O(n)$ approaches to efficient Divide-and-Conquer $O(n \log n)$ / $O(\log n)$ strategies.

---

## Problem 1: Number of Occurrences

### 1. Solution Ideas

#### Brute-Force Approach (`countFreqBrute`)
The brute-force solution iterates sequentially through the entire array $A$ from index $0$ to $n - 1$. We maintain a counter variable initialized to $0$ and increment it every time we encounter an element $A[i] == key$. While simple and correct for any array, this approach completely ignores the crucial property that the array $A$ is **sorted in non-descending order**.

#### Divide-and-Conquer Approach (`countFreqSmart`)
Because the array $A$ is sorted, all occurrences of `key` (if any exist) are contiguous and form a single block $[i_{first}, i_{last}]$. Instead of counting elements one by one, we can reduce the problem to finding the boundaries of this block using two modified binary searches (following Steven Skiena's *The Algorithm Design Manual*, Section 5.1.1):
1. **Left Boundary (`findFirst`)**: We iteratively divide the search space in half. When we find $A[mid] == key$, we record the position and **continue searching in the left half** to see if an earlier occurrence exists.
2. **Right Boundary (`findLast`)**: Similarly, when $A[mid] == key$, we record the position and **continue searching in the right half** to find the absolute last occurrence.

If `findFirst` returns $-1$, the element is not present in $A$, and we immediately return $0$. Otherwise, the total number of occurrences is computed in $\Theta(1)$ time as:
$$\text{count} = i_{last} - i_{first} + 1$$

### 2. Asymptotic Time and Space Complexity Analysis

#### Brute-Force (`countFreqBrute`)
* **Time Complexity**: $T(n) = \Theta(n)$, since the number of operations scales linearly with the input size $n$.
* **Space Complexity**: $\Theta(1)$ auxiliary space.

#### Divide-and-Conquer (`countFreqSmart`)
* **Recurrence Relation**: At each step of the binary search, the algorithm evaluates the midpoint in constant time $\Theta(1)$ and halves the search space:
  $$T(n) = T\left(\frac{n}{2}\right) + \Theta(1)$$
* **Justification via the Master Theorem**:
  For $T(n) = aT(n/b) + f(n)$, we have $a = 1$, $b = 2$, and $f(n) = \Theta(1) = \Theta(n^0)$.
  Since $n^{\log_b a} = n^0 = 1$, this is **Case 2 of the Master Theorem**, yielding:
  $$T(n) = \Theta(\log n)$$
  Since we perform at most two sequential binary searches, the total running time remains tightly bounded at $\Theta(\log n)$.
* **Space Complexity**: $\Theta(1)$ auxiliary space, as the solution uses an iterative `while` loop rather than the call stack.

### 3. Empirical Comparison (`System.nanoTime()`)

| Input Size ($n$) | Brute-Force Time (ns) | Divide-and-Conquer Time (ns) | Speedup Factor |
| :--- | :--- | :--- | :--- |
| $100$ | $48\text{ ns}$ | $19\text{ ns}$ | $2.53\times$ |
| $1,000$ | $390\text{ ns}$ | $28\text{ ns}$ | $13.93\times$ |
| $10,000$ | $3,850\text{ ns}$ | $37\text{ ns}$ | $104.05\times$ |
| $50,000$ | $19,420\text{ ns}$ | $43\text{ ns}$ | $451.63\times$ |
| $100,000$ | $39,150\text{ ns}$ | $46\text{ ns}$ | $851.09\times$ |

---

## Problem 2: Median of Two Sorted Arrays

### 1. Solution Ideas

#### Brute-Force Approach (`getMedianBrute`)
The simplest way to find the median of two sorted arrays $A$ and $B$ (of sizes $m$ and $n$) is to merge them into a single sorted array of size $m + n$. We iterate through both arrays simultaneously with two pointers, picking the smaller element at each step. Once merged, we return the middle element (if the total length is odd) or the average of the two middle elements (if even).

#### Divide-and-Conquer Approach (`getMedianSmart`)
The median divides a combined sorted array into two equal halves where elements on the left are $\le$ elements on the right. We perform a binary search on the **smaller** array (let's say $A$ with length $m$) to find the correct partition line.
- If we cut $A$ at index `i`, we must cut $B$ at index `j = (m + n + 1) / 2 - i`.
- The partition is correct if `A[i-1] <= B[j]` and `B[j-1] <= A[i]`.
- Based on these conditions, we adjust our binary search boundaries. Once the correct partition is found, the median is calculated in $O(1)$ time based on the maximum of the left elements and minimum of the right elements.

### 2. Asymptotic Time and Space Complexity Analysis

#### Brute-Force (`getMedianBrute`)
* **Time Complexity**: $\Theta(m + n)$. We visit each element of both arrays exactly once during the merge.
* **Space Complexity**: $\Theta(m + n)$. We allocate a new `merged` array of size $m + n$.

#### Divide-and-Conquer (`getMedianSmart`)
* **Recurrence Relation**: We evaluate the partition condition in $\Theta(1)$ time and reduce the search space by half. The search space is bounded by the size of the smaller array $k = \min(m, n)$.
  $$T(k) = T\left(\frac{k}{2}\right) + \Theta(1)$$
* **Justification via Master Theorem**:
  $a = 1$, $b = 2$, $f(k) = \Theta(1)$. Since $k^{\log_b a} = 1$, we apply **Case 2**, yielding $T(k) = \Theta(\log k)$.
* **Bounds**: $T(n, m) = \Theta(\log(\min(m, n)))$. Space complexity is $\Theta(1)$.

### 3. Empirical Comparison

| Size ($m=n$) | Brute-Force Time (ns) | Divide-and-Conquer Time (ns) | Speedup Factor |
| :--- | :--- | :--- | :--- |
| $1,000$ | $740\text{ ns}$ | $34\text{ ns}$ | $21.76\times$ |
| $5,000$ | $3,800\text{ ns}$ | $38\text{ ns}$ | $100.00\times$ |
| $10,000$ | $7,500\text{ ns}$ | $41\text{ ns}$ | $182.93\times$ |

---

## Problem 3: Largest Subrange (Maximum Subarray Problem)

### 1. Solution Ideas

#### Brute-Force Approach (`maxSumBrute`)
We evaluate every possible contiguous subarray within the array $A$ using two nested loops. We fix the starting index $i$ and expand the ending index $j$. A running sum is updated for each $j$, allowing us to track the maximum sum encountered in $O(1)$ time per step.

#### Divide-and-Conquer Approach (`maxSumSmart`)
We recursively divide the array into a left half and a right half. The maximum subarray must be:
1. Entirely in the left half.
2. Entirely in the right half.
3. Crossing the midpoint.

We recursively find the maximum for the left and right halves. For the crossing subarray, we linearly scan outwards from the midpoint to find the maximum sum spanning across both sides. The final answer is the maximum of these three values.

### 2. Asymptotic Time and Space Complexity Analysis

#### Brute-Force (`maxSumBrute`)
* **Time Complexity**: $\Theta(n^2)$. The total number of iterations is $n(n+1)/2$.
* **Space Complexity**: $\Theta(1)$.

#### Divide-and-Conquer (`maxSumSmart`)
* **Recurrence Relation**: Splitting the array creates two subproblems of size $n/2$, and checking the crossing subarray takes $\Theta(n)$ time.
  $$T(n) = 2T\left(\frac{n}{2}\right) + \Theta(n)$$
* **Justification via Master Theorem**:
  $a = 2$, $b = 2$, $f(n) = \Theta(n)$. The critical exponent is $n^{\log_2 2} = n$. Since $f(n) = \Theta(n^{\log_b a})$, this is **Case 2 of the Master Theorem**. Therefore:
  $$T(n) = \Theta(n \log n)$$
* **Space Complexity**: $\Theta(\log n)$ due to the recursion stack.

### 3. Empirical Comparison

| Array Size ($n$) | Brute-Force Time (ns) | Divide-and-Conquer Time (ns) | Speedup Factor |
| :--- | :--- | :--- | :--- |
| $1,000$ | $280,000\text{ ns}$ | $15,000\text{ ns}$ | $18.66\times$ |
| $10,000$ | $24,500,000\text{ ns}$ | $175,000\text{ ns}$ | $140.00\times$ |
| $50,000$ | $615,000,000\text{ ns}$ | $950,000\text{ ns}$ | $647.36\times$ |

---

## Problem 4: Closest Pair of Points

### 1. Solution Ideas

#### Brute-Force Approach (`minDistBrute`)
The brute-force algorithm systematically calculates the Euclidean distance $\sqrt{(x_i - x_j)^2 + (y_i - y_j)^2}$ between every unique pair of points using two nested loops, tracking the minimum.

#### Divide-and-Conquer Approach (`minDistSmart`)
1. **Pre-sorting:** The points are sorted by their X-coordinate.
2. **Divide:** The sorted array is recursively split into two halves by a vertical line.
3. **Conquer:** Recursively find the smallest distance in the left ($d_l$) and right ($d_r$) halves. Let $d = \min(d_l, d_r)$.
4. **Combine (Strip Check):** We collect all points strictly within a horizontal distance $d$ of the dividing line into a `strip` array. We sort this strip by Y-coordinates. Geometrically, for every point in the strip, we only need to check subsequent points whose Y-distance is less than $d$ (a maximum of 7 points).

### 2. Asymptotic Time and Space Complexity Analysis

#### Brute-Force (`minDistBrute`)
* **Time Complexity**: $\Theta(n^2)$.
* **Space Complexity**: $\Theta(1)$.

#### Divide-and-Conquer (`minDistSmart`)
* **Time Complexity**:
  Initial sorting takes $\Theta(n \log n)$. At each recursive step, we split the array and sort the boundary strip by Y-coordinates (worst case $O(n \log n)$).
  $$T(n) = 2T\left(\frac{n}{2}\right) + O(n \log n)$$
  By the extended Master Theorem for polylogarithmic functions ($k=1$), this yields a tight bound of $\Theta(n \log^2 n)$.
* **Space Complexity**: $O(n)$ auxiliary space to build the strips.

### 3. Empirical Comparison

| Array Size ($n$) | Brute-Force Time (ns) | Divide-and-Conquer Time (ns) | Speedup Factor |
| :--- | :--- | :--- | :--- |
| $1,000$ | $740,000\text{ ns}$ | $68,000\text{ ns}$ | $10.88\times$ |
| $10,000$ | $75,100,000\text{ ns}$ | $730,000\text{ ns}$ | $102.88\times$ |
| $20,000$ | $302,000,000\text{ ns}$ | $1,550,000\text{ ns}$ | $194.83\times$ |

---

## Problem 5: Integer Multiplication

### 1. Solution Ideas

#### Brute-Force Approach (`multBrute`)
This mirrors standard grade-school long multiplication. We allocate an array of size $len(A) + len(B)$. We multiply each digit of $A$ by each digit of $B$, accumulate the result into the appropriate positional index, and handle base-10 carry operations sequentially.

#### Divide-and-Conquer Approach (`multSmart` - Karatsuba)
We parse the numerical strings backward into padded `long[]` polynomial arrays. Instead of splitting strings, we recursively split the arrays into lower ($a_0$) and upper ($a_1$) halves, calculating:
1. $z_0 = a_0 \times b_0$
2. $z_2 = a_1 \times b_1$
3. $z_1 = (a_0 + a_1) \times (b_0 + b_1) - z_0 - z_2$
   The product is combined using shifted index addition without resolving base-10 carries. A single sequential pass at the very end resolves all carries to formulate the final string. For inputs smaller than 32 digits, it falls back to $O(n^2)$ polynomial multiplication to avoid recursion overhead.

### 2. Asymptotic Time and Space Complexity Analysis

#### Brute-Force (`multBrute`)
* **Time Complexity**: $\Theta(n^2)$.
* **Space Complexity**: $\Theta(n)$.

#### Divide-and-Conquer (`multSmart` - Karatsuba)
* **Recurrence Relation**: Karatsuba requires 3 recursive calls of size $n/2$ plus $\Theta(n)$ additions.
  $$T(n) = 3T\left(\frac{n}{2}\right) + \Theta(n)$$
* **Justification via Master Theorem**:
  $a = 3$, $b = 2$, and $f(n) = \Theta(n)$. The critical exponent is $n^{\log_2 3} \approx n^{1.585}$. Since $f(n) = O(n^{\log_2 3 - \epsilon})$, this is **Case 1 of the Master Theorem**.
  $$T(n) = \Theta(n^{\log_2 3}) \approx \Theta(n^{1.585})$$
* **Space Complexity**: $O(n \log n)$ due to sub-array copies at each recursion level.

### 3. Empirical Comparison

| Input Size ($n$ digits) | Brute-Force Time (ns) | Divide-and-Conquer Time (ns) | Speedup Factor |
| :--- | :--- | :--- | :--- |
| $1,000$ | $1,250,000\text{ ns}$ | $580,000\text{ ns}$ | $2.15\times$ |
| $5,000$ | $31,500,000\text{ ns}$ | $6,100,000\text{ ns}$ | $5.16\times$ |
| $10,000$ | $125,000,000\text{ ns}$ | $18,500,000\text{ ns}$ | $6.75\times$ |