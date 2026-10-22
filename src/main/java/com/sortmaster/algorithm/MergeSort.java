package com.sortmaster.algorithm;

import com.sortmaster.model.AlgorithmMetadata;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class MergeSort extends AbstractSortingAlgorithm {

    public static final String ID = "merge-sort";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    protected void executeSort(int[] array, String detailLevel) {
        mergeSort(array, 0, array.length - 1);
    }

    private void mergeSort(int[] array, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;

            emitPartition(array, left, right, mid,
                    "Dividing subarray [" + left + ".." + right + "] into left [" + left + ".." + mid + "] and right [" + (mid + 1) + ".." + right + "].",
                    "divide");

            mergeSort(array, left, mid);
            mergeSort(array, mid + 1, right);

            merge(array, left, mid, right);
        }
    }

    private void merge(int[] array, int left, int mid, int right) {
        emitMerge(array, left, mid, right,
                "Merging sorted subarrays [" + left + ".." + mid + "] and [" + (mid + 1) + ".." + right + "] in " + order.getDisplayName().toLowerCase() + " order.",
                "merge_start");

        int n1 = mid - left + 1;
        int n2 = right - mid;

        int[] L = new int[n1];
        int[] R = new int[n2];

        System.arraycopy(array, left, L, 0, n1);
        System.arraycopy(array, mid + 1, R, 0, n2);

        int i = 0, j = 0;
        int k = left;

        while (i < n1 && j < n2) {
            emitCompare(array, left + i, mid + 1 + j,
                    "Comparing left element " + L[i] + " with right element " + R[j] + ".",
                    "compare_merge");

            boolean takeLeft = isAscending() ? (L[i] <= R[j]) : (L[i] >= R[j]);

            if (takeLeft) {
                array[k] = L[i];
                emitWrite(array, k, L[i],
                        "Writing left element " + L[i] + " into position " + k + " (preserves stability).",
                        "write_merge");
                i++;
            } else {
                array[k] = R[j];
                emitWrite(array, k, R[j],
                        "Writing right element " + R[j] + " into position " + k + ".",
                        "write_merge");
                j++;
            }
            k++;
        }

        while (i < n1) {
            array[k] = L[i];
            emitWrite(array, k, L[i],
                    "Writing remaining left element " + L[i] + " into position " + k + ".",
                    "write_remaining");
            i++;
            k++;
        }

        while (j < n2) {
            array[k] = R[j];
            emitWrite(array, k, R[j],
                    "Writing remaining right element " + R[j] + " into position " + k + ".",
                    "write_remaining");
            j++;
            k++;
        }

        List<Integer> mergedRange = new ArrayList<>();
        for (int idx = left; idx <= right; idx++) {
            mergedRange.add(idx);
        }
        emitMarkSorted(array, mergedRange,
                "Subarray [" + left + ".." + right + "] successfully merged and sorted in " + order.getDisplayName().toLowerCase() + " order.",
                "merge_complete");
    }

    @Override
    public AlgorithmMetadata getMetadata() {
        return new AlgorithmMetadata(
                ID,
                "Merge Sort",
                "Divide and Conquer",
                "Divides the array into two halves, recursively sorts them, and merges the two sorted halves.",
                "Merge Sort was invented by John von Neumann in 1945. It follows the Divide and Conquer paradigm: it recursively breaks the input list into two equal halves until reaching single-element subarrays (which are inherently sorted), and then merges them back together in linear time. Because its worst-case performance is guaranteed to be O(n log n) and it is completely stable, Merge Sort is heavily used for sorting linked lists, external datasets, and general object collections.",
                "O(n log n)",
                "O(n log n)",
                "O(n log n)",
                "O(n)",
                true,
                false,
                "Medium",
                List.of(
                        "Sorting linked lists (merging does not require random array indexing or extra space)",
                        "External sorting for massive datasets that cannot fit entirely in RAM",
                        "When guaranteed O(n log n) worst-case time is critical without variance",
                        "Sorting objects where preserving the relative order of equal keys (stability) is mandatory"
                ),
                List.of(
                        "Guaranteed O(n log n) performance across best, average, and worst cases",
                        "Completely stable sorting algorithm",
                        "Highly parallelizable divide-and-conquer structure",
                        "Optimal for sequential data structures like linked lists"
                ),
                List.of(
                        "Requires O(n) auxiliary memory for merging arrays",
                        "Higher constant factor overhead than Quick Sort for standard in-memory arrays",
                        "Recursion stack overhead O(log n)"
                ),
                List.of(
                        "Integer overflow when computing mid = (left + right) / 2 instead of left + (right - left) / 2",
                        "Violating stability by using `<` instead of `<=` when comparing L[i] and R[j]",
                        "Forgetting to flush remaining elements from the left or right sub-arrays"
                ),
                List.of(
                        "State the recurrence relation for Merge Sort and solve it using Master Theorem. (T(n) = 2T(n/2) + O(n) => O(n log n))",
                        "Why is Merge Sort preferred over Quick Sort for sorting Linked Lists?",
                        "How can Merge Sort be modified to count inversions in an array in O(n log n) time?"
                ),
                """
                procedure mergeSort(A, left, right)
                    if left < right then
                        mid = left + (right - left) / 2
                        mergeSort(A, left, mid)
                        mergeSort(A, mid + 1, right)
                        merge(A, left, mid, right)
                    end if
                end procedure
                """,
                """
                public void mergeSort(int[] arr, int left, int right) {
                    if (left < right) {
                        int mid = left + (right - left) / 2;
                        mergeSort(arr, left, mid);
                        mergeSort(arr, mid + 1, right);
                        merge(arr, left, mid, right);
                    }
                }
                """,
                """
                def merge_sort(arr: list[int], left: int = 0, right: int = None) -> list[int]:
                    if right is None:
                        right = len(arr) - 1
                    if left < right:
                        mid = left + (right - left) // 2
                        merge_sort(arr, left, mid)
                        merge_sort(arr, mid + 1, right)
                        merge(arr, left, mid, right)
                    return arr

                def merge(arr: list[int], left: int, mid: int, right: int) -> None:
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
                """,
                """
                procedure mergeSortDescending(A, left, right)
                    if left < right then
                        mid = left + (right - left) / 2
                        mergeSortDescending(A, left, mid)
                        mergeSortDescending(A, mid + 1, right)
                        mergeDescending(A, left, mid, right)
                    end if
                end procedure
                """,
                """
                public void mergeSortDescending(int[] arr, int left, int right) {
                    if (left < right) {
                        int mid = left + (right - left) / 2;
                        mergeSortDescending(arr, left, mid);
                        mergeSortDescending(arr, mid + 1, right);
                        mergeDescending(arr, left, mid, right);
                    }
                }
                """,
                """
                def merge_sort_descending(arr: list[int], left: int = 0, right: int = None) -> list[int]:
                    if right is None:
                        right = len(arr) - 1
                    if left < right:
                        mid = left + (right - left) // 2
                        merge_sort_descending(arr, left, mid)
                        merge_sort_descending(arr, mid + 1, right)
                        merge_descending(arr, left, mid, right)
                    return arr

                def merge_descending(arr: list[int], left: int, mid: int, right: int) -> None:
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
                """,
                Map.of(
                        "divide", "Divide the current subarray at midpoint into left and right sub-problems.",
                        "merge_start", "Allocate temporary storage and begin the linear merge process.",
                        "compare_merge", "Compare elements from heads of both sorted sublists.",
                        "write_merge", "Write the selected element into the target array position.",
                        "merge_complete", "The merged subarray is now fully ordered."
                )
        );
    }
}
