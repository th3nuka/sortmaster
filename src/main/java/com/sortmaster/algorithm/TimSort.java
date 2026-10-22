package com.sortmaster.algorithm;

import com.sortmaster.model.AlgorithmMetadata;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class TimSort extends AbstractSortingAlgorithm {

    public static final String ID = "tim-sort";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    protected void executeSort(int[] array, String detailLevel) {
        int n = array.length;
        if (n <= 1) return;

        // Compute adaptive minRun: for visualization, pick minRun between 4 and 32
        int minRun = calculateMinRun(n);
        emitStart(array, "Calculated adaptive minRun = " + minRun + " for input size " + n + ".");

        List<int[]> runs = new ArrayList<>(); // Store [start, length]

        int i = 0;
        while (i < n) {
            int runStart = i;
            // 1. Detect natural run according to sorting order
            int runEnd = i + 1;
            if (runEnd < n) {
                if (isAscending()) {
                    if (compare(array, runEnd - 1, runEnd, "detect_run") <= 0) {
                        // Ascending run
                        while (runEnd < n && array[runEnd - 1] <= array[runEnd]) {
                            runEnd++;
                        }
                    } else {
                        // Strictly descending run: reverse it to ascending
                        while (runEnd < n && array[runEnd - 1] > array[runEnd]) {
                            runEnd++;
                        }
                        reverseRange(array, runStart, runEnd - 1);
                        emitStart(array, "Reversed strictly descending natural run [" + runStart + ".." + (runEnd - 1) + "].");
                    }
                } else {
                    if (compare(array, runEnd - 1, runEnd, "detect_run") >= 0) {
                        // Descending run
                        while (runEnd < n && array[runEnd - 1] >= array[runEnd]) {
                            runEnd++;
                        }
                    } else {
                        // Strictly ascending run: reverse it to descending
                        while (runEnd < n && array[runEnd - 1] < array[runEnd]) {
                            runEnd++;
                        }
                        reverseRange(array, runStart, runEnd - 1);
                        emitStart(array, "Reversed strictly ascending natural run [" + runStart + ".." + (runEnd - 1) + "].");
                    }
                }
            }

            int runLength = runEnd - runStart;
            emitPivot(array, runStart, new int[]{runStart, runEnd - 1},
                    "Detected natural " + getOrderName().toLowerCase() + " run of length " + runLength + " at [" + runStart + ".." + (runEnd - 1) + "].",
                    "natural_run");

            // 2. If run length < minRun, extend it with Insertion Sort
            if (runLength < minRun && runStart + minRun <= n) {
                int extendEnd = Math.min(n - 1, runStart + minRun - 1);
                emitStart(array, "Run length " + runLength + " < minRun (" + minRun + "). Extending run to index " + extendEnd + " via " + getOrderName() + " Insertion Sort.");
                insertionSortRange(array, runStart, extendEnd);
                runLength = extendEnd - runStart + 1;
            } else if (runLength < minRun) {
                // At array tail
                insertionSortRange(array, runStart, n - 1);
                runLength = n - runStart;
            }

            runs.add(new int[]{runStart, runLength});
            i = runStart + runLength;
        }

        emitStart(array, "Phase 1 complete: Built " + runs.size() + " sorted runs (" + getOrderName().toLowerCase() + "). Beginning multi-run merging phase.");

        // 3. Merge runs pairwise until only 1 run remains
        while (runs.size() > 1) {
            List<int[]> nextRuns = new ArrayList<>();
            for (int r = 0; r < runs.size(); r += 2) {
                if (r + 1 < runs.size()) {
                    int[] runA = runs.get(r);
                    int[] runB = runs.get(r + 1);
                    mergeRuns(array, runA[0], runA[1], runB[0], runB[1]);
                    nextRuns.add(new int[]{runA[0], runA[1] + runB[1]});
                } else {
                    nextRuns.add(runs.get(r));
                }
            }
            runs = nextRuns;
        }
    }

    private int calculateMinRun(int n) {
        if (n <= 16) return Math.max(4, n / 2);
        int r = 0;
        int temp = n;
        while (temp >= 32) {
            r |= (temp & 1);
            temp >>= 1;
        }
        return temp + r;
    }

    private void reverseRange(int[] array, int start, int end) {
        while (start < end) {
            swap(array, start, end, "reverse_run");
            start++;
            end--;
        }
    }

    private void insertionSortRange(int[] array, int start, int end) {
        for (int i = start + 1; i <= end; i++) {
            int key = array[i];
            int j = i - 1;
            while (j >= start) {
                emitCompare(array, j, j + 1,
                        "TimSort run insertion: comparing index " + j + " (" + array[j] + ") with key (" + key + ").",
                        "insertion_compare");
                if (isOutOfOrder(array[j], key)) {
                    array[j + 1] = array[j];
                    emitWrite(array, j + 1, array[j],
                            "TimSort run insertion: shifted " + array[j] + " to index " + (j + 1) + ".",
                            "insertion_shift");
                    j--;
                } else {
                    break;
                }
            }
            array[j + 1] = key;
            emitWrite(array, j + 1, key,
                    "TimSort run insertion: inserted key " + key + " at index " + (j + 1) + ".",
                    "insertion_place");
        }
    }

    private void mergeRuns(int[] array, int startA, int lenA, int startB, int lenB) {
        int left = startA;
        int mid = startA + lenA - 1;
        int right = startB + lenB - 1;

        emitMerge(array, left, mid, right,
                "Merging TimSort run A [" + left + ".." + mid + "] with run B [" + (mid + 1) + ".." + right + "] (" + getOrderName().toLowerCase() + ").",
                "merge_runs");

        int[] L = new int[lenA];
        int[] R = new int[lenB];

        System.arraycopy(array, startA, L, 0, lenA);
        System.arraycopy(array, startB, R, 0, lenB);

        int i = 0, j = 0, k = startA;
        while (i < lenA && j < lenB) {
            emitCompare(array, startA + i, startB + j,
                    "Comparing element " + L[i] + " from Run A with element " + R[j] + " from Run B.",
                    "compare_merge");
            if (isBeforeOrEqual(L[i], R[j])) {
                array[k] = L[i];
                emitWrite(array, k, L[i], "Placed " + L[i] + " from Run A.", "write_merge");
                i++;
            } else {
                array[k] = R[j];
                emitWrite(array, k, R[j], "Placed " + R[j] + " from Run B.", "write_merge");
                j++;
            }
            k++;
        }

        while (i < lenA) {
            array[k] = L[i];
            emitWrite(array, k, L[i], "Placed remaining element " + L[i] + " from Run A.", "write_merge");
            i++;
            k++;
        }

        while (j < lenB) {
            array[k] = R[j];
            emitWrite(array, k, R[j], "Placed remaining element " + R[j] + " from Run B.", "write_merge");
            j++;
            k++;
        }

        List<Integer> sortedPart = new ArrayList<>();
        for (int idx = left; idx <= right; idx++) {
            sortedPart.add(idx);
        }
        emitMarkSorted(array, sortedPart,
                "Merged runs [" + left + ".." + right + "] are now sorted (" + getOrderName().toLowerCase() + ").",
                "mark_sorted");
    }

    @Override
    public AlgorithmMetadata getMetadata() {
        return new AlgorithmMetadata(
                ID,
                "Tim Sort",
                "Hybrid (Insertion + Merge)",
                "A hybrid, stable sorting algorithm derived from Merge Sort and Insertion Sort, designed to perform exceptionally well on real-world data.",
                "Created by Tim Peters in 2002 for the Python language and subsequently adopted as the default sorting algorithm for non-primitive objects in Java (Arrays.sort and Collections.sort) and Android. Tim Sort recognizes that real-world datasets frequently contain preexisting ascending or descending sequences ('natural runs'). It detects these runs, extends short runs up to a minimum threshold ('minRun') using Insertion Sort, and merges runs hierarchically using a stack, achieving O(n) performance on partially ordered data.",
                "O(n)",
                "O(n log n)",
                "O(n log n)",
                "O(n)",
                true,
                false,
                "Hard",
                List.of(
                        "Default general-purpose sorting for Java Objects (Arrays.sort(Object[]), Collections.sort())",
                        "Default sorting engine in Python (list.sort() and sorted())",
                        "Real-world data containing preexisting sorted clusters or streaks",
                        "Systems where stability and guaranteed O(n log n) worst-case time are non-negotiable"
                ),
                List.of(
                        "Outstanding real-world performance by exploiting natural data order",
                        "Adaptive O(n) best-case execution on already sorted or nearly sorted lists",
                        "Completely stable: equal elements preserve original sequence",
                        "Guaranteed O(n log n) worst-case upper bound"
                ),
                List.of(
                        "Complex implementation with intricate run stack invariants",
                        "Requires auxiliary memory buffer (O(n) worst case) for merging",
                        "Higher constant overhead than Quick Sort on purely randomized primitive data"
                ),
                List.of(
                        "Famous historic bug in the original Tim Sort run-stack collapse invariant that took over a decade to discover via formal verification",
                        "Reversing descending runs without preserving stability if non-strict inequality is used",
                        "Failing to calculate minRun such that n/minRun is close to a power of 2"
                ),
                List.of(
                        "Why is Tim Sort the default sorting algorithm for Java objects but not for primitive arrays? (Answer: Objects require stability; primitives use Dual-Pivot QuickSort)",
                        "What is a 'natural run' in Tim Sort, and how are descending runs handled?",
                        "How does Tim Sort balance runs using the stack invariant to ensure O(n log n) merge efficiency?"
                ),
                """
                procedure timSort(A)
                    n = length(A)
                    minRun = calculateMinRun(n)
                    for i = 0 to n-1 step minRun do
                        insertionSort(A, i, min(i + minRun - 1, n - 1))
                    end for
                    size = minRun
                    while size < n do
                        for left = 0 to n - 1 step 2 * size do
                            mid = left + size - 1
                            right = min(left + 2 * size - 1, n - 1)
                            if mid < right then
                                merge(A, left, mid, right)
                            end if
                        end for
                        size = 2 * size
                    end while
                end procedure
                """,
                """
                public void timSort(int[] arr) {
                    int n = arr.length;
                    int minRun = minRunLength(n);
                    for (int i = 0; i < n; i += minRun) {
                        insertionSort(arr, i, Math.min(i + minRun - 1, n - 1));
                    }
                    for (int size = minRun; size < n; size = 2 * size) {
                        for (int left = 0; left < n; left += 2 * size) {
                            int mid = left + size - 1;
                            int right = Math.min(left + 2 * size - 1, n - 1);
                            if (mid < right) {
                                merge(arr, left, mid, right);
                            }
                        }
                    }
                }
                """,
                """
                def min_run_length(n: int) -> int:
                    r = 0
                    while n >= 32:
                        r |= n & 1
                        n >>= 1
                    return n + r

                def insertion_sort_slice(arr: list[int], left: int, right: int) -> None:
                    for i in range(left + 1, right + 1):
                        key = arr[i]
                        j = i - 1
                        while j >= left and arr[j] > key:
                            arr[j + 1] = arr[j]
                            j -= 1
                        arr[j + 1] = key

                def merge_slices(arr: list[int], left: int, mid: int, right: int) -> None:
                    left_part = arr[left:mid + 1]
                    right_part = arr[mid + 1:right + 1]
                    i = j = 0
                    k = left
                    while i < len(left_part) and j < len(right_part):
                        if left_part[i] <= right_part[j]:
                            arr[k] = left_part[i]
                            i += 1
                        else:
                            arr[k] = right_part[j]
                            j += 1
                        k += 1
                    while i < len(left_part):
                        arr[k] = left_part[i]
                        i += 1
                        k += 1
                    while j < len(right_part):
                        arr[k] = right_part[j]
                        j += 1
                        k += 1

                def tim_sort(arr: list[int]) -> list[int]:
                    n = len(arr)
                    min_run = min_run_length(n)
                    for start in range(0, n, min_run):
                        end = min(start + min_run - 1, n - 1)
                        insertion_sort_slice(arr, start, end)
                    size = min_run
                    while size < n:
                        for left in range(0, n, 2 * size):
                            mid = min(n - 1, left + size - 1)
                            right = min(left + 2 * size - 1, n - 1)
                            if mid < right:
                                merge_slices(arr, left, mid, right)
                        size *= 2
                    return arr
                """,
                """
                procedure timSortDescending(A)
                    n = length(A)
                    minRun = calculateMinRun(n)
                    for i = 0 to n-1 step minRun do
                        insertionSortDescending(A, i, min(i + minRun - 1, n - 1))
                    end for
                    size = minRun
                    while size < n do
                        for left = 0 to n - 1 step 2 * size do
                            mid = left + size - 1
                            right = min(left + 2 * size - 1, n - 1)
                            if mid < right then
                                mergeDescending(A, left, mid, right)
                            end if
                        end for
                        size = 2 * size
                    end while
                end procedure
                """,
                """
                public void timSortDescending(int[] arr) {
                    int n = arr.length;
                    int minRun = minRunLength(n);
                    for (int i = 0; i < n; i += minRun) {
                        insertionSortDescending(arr, i, Math.min(i + minRun - 1, n - 1));
                    }
                    for (int size = minRun; size < n; size = 2 * size) {
                        for (int left = 0; left < n; left += 2 * size) {
                            int mid = left + size - 1;
                            int right = Math.min(left + 2 * size - 1, n - 1);
                            if (mid < right) {
                                mergeDescending(arr, left, mid, right);
                            }
                        }
                    }
                }
                """,
                """
                def min_run_length(n: int) -> int:
                    r = 0
                    while n >= 32:
                        r |= n & 1
                        n >>= 1
                    return n + r

                def insertion_sort_slice_descending(arr: list[int], left: int, right: int) -> None:
                    for i in range(left + 1, right + 1):
                        key = arr[i]
                        j = i - 1
                        while j >= left and arr[j] < key:
                            arr[j + 1] = arr[j]
                            j -= 1
                        arr[j + 1] = key

                def merge_slices_descending(arr: list[int], left: int, mid: int, right: int) -> None:
                    left_part = arr[left:mid + 1]
                    right_part = arr[mid + 1:right + 1]
                    i = j = 0
                    k = left
                    while i < len(left_part) and j < len(right_part):
                        if left_part[i] >= right_part[j]:
                            arr[k] = left_part[i]
                            i += 1
                        else:
                            arr[k] = right_part[j]
                            j += 1
                        k += 1
                    while i < len(left_part):
                        arr[k] = left_part[i]
                        i += 1
                        k += 1
                    while j < len(right_part):
                        arr[k] = right_part[j]
                        j += 1
                        k += 1

                def tim_sort_descending(arr: list[int]) -> list[int]:
                    n = len(arr)
                    min_run = min_run_length(n)
                    for start in range(0, n, min_run):
                        end = min(start + min_run - 1, n - 1)
                        insertion_sort_slice_descending(arr, start, end)
                    size = min_run
                    while size < n:
                        for left in range(0, n, 2 * size):
                            mid = min(n - 1, left + size - 1)
                            right = min(left + 2 * size - 1, n - 1)
                            if mid < right:
                                merge_slices_descending(arr, left, mid, right)
                        size *= 2
                    return arr
                """,
                Map.of(
                        "natural_run", "Identify naturally occurring monotonic runs in the dataset.",
                        "reverse_run", "Reverse strictly decreasing natural runs to ascending order.",
                        "insertion_place", "Use insertion sort to extend runs that are smaller than minRun.",
                        "merge_runs", "Merge adjacent runs together stably into a single ordered run."
                )
        );
    }
}
