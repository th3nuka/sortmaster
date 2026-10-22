package com.sortmaster.algorithm;

import com.sortmaster.exception.InvalidSortRequestException;
import com.sortmaster.model.AlgorithmMetadata;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class CountingSort extends AbstractSortingAlgorithm {

    public static final String ID = "counting-sort";
    private static final int MAX_SUPPORTED_RANGE = 100_000;

    @Override
    public String getId() {
        return ID;
    }

    @Override
    protected void executeSort(int[] array, String detailLevel) {
        int n = array.length;
        if (n <= 1) return;

        int min = array[0];
        int max = array[0];
        for (int val : array) {
            if (val < min) min = val;
            if (val > max) max = val;
        }

        long range = (long) max - min + 1;
        if (range > MAX_SUPPORTED_RANGE) {
            throw new InvalidSortRequestException("Counting Sort input range (" + range + ") exceeds maximum safe range (" + MAX_SUPPORTED_RANGE + ") to protect server memory.");
        }

        emitStart(array, "Scanning input range: minimum = " + min + ", maximum = " + max + " (range size = " + range + ") in " + order.getDisplayName().toLowerCase() + " order.");

        int[] count = new int[(int) range];

        // 1. Frequency count
        for (int i = 0; i < n; i++) {
            count[array[i] - min]++;
            emitCompare(array, i, i,
                    "Counted frequency of element " + array[i] + " (normalized index " + (array[i] - min) + ").",
                    "frequency_count");
        }

        // 2. Cumulative count for stability
        if (isAscending()) {
            for (int i = 1; i < count.length; i++) {
                count[i] += count[i - 1];
            }
        } else {
            for (int i = count.length - 2; i >= 0; i--) {
                count[i] += count[i + 1];
            }
        }

        // 3. Build output array in reverse to ensure stability
        int[] output = new int[n];
        for (int i = n - 1; i >= 0; i--) {
            int currentVal = array[i];
            int countIdx = currentVal - min;
            int targetPos = count[countIdx] - 1;
            output[targetPos] = currentVal;
            count[countIdx]--;
        }

        // 4. Write back to original array with visual events
        for (int i = 0; i < n; i++) {
            array[i] = output[i];
            emitWrite(array, i, output[i],
                    "Writing sorted value " + output[i] + " into position " + i + ".",
                    "write_output");
            emitMarkSorted(array, List.of(i),
                    "Position " + i + " is now sorted in " + order.getDisplayName().toLowerCase() + " order.",
                    "mark_sorted");
        }
    }

    @Override
    public AlgorithmMetadata getMetadata() {
        return new AlgorithmMetadata(
                ID,
                "Counting Sort",
                "Non-Comparison / Distribution",
                "An integer sorting algorithm that operates by counting the number of objects possessing each distinct key value.",
                "Counting Sort is a non-comparison based integer sorting algorithm. Rather than comparing elements against each other, it counts the occurrences of each unique integer in a frequency array. By calculating prefix sums of these frequencies, it determines the exact final position of every element in O(n + k) time, where k is the range of inputs. This implementation includes an offset for negative integers and validates bounds to prevent memory overflow.",
                "O(n + k)",
                "O(n + k)",
                "O(n + k)",
                "O(n + k)",
                true,
                false,
                "Medium",
                List.of(
                        "Datasets with small integer ranges (k = O(n))",
                        "Subroutine inside Radix Sort (where radix digits are in range 0..9 or 0..255)",
                        "Sorting test scores, ages, or census discrete categories"
                ),
                List.of(
                        "Linear O(n + k) time complexity beats the theoretical O(n log n) comparison lower bound",
                        "Completely stable when implemented using cumulative prefix sum backwards iteration",
                        "Simple and predictable deterministic runtime"
                ),
                List.of(
                        "Not a comparison sort; cannot sort general objects without numeric keys",
                        "Requires excessive O(k) memory if the difference between maximum and minimum is huge (e.g., sorting [1, 10000000])",
                        "Not in-place: requires auxiliary frequency and output arrays"
                ),
                List.of(
                        "Failing to support negative numbers by omitting the minimum offset shift",
                        "Iterating forward instead of backward when generating the output array, breaking stability",
                        "Memory exhaustion when sorting small arrays containing very large integer values"
                ),
                List.of(
                        "Why does Counting Sort violate the Ω(n log n) lower bound for comparison sorts?",
                        "Under what condition does Counting Sort become slower than Quick Sort or Merge Sort? (Answer: When k >> n log n)",
                        "How does prefix sum accumulation guarantee algorithm stability?"
                ),
                """
                procedure countingSort(A)
                    min = min(A), max = max(A)
                    range = max - min + 1
                    count = array of size range initialized to 0
                    output = array of size length(A)

                    for x in A do
                        count[x - min] = count[x - min] + 1
                    end for

                    for i = 1 to range - 1 do
                        count[i] = count[i] + count[i - 1]
                    end for

                    for i = length(A) - 1 down to 0 do
                        val = A[i]
                        output[count[val - min] - 1] = val
                        count[val - min] = count[val - min] - 1
                    end for

                    copy output back to A
                end procedure
                """,
                """
                public void countingSort(int[] arr) {
                    if (arr.length <= 1) return;
                    int min = arr[0], max = arr[0];
                    for (int x : arr) {
                        if (x < min) min = x;
                        if (x > max) max = x;
                    }
                    int range = max - min + 1;
                    int[] count = new int[range];
                    int[] output = new int[arr.length];

                    for (int x : arr) count[x - min]++;
                    for (int i = 1; i < range; i++) count[i] += count[i - 1];
                    for (int i = arr.length - 1; i >= 0; i--) {
                        output[count[arr[i] - min] - 1] = arr[i];
                        count[arr[i] - min]--;
                    }
                    System.arraycopy(output, 0, arr, 0, arr.length);
                }
                """,
                """
                def counting_sort(arr: list[int]) -> list[int]:
                    if len(arr) <= 1:
                        return arr
                    min_val, max_val = min(arr), max(arr)
                    val_range = max_val - min_val + 1
                    if val_range > 100_000:
                        raise ValueError("Range exceeds safe memory limit for Counting Sort.")
                    count = [0] * val_range
                    output = [0] * len(arr)
                    for x in arr:
                        count[x - min_val] += 1
                    for i in range(1, val_range):
                        count[i] += count[i - 1]
                    for i in range(len(arr) - 1, -1, -1):
                        output[count[arr[i] - min_val] - 1] = arr[i]
                        count[arr[i] - min_val] -= 1
                    for i in range(len(arr)):
                        arr[i] = output[i]
                    return arr
                """,
                """
                procedure countingSortDescending(A)
                    min = min(A), max = max(A)
                    range = max - min + 1
                    count = array of size range initialized to 0
                    output = array of size length(A)

                    for x in A do
                        count[x - min] = count[x - min] + 1
                    end for

                    for i = range - 2 down to 0 do
                        count[i] = count[i] + count[i + 1]
                    end for

                    for i = length(A) - 1 down to 0 do
                        val = A[i]
                        output[count[val - min] - 1] = val
                        count[val - min] = count[val - min] - 1
                    end for

                    copy output back to A
                end procedure
                """,
                """
                public void countingSortDescending(int[] arr) {
                    if (arr.length <= 1) return;
                    int min = arr[0], max = arr[0];
                    for (int x : arr) {
                        if (x < min) min = x;
                        if (x > max) max = x;
                    }
                    int range = max - min + 1;
                    int[] count = new int[range];
                    int[] output = new int[arr.length];

                    for (int x : arr) count[x - min]++;
                    for (int i = range - 2; i >= 0; i--) count[i] += count[i + 1];
                    for (int i = arr.length - 1; i >= 0; i--) {
                        output[count[arr[i] - min] - 1] = arr[i];
                        count[arr[i] - min]--;
                    }
                    System.arraycopy(output, 0, arr, 0, arr.length);
                }
                """,
                """
                def counting_sort_descending(arr: list[int]) -> list[int]:
                    if len(arr) <= 1:
                        return arr
                    min_val, max_val = min(arr), max(arr)
                    val_range = max_val - min_val + 1
                    if val_range > 100_000:
                        raise ValueError("Range exceeds safe memory limit for Counting Sort.")
                    count = [0] * val_range
                    output = [0] * len(arr)
                    for x in arr:
                        count[x - min_val] += 1
                    for i in range(val_range - 2, -1, -1):
                        count[i] += count[i + 1]
                    for i in range(len(arr) - 1, -1, -1):
                        output[count[arr[i] - min_val] - 1] = arr[i]
                        count[arr[i] - min_val] -= 1
                    for i in range(len(arr)):
                        arr[i] = output[i]
                    return arr
                """,
                Map.of(
                        "frequency_count", "Increment the tally counter for the current integer value.",
                        "write_output", "Place the value into its exact predetermined index from the cumulative frequency map.",
                        "mark_sorted", "Mark the element as fixed in its final sorted position."
                )
        );
    }
}
