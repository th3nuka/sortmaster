package com.sortmaster.algorithm;

import com.sortmaster.model.AlgorithmMetadata;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class CycleSort extends AbstractSortingAlgorithm {

    public static final String ID = "cycle-sort";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    protected void executeSort(int[] array, String detailLevel) {
        int n = array.length;
        String countDesc = isAscending() ? "smaller" : "greater";

        // Loop through the array to find cycles to rotate
        for (int cycleStart = 0; cycleStart <= n - 2; cycleStart++) {
            int item = array[cycleStart];
            int pos = cycleStart;

            emitPivot(array, cycleStart, new int[]{cycleStart, n - 1},
                    "Starting new cycle from index " + cycleStart + " with item = " + item + " in " + order.getDisplayName().toLowerCase() + " order.",
                    "start_cycle");

            // Find where to put the item by counting all out-of-order elements
            for (int i = cycleStart + 1; i < n; i++) {
                emitCompare(array, i, cycleStart,
                        "Counting elements " + countDesc + " than item " + item + " in range [" + (cycleStart + 1) + ".." + (n - 1) + "].",
                        "count_smaller");
                boolean condition = isAscending() ? (array[i] < item) : (array[i] > item);
                if (condition) {
                    pos++;
                }
            }

            // If item is already in correct position
            if (pos == cycleStart) {
                emitMarkSorted(array, List.of(cycleStart),
                        "Item " + item + " is already in its correct position at index " + cycleStart + ".",
                        "already_placed");
                continue;
            }

            // Ignore all duplicate elements
            while (item == array[pos]) {
                pos++;
            }

            // Put the item to its right position
            if (pos != cycleStart) {
                int temp = item;
                item = array[pos];
                array[pos] = temp;
                emitWrite(array, pos, temp,
                        "Writing item " + temp + " into its correct cycle position at index " + pos + " (displacing " + item + ").",
                        "write_cycle");
                emitMarkSorted(array, List.of(pos),
                        "Index " + pos + " placed.",
                        "mark_sorted");
            }

            // Rotate rest of the cycle
            while (pos != cycleStart) {
                pos = cycleStart;

                // Find where to put the displaced item
                for (int i = cycleStart + 1; i < n; i++) {
                    emitCompare(array, i, cycleStart,
                            "Counting elements " + countDesc + " than displaced item " + item + ".",
                            "count_smaller");
                    boolean condition = isAscending() ? (array[i] < item) : (array[i] > item);
                    if (condition) {
                        pos++;
                    }
                }

                // Ignore all duplicate elements
                while (item == array[pos]) {
                    pos++;
                }

                // Put item to its right position
                if (item != array[pos]) {
                    int temp = item;
                    item = array[pos];
                    array[pos] = temp;
                    emitWrite(array, pos, temp,
                            "Writing displaced item " + temp + " into resolved position " + pos + ".",
                            "write_cycle");
                    emitMarkSorted(array, List.of(pos),
                            "Index " + pos + " placed.",
                            "mark_sorted");
                }
            }
        }
    }

    @Override
    public AlgorithmMetadata getMetadata() {
        return new AlgorithmMetadata(
                ID,
                "Cycle Sort",
                "Selection / Cycle Decomposition",
                "An in-place sorting algorithm that minimizes the number of memory writes, theoretically writing to memory at most O(n) times.",
                "Cycle Sort is based on the idea that permutations can be factored into disjoint cyclic permutations. For each element, it calculates its exact final position in the sorted array by counting how many elements are smaller (or greater for descending) than it, then deposits it there, displacing whatever element was previously residing at that slot. The displaced element is similarly routed to its correct location until the cycle completes. It is theoretically optimal in minimizing write operations.",
                "O(n²)",
                "O(n²)",
                "O(n²)",
                "O(1)",
                false,
                true,
                "Hard",
                List.of(
                        "Flash memory, EEPROM, and SSD storage where write wear degradation is a critical concern",
                        "Embedded sensors with constrained write energy budgets",
                        "Theoretical computer science studies on minimum-write permutations"
                ),
                List.of(
                        "Theoretically optimal write count: each item is written at most once if already in place, or minimal cycle hops",
                        "In-place sorting requiring strictly O(1) auxiliary space",
                        "No complex data structure or recursive call stack overhead"
                ),
                List.of(
                        "O(n²) time complexity across all cases due to counting comparisons",
                        "Unstable algorithm",
                        "Complex logic to handle duplicate keys without infinite looping"
                ),
                List.of(
                        "Infinite loops caused by duplicate elements without skipping `while (item == array[pos]) pos++`",
                        "Failing to close the cycle when pos returns to cycleStart",
                        "Confusing Cycle Sort with counting sort"
                ),
                List.of(
                        "Why is Cycle Sort optimal in terms of memory writes?",
                        "What is the maximum number of memory writes performed by Cycle Sort on an array of size n? (Answer: n - 1 writes)",
                        "How are duplicate keys handled to guarantee termination in Cycle Sort?"
                ),
                """
                procedure cycleSort(A)
                    n = length(A)
                    for cycleStart = 0 to n - 2 do
                        item = A[cycleStart]
                        pos = cycleStart
                        for i = cycleStart + 1 to n - 1 do
                            if A[i] < item then pos = pos + 1
                        end for
                        if pos == cycleStart then continue
                        while item == A[pos] do pos = pos + 1
                        swap(item, A[pos])
                        while pos != cycleStart do
                            pos = cycleStart
                            for i = cycleStart + 1 to n - 1 do
                                if A[i] < item then pos = pos + 1
                            end for
                            while item == A[pos] do pos = pos + 1
                            swap(item, A[pos])
                        end while
                    end for
                end procedure
                """,
                """
                public void cycleSort(int[] arr) {
                    int n = arr.length;
                    for (int cycleStart = 0; cycleStart <= n - 2; cycleStart++) {
                        int item = arr[cycleStart];
                        int pos = cycleStart;
                        for (int i = cycleStart + 1; i < n; i++) {
                            if (arr[i] < item) pos++;
                        }
                        if (pos == cycleStart) continue;
                        while (item == arr[pos]) pos++;
                        int temp = item; item = arr[pos]; arr[pos] = temp;
                        while (pos != cycleStart) {
                            pos = cycleStart;
                            for (int i = cycleStart + 1; i < n; i++) {
                                if (arr[i] < item) pos++;
                            }
                            while (item == arr[pos]) pos++;
                            temp = item; item = arr[pos]; arr[pos] = temp;
                        }
                    }
                }
                """,
                """
                def cycle_sort(arr: list[int]) -> list[int]:
                    n = len(arr)
                    for cycle_start in range(0, n - 1):
                        item = arr[cycle_start]
                        pos = cycle_start
                        for i in range(cycle_start + 1, n):
                            if arr[i] < item:
                                pos += 1
                        if pos == cycle_start:
                            continue
                        while item == arr[pos]:
                            pos += 1
                        arr[pos], item = item, arr[pos]
                        while pos != cycle_start:
                            pos = cycle_start
                            for i in range(cycle_start + 1, n):
                                if arr[i] < item:
                                pos += 1
                            while item == arr[pos]:
                                pos += 1
                            arr[pos], item = item, arr[pos]
                    return arr
                """,
                """
                procedure cycleSortDescending(A)
                    n = length(A)
                    for cycleStart = 0 to n - 2 do
                        item = A[cycleStart]
                        pos = cycleStart
                        for i = cycleStart + 1 to n - 1 do
                            if A[i] > item then pos = pos + 1
                        end for
                        if pos == cycleStart then continue
                        while item == A[pos] do pos = pos + 1
                        swap(item, A[pos])
                        while pos != cycleStart do
                            pos = cycleStart
                            for i = cycleStart + 1 to n - 1 do
                                if A[i] > item then pos = pos + 1
                            end for
                            while item == A[pos] do pos = pos + 1
                            swap(item, A[pos])
                        end while
                    end for
                end procedure
                """,
                """
                public void cycleSortDescending(int[] arr) {
                    int n = arr.length;
                    for (int cycleStart = 0; cycleStart <= n - 2; cycleStart++) {
                        int item = arr[cycleStart];
                        int pos = cycleStart;
                        for (int i = cycleStart + 1; i < n; i++) {
                            if (arr[i] > item) pos++;
                        }
                        if (pos == cycleStart) continue;
                        while (item == arr[pos]) pos++;
                        int temp = item; item = arr[pos]; arr[pos] = temp;
                        while (pos != cycleStart) {
                            pos = cycleStart;
                            for (int i = cycleStart + 1; i < n; i++) {
                                if (arr[i] > item) pos++;
                            }
                            while (item == arr[pos]) pos++;
                            temp = item; item = arr[pos]; arr[pos] = temp;
                        }
                    }
                }
                """,
                """
                def cycle_sort_descending(arr: list[int]) -> list[int]:
                    n = len(arr)
                    for cycle_start in range(0, n - 1):
                        item = arr[cycle_start]
                        pos = cycle_start
                        for i in range(cycle_start + 1, n):
                            if arr[i] > item:
                                pos += 1
                        if pos == cycle_start:
                            continue
                        while item == arr[pos]:
                            pos += 1
                        arr[pos], item = item, arr[pos]
                        while pos != cycle_start:
                            pos = cycle_start
                            for i in range(cycle_start + 1, n):
                                if arr[i] > item:
                                    pos += 1
                            while item == arr[pos]:
                                pos += 1
                            arr[pos], item = item, arr[pos]
                    return arr
                """,
                Map.of(
                        "start_cycle", "Select origin element for current disjoint permutation cycle.",
                        "count_smaller", "Count elements in subarray to compute target position for displaced item.",
                        "write_cycle", "Write displaced item directly into its mathematically determined target slot.",
                        "already_placed", "Item is confirmed already located in its target position.",
                        "mark_sorted", "Mark element as fixed in sorted position."
                )
        );
    }
}
