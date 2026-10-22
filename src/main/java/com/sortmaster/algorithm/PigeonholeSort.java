package com.sortmaster.algorithm;

import com.sortmaster.exception.InvalidSortRequestException;
import com.sortmaster.model.AlgorithmMetadata;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class PigeonholeSort extends AbstractSortingAlgorithm {

    public static final String ID = "pigeonhole-sort";
    private static final int MAX_SAFE_RANGE = 100_000;

    @Override
    public String getId() {
        return ID;
    }

    @Override
    protected void executeSort(int[] array, String detailLevel) {
        int n = array.length;
        if (n <= 1) return;

        int min = array[0];
        int max = array[0];
        for (int x : array) {
            if (x < min) min = x;
            if (x > max) max = x;
        }

        long range = (long) max - min + 1;
        if (range > MAX_SAFE_RANGE) {
            throw new InvalidSortRequestException("Pigeonhole Sort range (" + range + ") exceeds safe limit (" + MAX_SAFE_RANGE + ").");
        }

        emitStart(array, "Setting up " + range + " pigeonholes spanning values [" + min + ".." + max + "].");

        List<List<Integer>> holes = new ArrayList<>((int) range);
        for (int i = 0; i < range; i++) {
            holes.add(new ArrayList<>());
        }

        // Put every element in its respective pigeonhole
        for (int i = 0; i < n; i++) {
            int val = array[i];
            int holeIdx = val - min;
            holes.get(holeIdx).add(val);
            emitCompare(array, i, i,
                    "Placed element " + val + " into pigeonhole #" + holeIdx + " (value - " + min + ").",
                    "put_hole");
        }

        // Traverse through all holes and put elements back into array
        int index = 0;
        int startHole = isAscending() ? 0 : (int) range - 1;
        int endHole = isAscending() ? (int) range : -1;
        int step = isAscending() ? 1 : -1;

        for (int count = startHole; count != endHole; count += step) {
            List<Integer> hole = holes.get(count);
            for (int val : hole) {
                array[index] = val;
                emitWrite(array, index, val,
                        "Retrieved value " + val + " from pigeonhole #" + count + " and placed at index " + index + " (" + getOrderName().toLowerCase() + ").",
                        "retrieve_hole");
                emitMarkSorted(array, List.of(index),
                        "Position " + index + " sorted.",
                        "mark_sorted");
                index++;
            }
        }
    }

    @Override
    public AlgorithmMetadata getMetadata() {
        return new AlgorithmMetadata(
                ID,
                "Pigeonhole Sort",
                "Distribution Sort",
                "Places each element into a dedicated 'hole' indexed by its key, then iterates sequentially through the holes.",
                "Pigeonhole Sort is a distribution sorting algorithm suitable when the number of elements (n) and the range of possible key values (Range) are approximately equal. Based on the Dirichlet Drawer / Pigeonhole Principle, it assigns each item to a pigeonhole labeled with its value. Once all items are pigeonholed, it visits each hole in ascending order (or descending order from highest to lowest) to reconstruct the fully sorted array. It preserves stability and handles negative values via minimum offsets.",
                "O(n + Range)",
                "O(n + Range)",
                "O(n + Range)",
                "O(n + Range)",
                true,
                false,
                "Medium",
                List.of(
                        "Sorting keys where range is close to the number of elements n",
                        "Demonstrating the Pigeonhole Principle in algorithmic computer science",
                        "Direct key indexing without comparison logic"
                ),
                List.of(
                        "Linear O(n + Range) time complexity",
                        "Completely stable: preserves original order of duplicates within each hole",
                        "Very simple to visualize and comprehend"
                ),
                List.of(
                        "Excessive memory consumption if Range is significantly larger than n",
                        "Restricted to integer keys or discrete mappable values",
                        "Not in-place: requires lists of pigeonholes"
                ),
                List.of(
                        "Allocating huge arrays when input numbers have large gaps (e.g., [1, 1000000])",
                        "Confusing Pigeonhole Sort with Counting Sort (Counting Sort stores counts; Pigeonhole stores actual elements/lists)",
                        "Index calculation errors without subtracting the minimum value"
                ),
                List.of(
                        "What is the key difference between Pigeonhole Sort and Counting Sort?",
                        "What is the Pigeonhole Principle, and how does it relate to duplicate keys?",
                        "Under what conditions does Pigeonhole Sort become impractical?"
                ),
                """
                procedure pigeonholeSort(A)
                    min = min(A), max = max(A)
                    range = max - min + 1
                    create array of empty lists 'holes' of size range
                    for x in A do
                        holes[x - min].append(x)
                    end for
                    index = 0
                    for hole in holes do
                        for x in hole do
                            A[index] = x
                            index = index + 1
                        end for
                    end for
                end procedure
                """,
                """
                public void pigeonholeSort(int[] arr) {
                    if (arr.length <= 1) return;
                    int min = arr[0], max = arr[0];
                    for (int x : arr) {
                        if (x < min) min = x;
                        if (x > max) max = x;
                    }
                    int range = max - min + 1;
                    List<Integer>[] holes = new ArrayList[range];
                    for (int i = 0; i < range; i++) holes[i] = new ArrayList<>();
                    for (int x : arr) holes[x - min].add(x);
                    int idx = 0;
                    for (List<Integer> hole : holes) {
                        for (int x : hole) arr[idx++] = x;
                    }
                }
                """,
                """
                def pigeonhole_sort(arr: list[int]) -> list[int]:
                    if len(arr) <= 1:
                        return arr
                    min_val, max_val = min(arr), max(arr)
                    val_range = max_val - min_val + 1
                    if val_range > 100_000:
                        raise ValueError("Range exceeds safe memory limit for Pigeonhole Sort.")
                    holes = [[] for _ in range(val_range)]
                    for x in arr:
                        holes[x - min_val].append(x)
                    idx = 0
                    for hole in holes:
                        for x in hole:
                            arr[idx] = x
                            idx += 1
                    return arr
                """,
                """
                procedure pigeonholeSortDescending(A)
                    min = min(A), max = max(A)
                    range = max - min + 1
                    create array of empty lists 'holes' of size range
                    for x in A do
                        holes[x - min].append(x)
                    end for
                    index = 0
                    for count from range - 1 down to 0 do
                        for x in holes[count] do
                            A[index] = x
                            index = index + 1
                        end for
                    end for
                end procedure
                """,
                """
                public void pigeonholeSortDescending(int[] arr) {
                    if (arr.length <= 1) return;
                    int min = arr[0], max = arr[0];
                    for (int x : arr) {
                        if (x < min) min = x;
                        if (x > max) max = x;
                    }
                    int range = max - min + 1;
                    List<Integer>[] holes = new ArrayList[range];
                    for (int i = 0; i < range; i++) holes[i] = new ArrayList<>();
                    for (int x : arr) holes[x - min].add(x);
                    int idx = 0;
                    for (int i = range - 1; i >= 0; i--) {
                        for (int x : holes[i]) arr[idx++] = x;
                    }
                }
                """,
                """
                def pigeonhole_sort_descending(arr: list[int]) -> list[int]:
                    if len(arr) <= 1:
                        return arr
                    min_val, max_val = min(arr), max(arr)
                    val_range = max_val - min_val + 1
                    if val_range > 100_000:
                        raise ValueError("Range exceeds safe memory limit for Pigeonhole Sort.")
                    holes = [[] for _ in range(val_range)]
                    for x in arr:
                        holes[x - min_val].append(x)
                    idx = 0
                    for count in range(val_range - 1, -1, -1):
                        for x in holes[count]:
                            arr[idx] = x
                            idx += 1
                    return arr
                """,
                Map.of(
                        "put_hole", "Deposit element into its dedicated value-indexed pigeonhole.",
                        "retrieve_hole", "Extract elements sequentially from pigeonholes back to the array."
                )
        );
    }
}
