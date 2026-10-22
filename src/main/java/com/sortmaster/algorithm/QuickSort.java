package com.sortmaster.algorithm;

import com.sortmaster.model.AlgorithmMetadata;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class QuickSort extends AbstractSortingAlgorithm {

    public static final String ID = "quick-sort";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    protected void executeSort(int[] array, String detailLevel) {
        quickSort(array, 0, array.length - 1);
    }

    private void quickSort(int[] array, int low, int high) {
        if (low < high) {
            emitPartition(array, low, high, high,
                    "Active partition range [" + low + ".." + high + "]. Choosing pivot = array[" + high + "] (" + array[high] + ") in " + order.getDisplayName().toLowerCase() + " order.",
                    "partition_start");

            int pi = partition(array, low, high);

            emitMarkSorted(array, List.of(pi),
                    "Pivot element " + array[pi] + " is now in its permanent sorted index " + pi + ".",
                    "pivot_placed");

            quickSort(array, low, pi - 1);
            quickSort(array, pi + 1, high);
        } else if (low == high) {
            emitMarkSorted(array, List.of(low),
                    "Single element at index " + low + " (" + array[low] + ") is sorted.",
                    "single_element");
        }
    }

    private int partition(int[] array, int low, int high) {
        int pivot = array[high];
        emitPivot(array, high, new int[]{low, high},
                "Selected pivot element " + pivot + " at index " + high + ".",
                "choose_pivot");

        int i = low - 1; // Index of partitioned elements

        for (int j = low; j < high; j++) {
            emitCompare(array, j, high,
                    "Comparing array[" + j + "] (" + array[j] + ") with pivot (" + pivot + ").",
                    "compare_pivot");

            boolean belongsBeforePivot = isAscending() ? (array[j] <= pivot) : (array[j] >= pivot);

            if (belongsBeforePivot) {
                i++;
                if (i != j) {
                    swap(array, i, j, "swap_smaller");
                }
            }
        }

        // Place pivot in its correct position
        swap(array, i + 1, high, "place_pivot");
        return i + 1;
    }

    @Override
    public AlgorithmMetadata getMetadata() {
        return new AlgorithmMetadata(
                ID,
                "Quick Sort",
                "Divide and Conquer",
                "Picks an element as pivot and partitions the given array around the picked pivot.",
                "Quick Sort, developed by Tony Hoare in 1959, is one of the most widely used general-purpose sorting algorithms. It works by choosing a 'pivot' element, rearranging the array so that elements matching the target order relative to the pivot appear before it and remaining elements appear after it (partitioning), and then recursively sorting the left and right partitions. Because it sorts in-place and exhibits superb cache locality, its constant factors are exceptionally small.",
                "O(n log n)",
                "O(n log n)",
                "O(n²)",
                "O(log n)",
                false,
                true,
                "Medium",
                List.of(
                        "Standard general-purpose in-memory sorting of primitive types (e.g. Arrays.sort in Java uses Dual-Pivot Quicksort)",
                        "Systems with fast cache hierarchies benefiting from sequential memory locality",
                        "When in-place sorting is required and average O(n log n) performance is needed"
                ),
                List.of(
                        "Very fast practical runtime due to excellent cache locality and low constant factors",
                        "In-place sorting requiring only O(log n) stack memory",
                        "Easily adaptable to Randomized Pivot or Dual-Pivot strategies"
                ),
                List.of(
                        "Worst-case O(n²) time complexity if poor pivots (e.g., already sorted array with naive pivot) are chosen",
                        "Unstable algorithm: relative order of equal keys is not preserved",
                        "Recursive call stack can reach O(n) depth in worst case without tail call optimization"
                ),
                List.of(
                        "Picking a fixed pivot (e.g., always last element) on already sorted arrays triggering O(n²)",
                        "Lomuto partition indexing bugs with the i and j pointer increments",
                        "Stack overflow from deep recursion on unbalanced partitions"
                ),
                List.of(
                        "What causes Quick Sort to degrade to O(n²) time complexity, and how can randomized pivot or 3-way partitioning prevent it?",
                        "Compare Quick Sort and Merge Sort regarding auxiliary space, stability, and cache performance.",
                        "Explain Lomuto partitioning vs Hoare partitioning. Which performs fewer swaps?"
                ),
                """
                procedure quickSort(A, low, high)
                    if low < high then
                        pi = partition(A, low, high)
                        quickSort(A, low, pi - 1)
                        quickSort(A, pi + 1, high)
                    end if
                end procedure

                procedure partition(A, low, high)
                    pivot = A[high]
                    i = low - 1
                    for j = low to high - 1 do
                        if A[j] <= pivot then
                            i = i + 1
                            swap(A[i], A[j])
                        end if
                    end for
                    swap(A[i + 1], A[high])
                    return i + 1
                end procedure
                """,
                """
                public void quickSort(int[] arr, int low, int high) {
                    if (low < high) {
                        int pi = partition(arr, low, high);
                        quickSort(arr, low, pi - 1);
                        quickSort(arr, pi + 1, high);
                    }
                }

                private int partition(int[] arr, int low, int high) {
                    int pivot = arr[high];
                    int i = low - 1;
                    for (int j = low; j < high; j++) {
                        if (arr[j] <= pivot) {
                            i++;
                            int temp = arr[i];
                            arr[i] = arr[j];
                            arr[j] = temp;
                        }
                    }
                    int temp = arr[i + 1];
                    arr[i + 1] = arr[high];
                    arr[high] = temp;
                    return i + 1;
                }
                """,
                """
                def quick_sort(arr: list[int], low: int = 0, high: int = None) -> list[int]:
                    if high is None:
                        high = len(arr) - 1
                    if low < high:
                        pi = partition(arr, low, high)
                        quick_sort(arr, low, pi - 1)
                        quick_sort(arr, pi + 1, high)
                    return arr

                def partition(arr: list[int], low: int, high: int) -> int:
                    pivot = arr[high]
                    i = low - 1
                    for j in range(low, high):
                        if arr[j] <= pivot:
                            i += 1
                            arr[i], arr[j] = arr[j], arr[i]
                    arr[i + 1], arr[high] = arr[high], arr[i + 1]
                    return i + 1
                """,
                """
                procedure quickSortDescending(A, low, high)
                    if low < high then
                        pi = partitionDescending(A, low, high)
                        quickSortDescending(A, low, pi - 1)
                        quickSortDescending(A, pi + 1, high)
                    end if
                end procedure

                procedure partitionDescending(A, low, high)
                    pivot = A[high]
                    i = low - 1
                    for j = low to high - 1 do
                        if A[j] >= pivot then
                            i = i + 1
                            swap(A[i], A[j])
                        end if
                    end for
                    swap(A[i + 1], A[high])
                    return i + 1
                end procedure
                """,
                """
                public void quickSortDescending(int[] arr, int low, int high) {
                    if (low < high) {
                        int pi = partitionDescending(arr, low, high);
                        quickSortDescending(arr, low, pi - 1);
                        quickSortDescending(arr, pi + 1, high);
                    }
                }

                private int partitionDescending(int[] arr, int low, int high) {
                    int pivot = arr[high];
                    int i = low - 1;
                    for (int j = low; j < high; j++) {
                        if (arr[j] >= pivot) {
                            i++;
                            int temp = arr[i];
                            arr[i] = arr[j];
                            arr[j] = temp;
                        }
                    }
                    int temp = arr[i + 1];
                    arr[i + 1] = arr[high];
                    arr[high] = temp;
                    return i + 1;
                }
                """,
                """
                def quick_sort_descending(arr: list[int], low: int = 0, high: int = None) -> list[int]:
                    if high is None:
                        high = len(arr) - 1
                    if low < high:
                        pi = partition_descending(arr, low, high)
                        quick_sort_descending(arr, low, pi - 1)
                        quick_sort_descending(arr, pi + 1, high)
                    return arr

                def partition_descending(arr: list[int], low: int, high: int) -> int:
                    pivot = arr[high]
                    i = low - 1
                    for j in range(low, high):
                        if arr[j] >= pivot:
                            i += 1
                            arr[i], arr[j] = arr[j], arr[i]
                    arr[i + 1], arr[high] = arr[high], arr[i + 1]
                    return i + 1
                """,
                Map.of(
                        "partition_start", "Define active subarray boundary and prepare partition pointers.",
                        "choose_pivot", "Select the pivot element against which all other subarray elements are compared.",
                        "compare_pivot", "Compare current element with pivot to determine left or right partition placement.",
                        "swap_smaller", "Swap element into target partition boundary.",
                        "place_pivot", "Swap pivot into its permanent position between partitions.",
                        "pivot_placed", "Pivot element is now fixed in its final sorted index."
                )
        );
    }
}
