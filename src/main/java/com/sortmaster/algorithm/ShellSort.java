package com.sortmaster.algorithm;

import com.sortmaster.model.AlgorithmMetadata;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ShellSort extends AbstractSortingAlgorithm {

    public static final String ID = "shell-sort";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    protected void executeSort(int[] array, String detailLevel) {
        int n = array.length;

        // Start with a large gap, then reduce the gap
        for (int gap = n / 2; gap > 0; gap /= 2) {
            emitStart(array, "Active gap sequence = " + gap + ". Performing gapped insertion sort in " + order.getDisplayName().toLowerCase() + " order.");

            for (int i = gap; i < n; i++) {
                int temp = array[i];
                int j = i;

                emitPivot(array, i, new int[]{0, n - 1},
                        "Gapped insertion: picking element " + temp + " at index " + i + " with gap " + gap + ".",
                        "pick_element");

                while (j >= gap) {
                    emitCompare(array, j - gap, i,
                            "Comparing index " + (j - gap) + " (" + array[j - gap] + ") with current element (" + temp + ") separated by gap " + gap + ".",
                            "compare_gap");

                    boolean shouldShift = isAscending() ? (array[j - gap] > temp) : (array[j - gap] < temp);

                    if (shouldShift) {
                        array[j] = array[j - gap];
                        emitWrite(array, j, array[j - gap],
                                "Shifted element " + array[j - gap] + " forward by gap " + gap + " to index " + j + ".",
                                "shift_gap");
                        j -= gap;
                    } else {
                        break;
                    }
                }

                array[j] = temp;
                emitWrite(array, j, temp,
                        "Placed element " + temp + " at target gap position index " + j + ".",
                        "place_element");
            }
        }
    }

    @Override
    public AlgorithmMetadata getMetadata() {
        return new AlgorithmMetadata(
                ID,
                "Shell Sort",
                "Insertion / Diminishing Increment",
                "An optimization over insertion sort that allows the exchange of far-apart elements using diminishing gaps.",
                "Invented by Donald Shell in 1959, Shell Sort was the first algorithm to break the O(n²) barrier for general comparison sorting. It improves upon Insertion Sort by comparing and shifting elements separated by a gap of several positions, rather than just adjacent elements. By progressively reducing the gap down to 1, distant inversions are resolved rapidly with few shifts, leaving the array nearly sorted for the final standard insertion pass.",
                "O(n log n)",
                "O(n^(4/3))",
                "O(n²)",
                "O(1)",
                false,
                true,
                "Medium",
                List.of(
                        "Medium-sized arrays where recursion stack overhead of Quick Sort or Merge Sort is unwanted",
                        "Embedded microcontrollers with severely restricted RAM and stack limits",
                        "Hardware-level sorting implementations"
                ),
                List.of(
                        "Significantly faster than standard Insertion Sort without requiring recursion",
                        "In-place sorting requiring strictly O(1) auxiliary space",
                        "Simple to implement compared to Heap Sort or Quick Sort"
                ),
                List.of(
                        "Unstable: jumps elements over each other, disrupting initial duplicate order",
                        "Complexity heavily depends on the chosen gap sequence (Shell, Sedgewick, Knuth, Hibbard)",
                        "Slower than modern O(n log n) algorithms for large inputs"
                ),
                List.of(
                        "Using an inefficient gap sequence that yields O(n²) worst-case behavior",
                        "Off-by-one errors with gap termination condition (must finish with gap = 1)",
                        "Thinking Shell Sort is stable"
                ),
                List.of(
                        "How does Shell Sort overcome the fundamental limitation of Bubble and Insertion Sort? (Answer: Eliminates distant inversions in O(1) swaps)",
                        "What is the effect of using Knuth's sequence (h = 3h + 1) versus Shell's original sequence (n/2)?",
                        "Is Shell Sort an adaptive sorting algorithm?"
                ),
                """
                procedure shellSort(A)
                    n = length(A)
                    gap = floor(n/2)
                    while gap > 0 do
                        for i = gap to n-1 do
                            temp = A[i]
                            j = i
                            while j >= gap and A[j - gap] > temp do
                                A[j] = A[j - gap]
                                j = j - gap
                            end while
                            A[j] = temp
                        end for
                        gap = floor(gap / 2)
                    end while
                end procedure
                """,
                """
                public void shellSort(int[] arr) {
                    int n = arr.length;
                    for (int gap = n / 2; gap > 0; gap /= 2) {
                        for (int i = gap; i < n; i++) {
                            int temp = arr[i];
                            int j = i;
                            while (j >= gap && arr[j - gap] > temp) {
                                arr[j] = arr[j - gap];
                                j -= gap;
                            }
                            arr[j] = temp;
                        }
                    }
                }
                """,
                """
                def shell_sort(arr: list[int]) -> list[int]:
                    n = len(arr)
                    gap = n // 2
                    while gap > 0:
                        for i in range(gap, n):
                            temp = arr[i]
                            j = i
                            while j >= gap and arr[j - gap] > temp:
                                arr[j] = arr[j - gap]
                                j -= gap
                            arr[j] = temp
                        gap //= 2
                    return arr
                """,
                """
                procedure shellSortDescending(A)
                    n = length(A)
                    gap = floor(n/2)
                    while gap > 0 do
                        for i = gap to n-1 do
                            temp = A[i]
                            j = i
                            while j >= gap and A[j - gap] < temp do
                                A[j] = A[j - gap]
                                j = j - gap
                            end while
                            A[j] = temp
                        end for
                        gap = floor(gap / 2)
                    end while
                end procedure
                """,
                """
                public void shellSortDescending(int[] arr) {
                    int n = arr.length;
                    for (int gap = n / 2; gap > 0; gap /= 2) {
                        for (int i = gap; i < n; i++) {
                            int temp = arr[i];
                            int j = i;
                            while (j >= gap && arr[j - gap] < temp) {
                                arr[j] = arr[j - gap];
                                j -= gap;
                            }
                            arr[j] = temp;
                        }
                    }
                }
                """,
                """
                def shell_sort_descending(arr: list[int]) -> list[int]:
                    n = len(arr)
                    gap = n // 2
                    while gap > 0:
                        for i in range(gap, n):
                            temp = arr[i]
                            j = i
                            while j >= gap and arr[j - gap] < temp:
                                arr[j] = arr[j - gap]
                                j -= gap
                            arr[j] = temp
                        gap //= 2
                    return arr
                """,
                Map.of(
                        "pick_element", "Pick element to insert into its interleaved gap-spaced subarray.",
                        "compare_gap", "Compare element with its gap-distance predecessor.",
                        "shift_gap", "Shift predecessor forward by gap distance.",
                        "place_element", "Place current element into resolved gap position."
                )
        );
    }
}
