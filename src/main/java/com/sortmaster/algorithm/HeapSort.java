package com.sortmaster.algorithm;

import com.sortmaster.model.AlgorithmMetadata;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class HeapSort extends AbstractSortingAlgorithm {

    public static final String ID = "heap-sort";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    protected void executeSort(int[] array, String detailLevel) {
        int n = array.length;
        String heapType = isAscending() ? "Max" : "Min";
        String rootType = isAscending() ? "max" : "min";

        // Build heap (rearrange array)
        emitStart(array, "Phase 1: Building " + heapType + " Heap from bottom up (starting from non-leaf nodes).");
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(array, n, i);
        }

        emitStart(array, "Phase 2: " + heapType + " Heap constructed. Extracting root element to end of array iteratively.");

        // One by one extract an element from heap
        for (int i = n - 1; i > 0; i--) {
            emitPivot(array, 0, new int[]{0, i},
                    "Current " + rootType + " element " + array[0] + " at root (index 0). Swapping with index " + i + ".",
                    "extract_root");

            // Move current root to end
            swap(array, 0, i, "swap_root");

            emitMarkSorted(array, List.of(i),
                    "Element " + array[i] + " at index " + i + " is permanently sorted in final position.",
                    "mark_sorted");

            // Call heapify on the reduced heap
            heapify(array, i, 0);
        }

        emitMarkSorted(array, List.of(0),
                "Final element at index 0 (" + array[0] + ") is sorted.",
                "mark_sorted");
    }

    private void heapify(int[] array, int n, int i) {
        int extremum = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n) {
            int cmp = compare(array, left, extremum, "compare_child");
            boolean leftWins = isAscending() ? (cmp > 0) : (cmp < 0);
            if (leftWins) {
                extremum = left;
            }
        }

        if (right < n) {
            int cmp = compare(array, right, extremum, "compare_child");
            boolean rightWins = isAscending() ? (cmp > 0) : (cmp < 0);
            if (rightWins) {
                extremum = right;
            }
        }

        if (extremum != i) {
            swap(array, i, extremum, "sift_down");
            heapify(array, n, extremum);
        }
    }

    @Override
    public AlgorithmMetadata getMetadata() {
        return new AlgorithmMetadata(
                ID,
                "Heap Sort",
                "Selection / Tree Sort",
                "Converts the array into a Heap data structure (Max-Heap for ascending, Min-Heap for descending) and iteratively extracts root.",
                "Heap Sort uses a binary heap to manage sorting efficiently. In ascending mode, it transforms the array into a Max-Heap where each parent is >= children, repeatedly moving the max root to the end. In descending mode, it transforms the array into a Min-Heap where each parent is <= children, repeatedly moving the min root to the end. It achieves guaranteed O(n log n) runtime with strictly O(1) auxiliary space.",
                "O(n log n)",
                "O(n log n)",
                "O(n log n)",
                "O(1)",
                false,
                true,
                "Medium",
                List.of(
                        "Embedded and real-time systems where worst-case O(n log n) and strict O(1) memory guarantees are required",
                        "Security-critical systems seeking immunity against Quick Sort's worst-case O(n²) denial-of-service vulnerabilities",
                        "Priority queues and top-K elements selection"
                ),
                List.of(
                        "Guaranteed O(n log n) worst-case time without recursion or extra memory",
                        "In-place sorting requiring strictly O(1) auxiliary space",
                        "Immune to quadratic worst-case degradation seen in Quick Sort"
                ),
                List.of(
                        "Poor cache locality: jumping between parent 2i+1 and 2i+2 leads to frequent cache misses",
                        "Unstable: heap structure swaps do not preserve relative order of duplicates",
                        "Slower in practice on modern CPUs than Quick Sort and Merge Sort due to memory access patterns"
                ),
                List.of(
                        "Calculating parent/child indices incorrectly (off-by-one with 2*i + 1 vs 2*i + 2)",
                        "Starting build-heap from 0 instead of n/2 - 1",
                        "Forgetting to pass reduced heap size n into heapify during extraction phase"
                ),
                List.of(
                        "Why does building a binary heap take O(n) time instead of O(n log n)?",
                        "Compare Heap Sort with Quick Sort in terms of cache performance and memory locality.",
                        "Why is Heap Sort unstable? Give a simple counterexample."
                ),
                """
                procedure heapSort(A)
                    n = length(A)
                    for i = floor(n/2) - 1 down to 0 do
                        heapify(A, n, i)
                    end for
                    for i = n - 1 down to 1 do
                        swap(A[0], A[i])
                        heapify(A, i, 0)
                    end for
                end procedure
                """,
                """
                public void heapSort(int[] arr) {
                    int n = arr.length;
                    for (int i = n / 2 - 1; i >= 0; i--) {
                        heapify(arr, n, i);
                    }
                    for (int i = n - 1; i > 0; i--) {
                        int temp = arr[0];
                        arr[0] = arr[i];
                        arr[i] = temp;
                        heapify(arr, i, 0);
                    }
                }

                private void heapify(int[] arr, int n, int i) {
                    int largest = i;
                    int left = 2 * i + 1;
                    int right = 2 * i + 2;
                    if (left < n && arr[left] > arr[largest]) largest = left;
                    if (right < n && arr[right] > arr[largest]) largest = right;
                    if (largest != i) {
                        int swap = arr[i];
                        arr[i] = arr[largest];
                        arr[largest] = swap;
                        heapify(arr, n, largest);
                    }
                }
                """,
                """
                def heap_sort(arr: list[int]) -> list[int]:
                    n = len(arr)
                    for i in range(n // 2 - 1, -1, -1):
                        heapify(arr, n, i)
                    for i in range(n - 1, 0, -1):
                        arr[0], arr[i] = arr[i], arr[0]
                        heapify(arr, i, 0)
                    return arr

                def heapify(arr: list[int], n: int, i: int) -> None:
                    largest = i
                    left = 2 * i + 1
                    right = 2 * i + 2
                    if left < n and arr[left] > arr[largest]:
                        largest = left
                    if right < n and arr[right] > arr[largest]:
                        largest = right
                    if largest != i:
                        arr[i], arr[largest] = arr[largest], arr[i]
                        heapify(arr, n, largest)
                """,
                """
                procedure heapSortDescending(A)
                    n = length(A)
                    for i = floor(n/2) - 1 down to 0 do
                        minHeapify(A, n, i)
                    end for
                    for i = n - 1 down to 1 do
                        swap(A[0], A[i])
                        minHeapify(A, i, 0)
                    end for
                end procedure
                """,
                """
                public void heapSortDescending(int[] arr) {
                    int n = arr.length;
                    for (int i = n / 2 - 1; i >= 0; i--) {
                        minHeapify(arr, n, i);
                    }
                    for (int i = n - 1; i > 0; i--) {
                        int temp = arr[0];
                        arr[0] = arr[i];
                        arr[i] = temp;
                        minHeapify(arr, i, 0);
                    }
                }

                private void minHeapify(int[] arr, int n, int i) {
                    int smallest = i;
                    int left = 2 * i + 1;
                    int right = 2 * i + 2;
                    if (left < n && arr[left] < arr[smallest]) smallest = left;
                    if (right < n && arr[right] < arr[smallest]) smallest = right;
                    if (smallest != i) {
                        int swap = arr[i];
                        arr[i] = arr[smallest];
                        arr[smallest] = swap;
                        minHeapify(arr, n, smallest);
                    }
                }
                """,
                """
                def heap_sort_descending(arr: list[int]) -> list[int]:
                    n = len(arr)
                    for i in range(n // 2 - 1, -1, -1):
                        min_heapify(arr, n, i)
                    for i in range(n - 1, 0, -1):
                        arr[0], arr[i] = arr[i], arr[0]
                        min_heapify(arr, i, 0)
                    return arr

                def min_heapify(arr: list[int], n: int, i: int) -> None:
                    smallest = i
                    left = 2 * i + 1
                    right = 2 * i + 2
                    if left < n and arr[left] < arr[smallest]:
                        smallest = left
                    if right < n and arr[right] < arr[smallest]:
                        smallest = right
                    if smallest != i:
                        arr[i], arr[smallest] = arr[smallest], arr[i]
                        min_heapify(arr, n, smallest)
                """,
                Map.of(
                        "extract_root", "Locate root element containing extremum value of current active heap.",
                        "swap_root", "Move root element to its final sorted location at end of unsorted boundary.",
                        "compare_child", "Compare parent node with its left and right children.",
                        "sift_down", "Sift down the violation to restore heap structural invariants."
                )
        );
    }
}
