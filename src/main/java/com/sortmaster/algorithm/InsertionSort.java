package com.sortmaster.algorithm;

import com.sortmaster.model.AlgorithmMetadata;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class InsertionSort extends AbstractSortingAlgorithm {

    public static final String ID = "insertion-sort";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    protected void executeSort(int[] array, String detailLevel) {
        int n = array.length;
        emitMarkSorted(array, List.of(0), "Index 0 (" + array[0] + ") is sorted as a trivial 1-element sublist.", "init");

        for (int i = 1; i < n; i++) {
            int key = array[i];
            int j = i - 1;

            emitPivot(array, i, new int[]{0, i},
                    "Extracting key = " + key + " from index " + i + " to insert into sorted prefix [0.." + (i - 1) + "].",
                    "pick_key");

            while (j >= 0) {
                emitCompare(array, j, j + 1,
                        "Comparing sorted element at index " + j + " (" + array[j] + ") with key (" + key + ").",
                        "compare");
                boolean shouldShift = isAscending() ? (array[j] > key) : (array[j] < key);
                if (shouldShift) {
                    // Shift element at j to j + 1
                    array[j + 1] = array[j];
                    emitWrite(array, j + 1, array[j],
                            "Shifted element " + array[j] + " from index " + j + " to index " + (j + 1) + ".",
                            "shift");
                    j--;
                } else {
                    String rel = isAscending() ? ">=" : "<=";
                    emitCompare(array, j, j + 1,
                            "Key " + key + " is " + rel + " element " + array[j] + " at index " + j + ". Insertion position found.",
                            "stop_shift");
                    break;
                }
            }

            array[j + 1] = key;
            emitWrite(array, j + 1, key,
                    "Inserted key " + key + " at index " + (j + 1) + ".",
                    "insert_key");

            List<Integer> prefix = new ArrayList<>();
            for (int k = 0; k <= i; k++) {
                prefix.add(k);
            }
            emitMarkSorted(array, prefix,
                    "Sorted prefix now spans indices [0.." + i + "].",
                    "mark_prefix");
        }
    }

    @Override
    public AlgorithmMetadata getMetadata() {
        return new AlgorithmMetadata(
                ID,
                "Insertion Sort",
                "Insertion Sort",
                "Builds the final sorted array one item at a time by repeatedly inserting each new element into its proper position.",
                "Insertion Sort mimics the way people naturally sort playing cards in their hands. It iterates through the array, taking each element as a 'key', and shifts all out-of-order elements in the sorted sublist to the right to make room for insertion. It is remarkably efficient for small arrays (typically n < 32) and nearly-sorted arrays, making it an essential building block inside advanced hybrid algorithms like Tim Sort and Introsort.",
                "O(n)",
                "O(n²)",
                "O(n²)",
                "O(1)",
                true,
                true,
                "Easy",
                List.of(
                        "Small datasets (n < 32) where low overhead beats O(n log n) recursive algorithms",
                        "Subarrays inside hybrid algorithms like Tim Sort and Quick Sort / Introsort",
                        "Nearly sorted or partially sorted datasets (runs in near O(n) time)",
                        "Online sorting where data arrives continuously in a stream"
                ),
                List.of(
                        "Adaptive: runs in O(n) time when array is already sorted or nearly sorted",
                        "Stable: preserves the relative ordering of duplicate elements",
                        "In-place: requires only O(1) auxiliary memory",
                        "Online: can sort a list as it receives it element by element"
                ),
                List.of(
                        "O(n²) worst and average case makes it impractical for large datasets",
                        "Performs many data shift operations when elements are far from target positions"
                ),
                List.of(
                        "Off-by-one errors with index j when shifting elements",
                        "Forgetting to check j >= 0 before accessing array[j], causing IndexOutOfBounds",
                        "Using swap instead of direct shifts, introducing unnecessary writes"
                ),
                List.of(
                        "What is the number of comparisons in Insertion Sort when the input is already sorted? (Answer: n - 1)",
                        "How does Insertion Sort compare to Bubble Sort for nearly sorted arrays? (Answer: Both are O(n), but Insertion Sort does fewer operations and no swaps)",
                        "Explain why Tim Sort uses Insertion Sort for small runs."
                ),
                """
                procedure insertionSort(A : list of sortable items)
                    n = length(A)
                    for i = 1 to n-1 do
                        key = A[i]
                        j = i - 1
                        while j >= 0 and A[j] > key do
                            A[j+1] = A[j]
                            j = j - 1
                        end while
                        A[j+1] = key
                    end for
                end procedure
                """,
                """
                public void insertionSort(int[] arr) {
                    int n = arr.length;
                    for (int i = 1; i < n; i++) {
                        int key = arr[i];
                        int j = i - 1;
                        while (j >= 0 && arr[j] > key) {
                            arr[j + 1] = arr[j];
                            j = j - 1;
                        }
                        arr[j + 1] = key;
                    }
                }
                """,
                """
                def insertion_sort(arr: list[int]) -> list[int]:
                    n = len(arr)
                    for i in range(1, n):
                        key = arr[i]
                        j = i - 1
                        while j >= 0 and arr[j] > key:
                            arr[j + 1] = arr[j]
                            j -= 1
                        arr[j + 1] = key
                    return arr
                """,
                """
                procedure insertionSortDescending(A : list of sortable items)
                    n = length(A)
                    for i = 1 to n-1 do
                        key = A[i]
                        j = i - 1
                        while j >= 0 and A[j] < key do
                            A[j+1] = A[j]
                            j = j - 1
                        end while
                        A[j+1] = key
                    end for
                end procedure
                """,
                """
                public void insertionSortDescending(int[] arr) {
                    int n = arr.length;
                    for (int i = 1; i < n; i++) {
                        int key = arr[i];
                        int j = i - 1;
                        while (j >= 0 && arr[j] < key) {
                            arr[j + 1] = arr[j];
                            j = j - 1;
                        }
                        arr[j + 1] = key;
                    }
                }
                """,
                """
                def insertion_sort_descending(arr: list[int]) -> list[int]:
                    n = len(arr)
                    for i in range(1, n):
                        key = arr[i]
                        j = i - 1
                        while j >= 0 and arr[j] < key:
                            arr[j + 1] = arr[j]
                            j -= 1
                        arr[j + 1] = key
                    return arr
                """,
                Map.of(
                        "pick_key", "Select the current element as the 'key' to be inserted into the sorted subarray.",
                        "compare", "Compare the key with the sorted element to check if shifting is required.",
                        "shift", "Shift the out-of-order sorted element one position to the right.",
                        "insert_key", "Drop the key into the vacant position that maintains sorted order.",
                        "mark_prefix", "The prefix up to this index is now verified to be sorted."
                )
        );
    }
}
