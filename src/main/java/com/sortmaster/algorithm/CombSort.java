package com.sortmaster.algorithm;

import com.sortmaster.model.AlgorithmMetadata;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class CombSort extends AbstractSortingAlgorithm {

    public static final String ID = "comb-sort";
    private static final double SHRINK_FACTOR = 1.3;

    @Override
    public String getId() {
        return ID;
    }

    @Override
    protected void executeSort(int[] array, String detailLevel) {
        int n = array.length;
        int gap = n;
        boolean swapped = true;

        while (gap > 1 || swapped) {
            gap = (int) (gap / SHRINK_FACTOR);
            if (gap < 1) {
                gap = 1;
            }

            swapped = false;
            emitStart(array, "Comb Sort pass with gap = " + gap + " in " + order.getDisplayName().toLowerCase() + " order.");

            for (int i = 0; i < n - gap; i++) {
                int cmp = compare(array, i, i + gap, "compare_comb");
                boolean shouldSwap = isAscending() ? (cmp > 0) : (cmp < 0);
                if (shouldSwap) {
                    swap(array, i, i + gap, "swap_comb");
                    swapped = true;
                }
            }
        }
    }

    @Override
    public AlgorithmMetadata getMetadata() {
        return new AlgorithmMetadata(
                ID,
                "Comb Sort",
                "Exchange / Diminishing Gap",
                "An improvement over Bubble Sort that uses a diminishing gap with a shrink factor of 1.3 to eliminate turtles.",
                "Comb Sort was designed in 1980 by Włodzimierz Dobosiewicz and revived by Stephen Lacey and Richard Box in 1991. Like Shell Sort does for Insertion Sort, Comb Sort improves Bubble Sort by comparing elements over an initial large gap. The gap shrinks by an empirically ideal factor of 1.3 on each iteration until it reaches 1. This rapid elimination of out-of-place values at the end of lists brings average runtime down to near O(n log n).",
                "O(n log n)",
                "O(n² / 2^p) ≈ O(n log n)",
                "O(n²)",
                "O(1)",
                false,
                true,
                "Medium",
                List.of(
                        "Lightweight replacement for Bubble Sort when simplicity with high practical speed is required",
                        "Embedded systems where code footprint must be minimal and recursion is prohibited",
                        "Demonstrating how diminishing gap techniques transform quadratic exchange algorithms"
                ),
                List.of(
                        "Significantly faster than Bubble Sort in practice (approaches O(n log n) average)",
                        "Extremely simple code with zero recursion and O(1) auxiliary space",
                        "Eliminates both 'rabbits' and 'turtles' efficiently"
                ),
                List.of(
                        "Unstable: comparisons across gap intervals alter the relative ordering of duplicates",
                        "Worst case is still O(n²) for certain malicious permutations",
                        "Lacks standard library adoption compared to Quick Sort or Tim Sort"
                ),
                List.of(
                        "Terminating before gap == 1 and swapped == false",
                        "Using an incorrect shrink factor (empirical studies prove 1.3 is optimal)",
                        "Assuming it is stable"
                ),
                List.of(
                        "Why is 1.3 considered the optimal shrink factor for Comb Sort?",
                        "How does Comb Sort compare to Shell Sort in terms of underlying mechanism?",
                        "Why does Comb Sort lose the stability that Bubble Sort possessed?"
                ),
                """
                procedure combSort(A)
                    n = length(A)
                    gap = n
                    shrink = 1.3
                    sorted = false
                    while not sorted do
                        gap = floor(gap / shrink)
                        if gap <= 1 then
                            gap = 1
                            sorted = true
                        end if
                        for i = 0 to n - gap - 1 do
                            if A[i] > A[i + gap] then
                                swap(A[i], A[i + gap])
                                sorted = false
                            end if
                        end for
                    end while
                end procedure
                """,
                """
                public void combSort(int[] arr) {
                    int n = arr.length;
                    int gap = n;
                    boolean swapped = true;
                    while (gap > 1 || swapped) {
                        gap = (int)(gap / 1.3);
                        if (gap < 1) gap = 1;
                        swapped = false;
                        for (int i = 0; i < n - gap; i++) {
                            if (arr[i] > arr[i + gap]) {
                                int temp = arr[i];
                                arr[i] = arr[i + gap];
                                arr[i + gap] = temp;
                                swapped = true;
                            }
                        }
                    }
                }
                """,
                """
                def comb_sort(arr: list[int]) -> list[int]:
                    n = len(arr)
                    gap = n
                    shrink = 1.3
                    swapped = True
                    while gap > 1 or swapped:
                        gap = int(gap / shrink)
                        if gap < 1:
                            gap = 1
                        swapped = False
                        for i in range(n - gap):
                            if arr[i] > arr[i + gap]:
                                arr[i], arr[i + gap] = arr[i + gap], arr[i]
                                swapped = True
                    return arr
                """,
                """
                procedure combSortDescending(A)
                    n = length(A)
                    gap = n
                    shrink = 1.3
                    sorted = false
                    while not sorted do
                        gap = floor(gap / shrink)
                        if gap <= 1 then
                            gap = 1
                            sorted = true
                        end if
                        for i = 0 to n - gap - 1 do
                            if A[i] < A[i + gap] then
                                swap(A[i], A[i + gap])
                                sorted = false
                            end if
                        end for
                    end while
                end procedure
                """,
                """
                public void combSortDescending(int[] arr) {
                    int n = arr.length;
                    int gap = n;
                    boolean swapped = true;
                    while (gap > 1 || swapped) {
                        gap = (int)(gap / 1.3);
                        if (gap < 1) gap = 1;
                        swapped = false;
                        for (int i = 0; i < n - gap; i++) {
                            if (arr[i] < arr[i + gap]) {
                                int temp = arr[i];
                                arr[i] = arr[i + gap];
                                arr[i + gap] = temp;
                                swapped = true;
                            }
                        }
                    }
                }
                """,
                """
                def comb_sort_descending(arr: list[int]) -> list[int]:
                    n = len(arr)
                    gap = n
                    shrink = 1.3
                    swapped = True
                    while gap > 1 or swapped:
                        gap = int(gap / shrink)
                        if gap < 1:
                            gap = 1
                        swapped = False
                        for i in range(n - gap):
                            if arr[i] < arr[i + gap]:
                                arr[i], arr[i + gap] = arr[i + gap], arr[i]
                                swapped = True
                    return arr
                """,
                Map.of(
                        "compare_comb", "Compare elements separated by the current shrink gap.",
                        "swap_comb", "Swap inverted elements across the comb gap distance."
                )
        );
    }
}
