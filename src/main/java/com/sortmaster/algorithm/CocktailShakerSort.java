package com.sortmaster.algorithm;

import com.sortmaster.model.AlgorithmMetadata;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class CocktailShakerSort extends AbstractSortingAlgorithm {

    public static final String ID = "cocktail-shaker-sort";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    protected void executeSort(int[] array, String detailLevel) {
        int n = array.length;
        boolean swapped = true;
        int start = 0;
        int end = n - 1;

        while (swapped) {
            swapped = false;

            // Forward pass
            emitStart(array, "Forward pass from index " + start + " to " + end + " in " + order.getDisplayName().toLowerCase() + " order.");
            for (int i = start; i < end; i++) {
                int cmp = compare(array, i, i + 1, "compare_forward");
                boolean shouldSwap = isAscending() ? (cmp > 0) : (cmp < 0);
                if (shouldSwap) {
                    swap(array, i, i + 1, "swap_forward");
                    swapped = true;
                }
            }

            emitMarkSorted(array, List.of(end),
                    "Forward pass finished: Element " + array[end] + " at index " + end + " is sorted.",
                    "mark_sorted");

            if (!swapped) {
                break;
            }

            swapped = false;
            end--;

            // Backward pass
            emitStart(array, "Backward pass from index " + (end - 1) + " down to " + start + " in " + order.getDisplayName().toLowerCase() + " order.");
            for (int i = end - 1; i >= start; i--) {
                int cmp = compare(array, i, i + 1, "compare_backward");
                boolean shouldSwap = isAscending() ? (cmp > 0) : (cmp < 0);
                if (shouldSwap) {
                    swap(array, i, i + 1, "swap_backward");
                    swapped = true;
                }
            }

            emitMarkSorted(array, List.of(start),
                    "Backward pass finished: Element " + array[start] + " at index " + start + " is sorted.",
                    "mark_sorted");

            start++;
        }
    }

    @Override
    public AlgorithmMetadata getMetadata() {
        return new AlgorithmMetadata(
                ID,
                "Cocktail Shaker Sort",
                "Exchange Sort",
                "A bidirectional variation of Bubble Sort that traverses the list alternating in both directions.",
                "Cocktail Shaker Sort (also called Bidirectional Bubble Sort, Ripple Sort, or Shuttle Sort) improves on standard Bubble Sort by alternating passes in both directions. In standard Bubble Sort, elements near the beginning move quickly, but elements stuck at the opposite end move forward by only one position per full pass. By sweeping backward, Cocktail Shaker Sort eliminates these in a single pass, noticeably speeding up partially ordered lists in both ascending and descending modes.",
                "O(n)",
                "O(n²)",
                "O(n²)",
                "O(1)",
                true,
                true,
                "Easy",
                List.of(
                        "Arrays with 'turtle' elements where small items are stuck near the end of the array",
                        "Educational comparisons illustrating bidirectional traversal benefits",
                        "Nearly sorted arrays where alternating sweeps terminate rapidly"
                ),
                List.of(
                        "Resolves the 'turtle problem' inherent to standard Bubble Sort",
                        "Stable: preserves the relative ordering of equal keys",
                        "In-place: requires only O(1) auxiliary memory",
                        "Best case is O(n) for already sorted arrays"
                ),
                List.of(
                        "Still suffers from O(n²) average and worst case time complexity",
                        "More complicated loop management than standard Bubble Sort",
                        "Outperformed by Insertion Sort on almost all inputs"
                ),
                List.of(
                        "Forgetting to decrement end or increment start, causing infinite loops",
                        "Directional index boundary errors during the backward iteration",
                        "Omitting the early termination swapped flag check between half-passes"
                ),
                List.of(
                        "What is the 'turtle problem' in Bubble Sort, and how does Cocktail Shaker Sort solve it?",
                        "Is Cocktail Shaker Sort stable? Why?",
                        "What is the worst-case number of comparisons for Cocktail Shaker Sort?"
                ),
                """
                procedure cocktailShakerSort(A)
                    start = 0, end = length(A) - 1, swapped = true
                    while swapped do
                        swapped = false
                        for i = start to end - 1 do
                            if A[i] > A[i+1] then
                                swap(A[i], A[i+1])
                                swapped = true
                            end if
                        end for
                        if not swapped then break
                        end = end - 1
                        for i = end - 1 down to start do
                            if A[i] > A[i+1] then
                                swap(A[i], A[i+1])
                                swapped = true
                            end if
                        end for
                        start = start + 1
                    end while
                end procedure
                """,
                """
                public void cocktailShakerSort(int[] arr) {
                    boolean swapped = true;
                    int start = 0, end = arr.length - 1;
                    while (swapped) {
                        swapped = false;
                        for (int i = start; i < end; i++) {
                            if (arr[i] > arr[i + 1]) {
                                int temp = arr[i];
                                arr[i] = arr[i + 1];
                                arr[i + 1] = temp;
                                swapped = true;
                            }
                        }
                        if (!swapped) break;
                        end--;
                        for (int i = end - 1; i >= start; i--) {
                            if (arr[i] > arr[i + 1]) {
                                int temp = arr[i];
                                arr[i] = arr[i + 1];
                                arr[i + 1] = temp;
                                swapped = true;
                            }
                        }
                        start++;
                    }
                }
                """,
                """
                def cocktail_shaker_sort(arr: list[int]) -> list[int]:
                    start = 0
                    end = len(arr) - 1
                    swapped = True
                    while swapped:
                        swapped = False
                        for i in range(start, end):
                            if arr[i] > arr[i + 1]:
                                arr[i], arr[i + 1] = arr[i + 1], arr[i]
                                swapped = True
                        if not swapped:
                            break
                        end -= 1
                        for i in range(end - 1, start - 1, -1):
                            if arr[i] > arr[i + 1]:
                                arr[i], arr[i + 1] = arr[i + 1], arr[i]
                                swapped = True
                        start += 1
                    return arr
                """,
                """
                procedure cocktailShakerSortDescending(A)
                    start = 0, end = length(A) - 1, swapped = true
                    while swapped do
                        swapped = false
                        for i = start to end - 1 do
                            if A[i] < A[i+1] then
                                swap(A[i], A[i+1])
                                swapped = true
                            end if
                        end for
                        if not swapped then break
                        end = end - 1
                        for i = end - 1 down to start do
                            if A[i] < A[i+1] then
                                swap(A[i], A[i+1])
                                swapped = true
                            end if
                        end for
                        start = start + 1
                    end while
                end procedure
                """,
                """
                public void cocktailShakerSortDescending(int[] arr) {
                    boolean swapped = true;
                    int start = 0, end = arr.length - 1;
                    while (swapped) {
                        swapped = false;
                        for (int i = start; i < end; i++) {
                            if (arr[i] < arr[i + 1]) {
                                int temp = arr[i];
                                arr[i] = arr[i + 1];
                                arr[i + 1] = temp;
                                swapped = true;
                            }
                        }
                        if (!swapped) break;
                        end--;
                        for (int i = end - 1; i >= start; i--) {
                            if (arr[i] < arr[i + 1]) {
                                int temp = arr[i];
                                arr[i] = arr[i + 1];
                                arr[i + 1] = temp;
                                swapped = true;
                            }
                        }
                        start++;
                    }
                }
                """,
                """
                def cocktail_shaker_sort_descending(arr: list[int]) -> list[int]:
                    start = 0
                    end = len(arr) - 1
                    swapped = True
                    while swapped:
                        swapped = False
                        for i in range(start, end):
                            if arr[i] < arr[i + 1]:
                                arr[i], arr[i + 1] = arr[i + 1], arr[i]
                                swapped = True
                        if not swapped:
                            break
                        end -= 1
                        for i in range(end - 1, start - 1, -1):
                            if arr[i] < arr[i + 1]:
                                arr[i], arr[i + 1] = arr[i + 1], arr[i]
                                swapped = True
                        start += 1
                    return arr
                """,
                Map.of(
                        "compare_forward", "Compare adjacent elements in the forward pass.",
                        "swap_forward", "Swap out-of-order adjacent elements moving forward.",
                        "compare_backward", "Compare adjacent elements in the backward pass.",
                        "swap_backward", "Swap out-of-order adjacent elements moving backward.",
                        "mark_sorted", "Mark the element that has settled into its final sorted position."
                )
        );
    }
}
