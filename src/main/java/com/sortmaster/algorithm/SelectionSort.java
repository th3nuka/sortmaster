package com.sortmaster.algorithm;

import com.sortmaster.model.AlgorithmMetadata;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class SelectionSort extends AbstractSortingAlgorithm {

    public static final String ID = "selection-sort";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    protected void executeSort(int[] array, String detailLevel) {
        int n = array.length;
        String extremumName = isAscending() ? "minimum" : "maximum";

        for (int i = 0; i < n - 1; i++) {
            int extremumIdx = i;
            emitPivot(array, extremumIdx, new int[]{i, n - 1},
                    "Starting pass " + (i + 1) + ": Assuming index " + i + " (" + array[i] + ") is current " + extremumName + ".",
                    "find_min");

            for (int j = i + 1; j < n; j++) {
                int cmp = compare(array, j, extremumIdx, "compare");
                boolean isNewExtremum = isAscending() ? (cmp < 0) : (cmp > 0);
                if (isNewExtremum) {
                    extremumIdx = j;
                    emitPivot(array, extremumIdx, new int[]{i, n - 1},
                            "New " + extremumName + " found: element " + array[extremumIdx] + " at index " + extremumIdx + ".",
                            "new_min");
                }
            }

            if (extremumIdx != i) {
                swap(array, i, extremumIdx, "swap");
            } else {
                emitCompare(array, i, extremumIdx,
                        "Element " + array[i] + " at index " + i + " is already the " + extremumName + " for this pass.",
                        "no_swap");
            }

            emitMarkSorted(array, List.of(i),
                    "Index " + i + " (" + array[i] + ") is confirmed in its final sorted position.",
                    "mark_sorted");
        }
    }

    @Override
    public AlgorithmMetadata getMetadata() {
        return new AlgorithmMetadata(
                ID,
                "Selection Sort",
                "Selection Sort",
                "Repeatedly finds the target extremum (minimum in ascending, maximum in descending) and moves it to the front.",
                "Selection Sort divides the array into a sorted sublist at the front and an unsorted sublist at the back. It scans the unsorted portion to locate the target extremum (minimum for ascending, maximum for descending), swaps it with the leftmost unsorted element, and extends the sorted boundary by one. Because it performs at most n-1 swaps regardless of input, it is useful when write operations are significantly more expensive than reads (e.g. Flash EEPROM memory).",
                "O(n²)",
                "O(n²)",
                "O(n²)",
                "O(1)",
                false,
                true,
                "Easy",
                List.of(
                        "Systems where memory write operations are extremely costly (e.g., EEPROM, flash)",
                        "Small lists where algorithm simplicity is paramount",
                        "Educational comparisons demonstrating the distinction between comparisons and swaps"
                ),
                List.of(
                        "Minimal write operations: strictly at most O(n) swaps",
                        "Simple and straightforward logic",
                        "In-place sorting with O(1) auxiliary memory",
                        "Consistent performance regardless of initial element ordering"
                ),
                List.of(
                        "O(n²) time complexity across all cases, even when array is already sorted",
                        "Not stable in standard array implementations (can jump duplicates over equal items)",
                        "Inefficient for medium to large datasets"
                ),
                List.of(
                        "Assuming it is stable (standard array swap can alter order of identical keys)",
                        "Thinking best case is O(n); it must always scan all remaining items to verify minimum/maximum",
                        "Not checking if extremumIdx == i before swapping, generating redundant swap operations"
                ),
                List.of(
                        "Why is Selection Sort unstable? Provide an example array that demonstrates this. (e.g. [5a, 5b, 2] -> 2 swaps with 5a, putting 5a after 5b)",
                        "What is the exact number of comparisons performed by Selection Sort for array size n? (Answer: n(n-1)/2)",
                        "In what hardware scenario might Selection Sort outperform Quick Sort or Insertion Sort? (Answer: When write operations degrade hardware, like Flash memory)"
                ),
                """
                procedure selectionSort(A : list of sortable items)
                    n = length(A)
                    for i = 0 to n-2 do
                        minIdx = i
                        for j = i+1 to n-1 do
                            if A[j] < A[minIdx] then
                                minIdx = j
                            end if
                        end for
                        if minIdx != i then
                            swap(A[i], A[minIdx])
                        end if
                    end for
                end procedure
                """,
                """
                public void selectionSort(int[] arr) {
                    int n = arr.length;
                    for (int i = 0; i < n - 1; i++) {
                        int minIdx = i;
                        for (int j = i + 1; j < n; j++) {
                            if (arr[j] < arr[minIdx]) {
                                minIdx = j;
                            }
                        }
                        if (minIdx != i) {
                            int temp = arr[i];
                            arr[i] = arr[minIdx];
                            arr[minIdx] = temp;
                        }
                    }
                }
                """,
                """
                def selection_sort(arr: list[int]) -> list[int]:
                    n = len(arr)
                    for i in range(n - 1):
                        min_idx = i
                        for j in range(i + 1, n):
                            if arr[j] < arr[min_idx]:
                                min_idx = j
                        if min_idx != i:
                            arr[i], arr[min_idx] = arr[min_idx], arr[i]
                    return arr
                """,
                """
                procedure selectionSortDescending(A : list of sortable items)
                    n = length(A)
                    for i = 0 to n-2 do
                        maxIdx = i
                        for j = i+1 to n-1 do
                            if A[j] > A[maxIdx] then
                                maxIdx = j
                            end if
                        end for
                        if maxIdx != i then
                            swap(A[i], A[maxIdx])
                        end if
                    end for
                end procedure
                """,
                """
                public void selectionSortDescending(int[] arr) {
                    int n = arr.length;
                    for (int i = 0; i < n - 1; i++) {
                        int maxIdx = i;
                        for (int j = i + 1; j < n; j++) {
                            if (arr[j] > arr[maxIdx]) {
                                maxIdx = j;
                            }
                        }
                        if (maxIdx != i) {
                            int temp = arr[i];
                            arr[i] = arr[maxIdx];
                            arr[maxIdx] = temp;
                        }
                    }
                }
                """,
                """
                def selection_sort_descending(arr: list[int]) -> list[int]:
                    n = len(arr)
                    for i in range(n - 1):
                        max_idx = i
                        for j in range(i + 1, n):
                            if arr[j] > arr[max_idx]:
                                max_idx = j
                        if max_idx != i:
                            arr[i], arr[max_idx] = arr[max_idx], arr[i]
                    return arr
                """,
                Map.of(
                        "find_min", "Assume the first element of the unsorted partition is the current extremum.",
                        "compare", "Compare the current candidate extremum with the next unsorted element.",
                        "new_min", "Update the extremum index to the newly discovered value.",
                        "swap", "Swap the located extremum element into the sorted partition boundary.",
                        "mark_sorted", "Expand the boundary of the sorted partition by one element."
                )
        );
    }
}
