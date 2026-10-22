package com.sortmaster.algorithm;

import com.sortmaster.model.AlgorithmMetadata;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class BubbleSort extends AbstractSortingAlgorithm {

    public static final String ID = "bubble-sort";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    protected void executeSort(int[] array, String detailLevel) {
        int n = array.length;
        boolean swapped;

        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                int cmp = compare(array, j, j + 1, "compare");
                boolean shouldSwap = isAscending() ? (cmp > 0) : (cmp < 0);
                if (shouldSwap) {
                    swap(array, j, j + 1, "swap");
                    swapped = true;
                }
            }
            int sortedIndex = n - i - 1;
            String elementRole = isAscending() ? "largest" : "smallest";
            emitMarkSorted(array, List.of(sortedIndex),
                    "Pass " + (i + 1) + " complete: The " + elementRole + " remaining element (" + array[sortedIndex] + ") at index " + sortedIndex + " is now in its final sorted position.",
                    "mark_sorted");

            if (!swapped) {
                List<Integer> remaining = new ArrayList<>();
                for (int k = 0; k < n - i - 1; k++) {
                    remaining.add(k);
                }
                emitMarkSorted(array, remaining,
                        "Early exit triggered! No swaps were made in pass " + (i + 1) + "; the array is fully sorted in " + order.getDisplayName().toLowerCase() + " order.",
                        "early_exit");
                break;
            }
        }
    }

    @Override
    public AlgorithmMetadata getMetadata() {
        return new AlgorithmMetadata(
                ID,
                "Bubble Sort",
                "Exchange Sort",
                "Repeatedly steps through the list, compares adjacent elements, and swaps them if they are in the wrong order.",
                "Bubble Sort is a foundational comparison-based sorting algorithm. In each pass through the array, adjacent elements are compared and swapped if out of order. With each pass, the extreme unsorted element bubbles to its correct position at the end of the array. An optimized version detects if no swaps occurred during a pass, terminating early in O(n) time.",
                "O(n)",
                "O(n²)",
                "O(n²)",
                "O(1)",
                true,
                true,
                "Easy",
                List.of(
                        "Educational demonstrations and teaching core sorting concepts",
                        "Detecting whether an array is already sorted in a single pass",
                        "Very small datasets (n < 20) where implementation simplicity is valued"
                ),
                List.of(
                        "Extremely simple to conceptualize and implement",
                        "Stable sorting preserves the relative order of duplicate elements",
                        "In-place sorting requiring only O(1) auxiliary memory",
                        "Best-case O(n) time complexity with the swapped flag optimization"
                ),
                List.of(
                        "O(n²) time complexity makes it impractical for large datasets",
                        "Performs excessive write operations compared to Selection Sort",
                        "Generally slower than Insertion Sort for small or nearly sorted lists"
                ),
                List.of(
                        "Forgetting the swapped flag optimization, leading to O(n²) best-case time",
                        "Inner loop upper bound mistakes (iterating up to n-1 instead of n-i-1)",
                        "Off-by-one errors causing ArrayIndexOutOfBoundsException when checking j + 1"
                ),
                List.of(
                        "What is the maximum number of comparisons in Bubble Sort for an array of size n? (Answer: n(n-1)/2)",
                        "Is Bubble Sort stable? Explain why adjacent element comparisons preserve stability.",
                        "Under what condition does Bubble Sort execute in O(n) time?"
                ),
                """
                procedure bubbleSort(A : list of sortable items)
                    n = length(A)
                    repeat
                        swapped = false
                        for i = 1 to n-1 inclusive do
                            if A[i-1] > A[i] then
                                swap(A[i-1], A[i])
                                swapped = true
                            end if
                        end for
                        n = n - 1
                    until not swapped
                end procedure
                """,
                """
                public void bubbleSort(int[] arr) {
                    int n = arr.length;
                    boolean swapped;
                    for (int i = 0; i < n - 1; i++) {
                        swapped = false;
                        for (int j = 0; j < n - i - 1; j++) {
                            if (arr[j] > arr[j + 1]) {
                                int temp = arr[j];
                                arr[j] = arr[j + 1];
                                arr[j + 1] = temp;
                                swapped = true;
                            }
                        }
                        if (!swapped) break;
                    }
                }
                """,
                """
                def bubble_sort(arr: list[int]) -> list[int]:
                    n = len(arr)
                    for i in range(n - 1):
                        swapped = False
                        for j in range(n - i - 1):
                            if arr[j] > arr[j + 1]:
                                arr[j], arr[j + 1] = arr[j + 1], arr[j]
                                swapped = True
                        if not swapped:
                            break
                    return arr
                """,
                """
                procedure bubbleSortDescending(A : list of sortable items)
                    n = length(A)
                    repeat
                        swapped = false
                        for i = 1 to n-1 inclusive do
                            if A[i-1] < A[i] then
                                swap(A[i-1], A[i])
                                swapped = true
                            end if
                        end for
                        n = n - 1
                    until not swapped
                end procedure
                """,
                """
                public void bubbleSortDescending(int[] arr) {
                    int n = arr.length;
                    boolean swapped;
                    for (int i = 0; i < n - 1; i++) {
                        swapped = false;
                        for (int j = 0; j < n - i - 1; j++) {
                            if (arr[j] < arr[j + 1]) {
                                int temp = arr[j];
                                arr[j] = arr[j + 1];
                                arr[j + 1] = temp;
                                swapped = true;
                            }
                        }
                        if (!swapped) break;
                    }
                }
                """,
                """
                def bubble_sort_descending(arr: list[int]) -> list[int]:
                    n = len(arr)
                    for i in range(n - 1):
                        swapped = False
                        for j in range(n - i - 1):
                            if arr[j] < arr[j + 1]:
                                arr[j], arr[j + 1] = arr[j + 1], arr[j]
                                swapped = True
                        if not swapped:
                            break
                    return arr
                """,
                Map.of(
                        "compare", "Compare adjacent elements arr[j] and arr[j+1] to check if they are in the wrong order.",
                        "swap", "Swap adjacent elements since they violate the target sorting order.",
                        "mark_sorted", "The extreme element in the current pass has settled into its final sorted position.",
                        "early_exit", "Zero swaps occurred in this pass, proving the array is fully sorted. We can safely terminate."
                )
        );
    }
}
