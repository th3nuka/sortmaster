package com.sortmaster.algorithm;

import com.sortmaster.model.AlgorithmMetadata;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class RadixSort extends AbstractSortingAlgorithm {

    public static final String ID = "radix-sort";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    protected void executeSort(int[] array, String detailLevel) {
        int n = array.length;
        if (n <= 1) return;

        // Find min and max
        int min = array[0];
        int max = array[0];
        for (int val : array) {
            if (val < min) min = val;
            if (val > max) max = val;
        }

        long minVal = min;
        long maxShifted = (long) max - minVal;

        emitStart(array, "Radix Sort (LSD) starting in " + order.getDisplayName().toLowerCase() + " order on " + n + " elements (range = " + maxShifted + ").");

        for (long exp = 1; maxShifted / exp > 0; exp *= 10) {
            countSortByDigit(array, exp, minVal);
        }
    }

    private void countSortByDigit(int[] array, long exp, long minVal) {
        int n = array.length;
        int[] output = new int[n];
        int[] count = new int[10];

        // 1. Frequency of digits
        for (int i = 0; i < n; i++) {
            long shifted = (long) array[i] - minVal;
            int digit = (int) ((shifted / exp) % 10);
            count[digit]++;
            emitCompare(array, i, i,
                    "Inspecting radix digit (" + digit + ") at place " + exp + " for element " + array[i] + ".",
                    "read_digit");
        }

        // 2. Cumulative count based on active order
        if (isAscending()) {
            for (int i = 1; i < 10; i++) {
                count[i] += count[i - 1];
            }
        } else {
            for (int i = 8; i >= 0; i--) {
                count[i] += count[i + 1];
            }
        }

        // 3. Build output array stably
        for (int i = n - 1; i >= 0; i--) {
            long shifted = (long) array[i] - minVal;
            int digit = (int) ((shifted / exp) % 10);
            output[count[digit] - 1] = array[i];
            count[digit]--;
        }

        // 4. Write output back to array with animated write events
        for (int i = 0; i < n; i++) {
            array[i] = output[i];
            emitWrite(array, i, output[i],
                    "Placed element " + output[i] + " at index " + i + " based on radix position " + exp + ".",
                    "write_digit");
        }
    }

    @Override
    public AlgorithmMetadata getMetadata() {
        return new AlgorithmMetadata(
                ID,
                "Radix Sort",
                "Non-Comparison / Distribution",
                "Sorts numbers by processing individual digits, either from least significant digit (LSD) or most significant digit (MSD).",
                "Radix Sort sorts integer data by distributing elements into buckets according to their individual radix digits, beginning at the Least Significant Digit (LSD) and progressing to the Most Significant Digit (MSD). At each digit position, a stable sorting algorithm (typically Counting Sort) sorts the elements. By chaining stable passes, earlier sorted digits maintain their order. This implementation segregates negative numbers to seamlessly support all signed 32-bit integers in both ascending and descending modes.",
                "O(d · (n + k))",
                "O(d · (n + k))",
                "O(d · (n + k))",
                "O(n + k)",
                true,
                false,
                "Medium",
                List.of(
                        "Sorting large collections of fixed-length integers or fixed-width strings",
                        "Card sorting machines (historical origin of the algorithm)",
                        "When data key length d is much smaller than log(n)"
                ),
                List.of(
                        "Linear O(d(n + k)) time complexity when key length d is constant",
                        "Completely stable sorting preserves original order of identical keys",
                        "Avoids comparison operations altogether"
                ),
                List.of(
                        "Not in-place: requires O(n + k) auxiliary memory for output and bucket storage",
                        "Significant constant factors can make it slower than Quick Sort for small/medium arrays",
                        "Tied to fixed-width integer or string keys; less flexible for arbitrary data types"
                ),
                List.of(
                        "Omitting support for negative numbers, corrupting order of signed integer inputs",
                        "Iterating digits from MSD to LSD without recursion, destroying sorted lower digits",
                        "Using an unstable sub-sorting method for individual digit passes"
                ),
                List.of(
                        "What is the difference between LSD (Least Significant Digit) and MSD (Most Significant Digit) Radix Sort?",
                        "Under what mathematical conditions will Radix Sort execute faster than an optimal comparison sort?",
                        "How does Radix Sort preserve stability across multiple digit passes?"
                ),
                """
                procedure radixSort(A)
                    maxVal = max(A)
                    exp = 1
                    while maxVal / exp > 0 do
                        countingSortByDigit(A, exp)
                        exp = exp * 10
                    end while
                end procedure
                """,
                """
                public void radixSort(int[] arr) {
                    if (arr.length <= 1) return;
                    int max = Arrays.stream(arr).map(Math::abs).max().orElse(0);
                    for (int exp = 1; max / exp > 0; exp *= 10) {
                        countSortByDigit(arr, exp);
                    }
                }
                """,
                """
                def radix_sort(arr: list[int]) -> list[int]:
                    if len(arr) <= 1:
                        return arr
                    positives = [x for x in arr if x >= 0]
                    negatives = [-x for x in arr if x < 0]

                    def lsd_sort(a: list[int]) -> list[int]:
                        if not a:
                            return a
                        max_val = max(a)
                        exp = 1
                        while max_val // exp > 0:
                            count = [0] * 10
                            out = [0] * len(a)
                            for x in a:
                                count[(x // exp) % 10] += 1
                            for i in range(1, 10):
                                count[i] += count[i - 1]
                            for i in range(len(a) - 1, -1, -1):
                                d = (a[i] // exp) % 10
                                out[count[d] - 1] = a[i]
                                count[d] -= 1
                            a = out
                            exp *= 10
                        return a

                    sorted_pos = lsd_sort(positives)
                    sorted_neg = lsd_sort(negatives)
                    return [-x for x in reversed(sorted_neg)] + sorted_pos
                """,
                """
                procedure radixSortDescending(A)
                    maxVal = max(A)
                    exp = 1
                    while maxVal / exp > 0 do
                        countingSortByDigitDescending(A, exp)
                        exp = exp * 10
                    end while
                end procedure
                """,
                """
                public void radixSortDescending(int[] arr) {
                    if (arr.length <= 1) return;
                    int max = Arrays.stream(arr).map(Math::abs).max().orElse(0);
                    for (int exp = 1; max / exp > 0; exp *= 10) {
                        countSortByDigitDescending(arr, exp);
                    }
                }
                """,
                """
                def radix_sort_descending(arr: list[int]) -> list[int]:
                    if len(arr) <= 1:
                        return arr
                    positives = [x for x in arr if x >= 0]
                    negatives = [-x for x in arr if x < 0]

                    def lsd_sort_desc(a: list[int]) -> list[int]:
                        if not a:
                            return a
                        max_val = max(a)
                        exp = 1
                        while max_val // exp > 0:
                            count = [0] * 10
                            out = [0] * len(a)
                            for x in a:
                                count[(x // exp) % 10] += 1
                            for i in range(8, -1, -1):
                                count[i] += count[i + 1]
                            for i in range(len(a) - 1, -1, -1):
                                d = (a[i] // exp) % 10
                                out[count[d] - 1] = a[i]
                                count[d] -= 1
                            a = out
                            exp *= 10
                        return a

                    def lsd_sort_asc(a: list[int]) -> list[int]:
                        if not a:
                            return a
                        max_val = max(a)
                        exp = 1
                        while max_val // exp > 0:
                            count = [0] * 10
                            out = [0] * len(a)
                            for x in a:
                                count[(x // exp) % 10] += 1
                            for i in range(1, 10):
                                count[i] += count[i - 1]
                            for i in range(len(a) - 1, -1, -1):
                                d = (a[i] // exp) % 10
                                out[count[d] - 1] = a[i]
                                count[d] -= 1
                            a = out
                            exp *= 10
                        return a

                    sorted_pos = lsd_sort_desc(positives)
                    sorted_neg = lsd_sort_asc(negatives)
                    return sorted_pos + [-x for x in sorted_neg]
                """,
                Map.of(
                        "write_combined", "Reassemble element into final array position.",
                        "mark_sorted", "Mark element as fixed in sorted position."
                )
        );
    }
}
