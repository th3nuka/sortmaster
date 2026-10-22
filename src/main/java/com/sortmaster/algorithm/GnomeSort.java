package com.sortmaster.algorithm;

import com.sortmaster.model.AlgorithmMetadata;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class GnomeSort extends AbstractSortingAlgorithm {

    public static final String ID = "gnome-sort";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    protected void executeSort(int[] array, String detailLevel) {
        int n = array.length;
        int index = 0;

        while (index < n) {
            if (index == 0) {
                index++;
            } else {
                int cmp = compare(array, index, index - 1, "compare_gnome");
                boolean inOrder = isAscending() ? (cmp >= 0) : (cmp <= 0);

                if (inOrder) {
                    // In order: advance gnome forward
                    index++;
                } else {
                    // Out of order: swap and step gnome backward
                    swap(array, index, index - 1, "swap_gnome");
                    index--;
                }
            }
        }
    }

    @Override
    public AlgorithmMetadata getMetadata() {
        return new AlgorithmMetadata(
                ID,
                "Gnome Sort",
                "Exchange Sort",
                "A delightfully simple sorting algorithm based on how a garden gnome sorts a line of flower pots.",
                "Gnome Sort (originally named 'Stupid Sort' by Hamid Sarbazi-Azad in 2000, then described by Dick Grune as Gnome Sort) is conceptually the simplest comparison sort. Imagine a garden gnome sorting flower pots: he looks at the pot next to him and the previous pot. If they are in order, he steps forward to the next pot. If out of order, he swaps them and steps one pot backward. He continues until reaching the end. It behaves like Insertion Sort implemented without nested loops.",
                "O(n)",
                "O(n²)",
                "O(n²)",
                "O(1)",
                true,
                true,
                "Easy",
                List.of(
                        "Teaching basic algorithm state machines without nested loop constructs",
                        "Tiny datasets or nearly-sorted lists where code simplicity is prioritized",
                        "Hardware state-machine pipelines with single-register pointer controls"
                ),
                List.of(
                        "Remarkably simple: uses a single index pointer and no nested loops",
                        "Stable: adjacent swaps preserve identical element order",
                        "In-place: requires strictly O(1) auxiliary space",
                        "Adaptive: runs in linear O(n) time on already sorted arrays"
                ),
                List.of(
                        "O(n²) average and worst-case time complexity",
                        "Performs many redundant single-step backwards traversals",
                        "Inferior to Insertion Sort in raw operation count on random data"
                ),
                List.of(
                        "Failing to handle index == 0 by advancing to 1, causing ArrayIndexOutOfBoundsException (-1)",
                        "Thinking Gnome Sort requires nested loops",
                        "Using strict inequality instead of non-strict, which destroys stability"
                ),
                List.of(
                        "How is Gnome Sort conceptually related to Insertion Sort? (Answer: It performs the same element shifts, but through repeated adjacent swaps and backwards traversal)",
                        "What is the maximum number of swaps Gnome Sort performs in reverse sorted input? (Answer: n(n-1)/2)",
                        "Why does Gnome Sort execute in O(n) time for already sorted data?"
                ),
                """
                procedure gnomeSort(A)
                    pos = 0
                    while pos < length(A) do
                        if pos == 0 or A[pos] >= A[pos-1] then
                            pos = pos + 1
                        else
                            swap(A[pos], A[pos-1])
                            pos = pos - 1
                        end if
                    end while
                end procedure
                """,
                """
                public void gnomeSort(int[] arr) {
                    int pos = 0;
                    while (pos < arr.length) {
                        if (pos == 0 || arr[pos] >= arr[pos - 1]) {
                            pos++;
                        } else {
                            int temp = arr[pos];
                            arr[pos] = arr[pos - 1];
                            arr[pos - 1] = temp;
                            pos--;
                        }
                    }
                }
                """,
                """
                def gnome_sort(arr: list[int]) -> list[int]:
                    n = len(arr)
                    pos = 0
                    while pos < n:
                        if pos == 0 or arr[pos] >= arr[pos - 1]:
                            pos += 1
                        else:
                            arr[pos], arr[pos - 1] = arr[pos - 1], arr[pos]
                            pos -= 1
                    return arr
                """,
                """
                procedure gnomeSortDescending(A)
                    pos = 0
                    while pos < length(A) do
                        if pos == 0 or A[pos] <= A[pos-1] then
                            pos = pos + 1
                        else
                            swap(A[pos], A[pos-1])
                            pos = pos - 1
                        end if
                    end while
                end procedure
                """,
                """
                public void gnomeSortDescending(int[] arr) {
                    int pos = 0;
                    while (pos < arr.length) {
                        if (pos == 0 || arr[pos] <= arr[pos - 1]) {
                            pos++;
                        } else {
                            int temp = arr[pos];
                            arr[pos] = arr[pos - 1];
                            arr[pos - 1] = temp;
                            pos--;
                        }
                    }
                }
                """,
                """
                def gnome_sort_descending(arr: list[int]) -> list[int]:
                    n = len(arr)
                    pos = 0
                    while pos < n:
                        if pos == 0 or arr[pos] <= arr[pos - 1]:
                            pos += 1
                        else:
                            arr[pos], arr[pos - 1] = arr[pos - 1], arr[pos]
                            pos -= 1
                    return arr
                """,
                Map.of(
                        "compare_gnome", "Gnome checks if current pot is in order with the previous pot.",
                        "swap_gnome", "Gnome swaps the two out-of-order pots and steps one position backward."
                )
        );
    }
}
