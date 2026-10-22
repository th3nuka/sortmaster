package com.sortmaster.algorithm;

import com.sortmaster.dto.SortResponse;
import com.sortmaster.model.SortOrder;
import com.sortmaster.model.SortStatistics;
import com.sortmaster.model.SortingEvent;
import com.sortmaster.model.SortingEventType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class AlgorithmAnimationVerificationTest {

    static Stream<SortingAlgorithm> allAlgorithms() {
        return Stream.of(
                new BubbleSort(),
                new SelectionSort(),
                new InsertionSort(),
                new MergeSort(),
                new QuickSort(),
                new HeapSort(),
                new ShellSort(),
                new CountingSort(),
                new RadixSort(),
                new BucketSort(),
                new CocktailShakerSort(),
                new CombSort(),
                new GnomeSort(),
                new CycleSort(),
                new PigeonholeSort(),
                new TimSort()
        );
    }

    private static int[] toDescending(int[] asc) {
        int[] desc = new int[asc.length];
        for (int i = 0; i < asc.length; i++) {
            desc[i] = asc[asc.length - 1 - i];
        }
        return desc;
    }

    private void verifyAnimationAndOutput(SortingAlgorithm algo, int[] initialArray, SortOrder order) {
        int[] originalCopy = Arrays.copyOf(initialArray, initialArray.length);

        SortResponse response = algo.sort(initialArray, "NORMAL", order);

        // 1. Initial array immutability
        assertArrayEquals(originalCopy, initialArray, "Algorithm " + algo.getId() + " mutated the input array!");

        // 2. Response sanity
        assertNotNull(response);
        assertEquals(algo.getId(), response.getAlgorithmId());
        assertEquals(order.name(), response.getOrder());

        int[] finalArray = response.getSortedArray();
        assertNotNull(finalArray);
        assertEquals(initialArray.length, finalArray.length);

        // 3. Expected sorted array verification
        int[] expected = Arrays.copyOf(originalCopy, originalCopy.length);
        Arrays.sort(expected);
        if (order == SortOrder.DESCENDING) {
            expected = toDescending(expected);
        }
        assertArrayEquals(expected, finalArray, "Algorithm " + algo.getId() + " final array mismatch for order " + order);

        // 4. Directional validation
        for (int i = 0; i < finalArray.length - 1; i++) {
            if (order == SortOrder.ASCENDING) {
                assertTrue(finalArray[i] <= finalArray[i + 1],
                        "Ascending violation at index " + i + " in " + algo.getId() + ": " + Arrays.toString(finalArray));
            } else {
                assertTrue(finalArray[i] >= finalArray[i + 1],
                        "Descending violation at index " + i + " in " + algo.getId() + ": " + Arrays.toString(finalArray));
            }
        }

        // 5. Animation events and intermediate snapshots validation
        List<SortingEvent> events = response.getEvents();
        assertNotNull(events);
        assertFalse(events.isEmpty(), "Events must not be empty for " + algo.getId());

        assertEquals(SortingEventType.START, events.get(0).getEventType(), "First event must be START in " + algo.getId());
        assertEquals(SortingEventType.COMPLETE, events.get(events.size() - 1).getEventType(), "Last event must be COMPLETE in " + algo.getId());

        int expectedStep = 1;
        for (SortingEvent ev : events) {
            assertEquals(expectedStep, ev.getStepIndex(), "Step index must be sequential in " + algo.getId());
            expectedStep++;

            int[] snapshot = ev.getArray();
            assertNotNull(snapshot, "Snapshot array must not be null in " + algo.getId() + " step " + ev.getStepIndex());
            assertEquals(initialArray.length, snapshot.length,
                    "Snapshot array length must match initial array length in " + algo.getId() + " step " + ev.getStepIndex());

            // Validate event indices are within bounds
            List<Integer> indices = ev.getIndices();
            if (indices != null && !indices.isEmpty()) {
                for (int idx : indices) {
                    if (initialArray.length > 0) {
                        assertTrue(idx >= 0 && idx < initialArray.length,
                                "Index " + idx + " out of bounds [0, " + initialArray.length + ") in " + algo.getId() + " event " + ev.getEventType());
                    }
                }
            }

            // Pivot index check
            if (ev.getPivotIndex() != null && initialArray.length > 0) {
                assertTrue(ev.getPivotIndex() >= 0 && ev.getPivotIndex() < initialArray.length,
                        "Pivot index " + ev.getPivotIndex() + " out of bounds in " + algo.getId());
            }
        }

        // 6. Statistics validation
        SortStatistics stats = response.getStatistics();
        assertNotNull(stats);
        assertEquals(events.size(), stats.getTotalSteps(), "Total steps in stats must match events size");
        assertTrue(stats.getComparisons() >= 0, "Comparisons count must be non-negative");
        assertTrue(stats.getSwaps() >= 0, "Swaps count must be non-negative");
        assertTrue(stats.getWrites() >= 0, "Writes count must be non-negative");
    }

    @Test
    @DisplayName("Bubble Sort specific verification on [5, 2, 8, 1, 4] for Ascending and Descending")
    void testBubbleSortRequirementSample() {
        BubbleSort bubbleSort = new BubbleSort();
        int[] input = {5, 2, 8, 1, 4};

        // Ascending
        SortResponse ascRes = bubbleSort.sort(input, "NORMAL", SortOrder.ASCENDING);
        assertArrayEquals(new int[]{1, 2, 4, 5, 8}, ascRes.getSortedArray());
        assertEquals("Bubble Sort", ascRes.getAlgorithmName());

        // Descending
        SortResponse descRes = bubbleSort.sort(input, "NORMAL", SortOrder.DESCENDING);
        assertArrayEquals(new int[]{8, 5, 4, 2, 1}, descRes.getSortedArray());
    }

    @ParameterizedTest(name = "{0} - Ascending Standard Unsorted")
    @MethodSource("allAlgorithms")
    void testAscendingStandard(SortingAlgorithm algo) {
        int[] input = {45, 12, 85, 32, 89, 39, 69, 44, 42, 1, 45};
        verifyAnimationAndOutput(algo, input, SortOrder.ASCENDING);
    }

    @ParameterizedTest(name = "{0} - Descending Standard Unsorted")
    @MethodSource("allAlgorithms")
    void testDescendingStandard(SortingAlgorithm algo) {
        int[] input = {45, 12, 85, 32, 89, 39, 69, 44, 42, 1, 45};
        verifyAnimationAndOutput(algo, input, SortOrder.DESCENDING);
    }

    @ParameterizedTest(name = "{0} - Ascending with Duplicates")
    @MethodSource("allAlgorithms")
    void testAscendingDuplicates(SortingAlgorithm algo) {
        int[] input = {5, 2, 5, 1, 5, 2, 8, 8, 1};
        verifyAnimationAndOutput(algo, input, SortOrder.ASCENDING);
    }

    @ParameterizedTest(name = "{0} - Descending with Duplicates")
    @MethodSource("allAlgorithms")
    void testDescendingDuplicates(SortingAlgorithm algo) {
        int[] input = {5, 2, 5, 1, 5, 2, 8, 8, 1};
        verifyAnimationAndOutput(algo, input, SortOrder.DESCENDING);
    }

    @ParameterizedTest(name = "{0} - Ascending Already Sorted")
    @MethodSource("allAlgorithms")
    void testAscendingAlreadySorted(SortingAlgorithm algo) {
        int[] input = {1, 2, 3, 4, 5, 6, 7, 8};
        verifyAnimationAndOutput(algo, input, SortOrder.ASCENDING);
    }

    @ParameterizedTest(name = "{0} - Descending Already Sorted")
    @MethodSource("allAlgorithms")
    void testDescendingAlreadySorted(SortingAlgorithm algo) {
        int[] input = {8, 7, 6, 5, 4, 3, 2, 1};
        verifyAnimationAndOutput(algo, input, SortOrder.DESCENDING);
    }

    @ParameterizedTest(name = "{0} - Ascending Reverse Sorted")
    @MethodSource("allAlgorithms")
    void testAscendingReverseSorted(SortingAlgorithm algo) {
        int[] input = {8, 7, 6, 5, 4, 3, 2, 1};
        verifyAnimationAndOutput(algo, input, SortOrder.ASCENDING);
    }

    @ParameterizedTest(name = "{0} - Descending Reverse Sorted")
    @MethodSource("allAlgorithms")
    void testDescendingReverseSorted(SortingAlgorithm algo) {
        int[] input = {1, 2, 3, 4, 5, 6, 7, 8};
        verifyAnimationAndOutput(algo, input, SortOrder.DESCENDING);
    }

    @ParameterizedTest(name = "{0} - Ascending Negative Numbers")
    @MethodSource("allAlgorithms")
    void testAscendingNegatives(SortingAlgorithm algo) {
        int[] input = {-15, 8, -3, 0, 22, -15, 7, -1, 10, -8};
        verifyAnimationAndOutput(algo, input, SortOrder.ASCENDING);
    }

    @ParameterizedTest(name = "{0} - Descending Negative Numbers")
    @MethodSource("allAlgorithms")
    void testDescendingNegatives(SortingAlgorithm algo) {
        int[] input = {-15, 8, -3, 0, 22, -15, 7, -1, 10, -8};
        verifyAnimationAndOutput(algo, input, SortOrder.DESCENDING);
    }

    @ParameterizedTest(name = "{0} - Ascending Single Element")
    @MethodSource("allAlgorithms")
    void testAscendingSingleElement(SortingAlgorithm algo) {
        int[] input = {42};
        verifyAnimationAndOutput(algo, input, SortOrder.ASCENDING);
    }

    @ParameterizedTest(name = "{0} - Descending Single Element")
    @MethodSource("allAlgorithms")
    void testDescendingSingleElement(SortingAlgorithm algo) {
        int[] input = {42};
        verifyAnimationAndOutput(algo, input, SortOrder.DESCENDING);
    }

    @ParameterizedTest(name = "{0} - Ascending Empty Array")
    @MethodSource("allAlgorithms")
    void testAscendingEmptyArray(SortingAlgorithm algo) {
        int[] input = new int[0];
        verifyAnimationAndOutput(algo, input, SortOrder.ASCENDING);
    }

    @ParameterizedTest(name = "{0} - Descending Empty Array")
    @MethodSource("allAlgorithms")
    void testDescendingEmptyArray(SortingAlgorithm algo) {
        int[] input = new int[0];
        verifyAnimationAndOutput(algo, input, SortOrder.DESCENDING);
    }

    @ParameterizedTest(name = "{0} - Ascending Large Random Array (50 items)")
    @MethodSource("allAlgorithms")
    void testAscendingLargeArray(SortingAlgorithm algo) {
        Random rand = new Random(999);
        int[] input = new int[50];
        for (int i = 0; i < input.length; i++) {
            input[i] = rand.nextInt(200) - 50;
        }
        verifyAnimationAndOutput(algo, input, SortOrder.ASCENDING);
    }

    @ParameterizedTest(name = "{0} - Descending Large Random Array (50 items)")
    @MethodSource("allAlgorithms")
    void testDescendingLargeArray(SortingAlgorithm algo) {
        Random rand = new Random(999);
        int[] input = new int[50];
        for (int i = 0; i < input.length; i++) {
            input[i] = rand.nextInt(200) - 50;
        }
        verifyAnimationAndOutput(algo, input, SortOrder.DESCENDING);
    }
}
