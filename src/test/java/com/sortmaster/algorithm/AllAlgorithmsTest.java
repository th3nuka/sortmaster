package com.sortmaster.algorithm;

import com.sortmaster.dto.SortResponse;
import com.sortmaster.exception.InvalidSortRequestException;
import com.sortmaster.model.SortingEvent;
import com.sortmaster.model.SortingEventType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class AllAlgorithmsTest {

    private List<SortingAlgorithm> algorithms;

    @BeforeEach
    void setUp() {
        algorithms = List.of(
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

    static Stream<SortingAlgorithm> algorithmProvider() {
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

    @Test
    @DisplayName("Verify exactly 16 sorting algorithms are registered")
    void testCountIs16() {
        assertEquals(16, algorithms.size());
    }

    @ParameterizedTest(name = "{0} should handle empty array")
    @MethodSource("algorithmProvider")
    void testEmptyArray(SortingAlgorithm algo) {
        int[] empty = new int[0];
        SortResponse res = algo.sort(empty, "NORMAL");
        assertNotNull(res);
        assertEquals(0, res.getSortedArray().length);
        assertFalse(res.getEvents().isEmpty());
    }

    @ParameterizedTest(name = "{0} should handle single-element array")
    @MethodSource("algorithmProvider")
    void testSingleElement(SortingAlgorithm algo) {
        int[] single = {99};
        SortResponse res = algo.sort(single, "NORMAL");
        assertNotNull(res);
        assertArrayEquals(new int[]{99}, res.getSortedArray());
    }

    @ParameterizedTest(name = "{0} should sort standard unsorted array")
    @MethodSource("algorithmProvider")
    void testStandardUnsortedArray(SortingAlgorithm algo) {
        int[] array = {45, 12, 85, 32, 89, 39, 69, 44, 42, 1, 45};
        int[] expected = Arrays.copyOf(array, array.length);
        Arrays.sort(expected);

        SortResponse res = algo.sort(array, "NORMAL");
        assertArrayEquals(expected, res.getSortedArray(), "Algorithm " + algo.getId() + " failed to sort array correctly.");
    }

    @ParameterizedTest(name = "{0} should sort already sorted array")
    @MethodSource("algorithmProvider")
    void testAlreadySorted(SortingAlgorithm algo) {
        int[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        SortResponse res = algo.sort(array, "NORMAL");
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, res.getSortedArray());
    }

    @ParameterizedTest(name = "{0} should sort reverse sorted array")
    @MethodSource("algorithmProvider")
    void testReverseSorted(SortingAlgorithm algo) {
        int[] array = {10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
        int[] expected = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        SortResponse res = algo.sort(array, "NORMAL");
        assertArrayEquals(expected, res.getSortedArray());
    }

    @ParameterizedTest(name = "{0} should sort duplicate values")
    @MethodSource("algorithmProvider")
    void testDuplicates(SortingAlgorithm algo) {
        int[] array = {5, 5, 5, 1, 2, 2, 8, 8, 8, 1, 5};
        int[] expected = Arrays.copyOf(array, array.length);
        Arrays.sort(expected);

        SortResponse res = algo.sort(array, "NORMAL");
        assertArrayEquals(expected, res.getSortedArray());
    }

    @ParameterizedTest(name = "{0} should handle mixed positive and negative numbers")
    @MethodSource("algorithmProvider")
    void testNegativesAndPositives(SortingAlgorithm algo) {
        int[] array = {-15, 8, -3, 0, 22, -15, 7, -1, 10, -8};
        int[] expected = Arrays.copyOf(array, array.length);
        Arrays.sort(expected);

        SortResponse res = algo.sort(array, "NORMAL");
        assertArrayEquals(expected, res.getSortedArray(), "Algorithm " + algo.getId() + " failed on negative numbers.");
    }

    private static int[] toDescending(int[] asc) {
        int[] desc = new int[asc.length];
        for (int i = 0; i < asc.length; i++) {
            desc[i] = asc[asc.length - 1 - i];
        }
        return desc;
    }

    @ParameterizedTest(name = "{0} should sort standard unsorted array in DESCENDING order")
    @MethodSource("algorithmProvider")
    void testStandardUnsortedArrayDescending(SortingAlgorithm algo) {
        int[] array = {45, 12, 85, 32, 89, 39, 69, 44, 42, 1, 45};
        int[] expectedAsc = Arrays.copyOf(array, array.length);
        Arrays.sort(expectedAsc);
        int[] expectedDesc = toDescending(expectedAsc);

        SortResponse res = algo.sort(array, "NORMAL", com.sortmaster.model.SortOrder.DESCENDING);
        assertArrayEquals(expectedDesc, res.getSortedArray(), "Algorithm " + algo.getId() + " failed to sort array in descending order.");
        assertEquals("DESCENDING", res.getOrder());
    }

    @ParameterizedTest(name = "{0} should sort already descending array in DESCENDING order")
    @MethodSource("algorithmProvider")
    void testAlreadyDescending(SortingAlgorithm algo) {
        int[] array = {10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
        SortResponse res = algo.sort(array, "NORMAL", com.sortmaster.model.SortOrder.DESCENDING);
        assertArrayEquals(new int[]{10, 9, 8, 7, 6, 5, 4, 3, 2, 1}, res.getSortedArray());
    }

    @ParameterizedTest(name = "{0} should sort ascending array in DESCENDING order")
    @MethodSource("algorithmProvider")
    void testAscendingToDescending(SortingAlgorithm algo) {
        int[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        SortResponse res = algo.sort(array, "NORMAL", com.sortmaster.model.SortOrder.DESCENDING);
        assertArrayEquals(new int[]{10, 9, 8, 7, 6, 5, 4, 3, 2, 1}, res.getSortedArray());
    }

    @ParameterizedTest(name = "{0} should sort duplicate values in DESCENDING order")
    @MethodSource("algorithmProvider")
    void testDuplicatesDescending(SortingAlgorithm algo) {
        int[] array = {5, 5, 5, 1, 2, 2, 8, 8, 8, 1, 5};
        int[] expectedAsc = Arrays.copyOf(array, array.length);
        Arrays.sort(expectedAsc);
        int[] expectedDesc = toDescending(expectedAsc);

        SortResponse res = algo.sort(array, "NORMAL", com.sortmaster.model.SortOrder.DESCENDING);
        assertArrayEquals(expectedDesc, res.getSortedArray());
    }

    @ParameterizedTest(name = "{0} should handle mixed positive and negative numbers in DESCENDING order")
    @MethodSource("algorithmProvider")
    void testNegativesAndPositivesDescending(SortingAlgorithm algo) {
        int[] array = {-15, 8, -3, 0, 22, -15, 7, -1, 10, -8};
        int[] expectedAsc = Arrays.copyOf(array, array.length);
        Arrays.sort(expectedAsc);
        int[] expectedDesc = toDescending(expectedAsc);

        SortResponse res = algo.sort(array, "NORMAL", com.sortmaster.model.SortOrder.DESCENDING);
        assertArrayEquals(expectedDesc, res.getSortedArray(), "Algorithm " + algo.getId() + " failed on negative numbers descending.");
    }

    @ParameterizedTest(name = "{0} should correctly sort random 30-element array in DESCENDING order")
    @MethodSource("algorithmProvider")
    void testRandomArrayDescending(SortingAlgorithm algo) {
        Random rand = new Random(42);
        int[] array = new int[30];
        for (int i = 0; i < array.length; i++) {
            array[i] = rand.nextInt(100) - 40;
        }
        int[] expectedAsc = Arrays.copyOf(array, array.length);
        Arrays.sort(expectedAsc);
        int[] expectedDesc = toDescending(expectedAsc);

        SortResponse res = algo.sort(array, "NORMAL", com.sortmaster.model.SortOrder.DESCENDING);
        assertArrayEquals(expectedDesc, res.getSortedArray());
    }

    @ParameterizedTest(name = "{0} should support switching from ascending to descending and back")
    @MethodSource("algorithmProvider")
    void testSwitchingAscendingDescending(SortingAlgorithm algo) {
        int[] array = {15, 3, 29, -4, 8, 15, 0};
        int[] expectedAsc = Arrays.copyOf(array, array.length);
        Arrays.sort(expectedAsc);
        int[] expectedDesc = toDescending(expectedAsc);

        SortResponse resAsc1 = algo.sort(array, "NORMAL", com.sortmaster.model.SortOrder.ASCENDING);
        assertArrayEquals(expectedAsc, resAsc1.getSortedArray());

        SortResponse resDesc = algo.sort(array, "NORMAL", com.sortmaster.model.SortOrder.DESCENDING);
        assertArrayEquals(expectedDesc, resDesc.getSortedArray());

        SortResponse resAsc2 = algo.sort(array, "NORMAL", com.sortmaster.model.SortOrder.ASCENDING);
        assertArrayEquals(expectedAsc, resAsc2.getSortedArray());
    }

    @ParameterizedTest(name = "{0} should handle empty array in DESCENDING order")
    @MethodSource("algorithmProvider")
    void testEmptyArrayDescending(SortingAlgorithm algo) {
        int[] empty = new int[0];
        SortResponse res = algo.sort(empty, "NORMAL", com.sortmaster.model.SortOrder.DESCENDING);
        assertNotNull(res);
        assertEquals(0, res.getSortedArray().length);
        assertFalse(res.getEvents().isEmpty());
    }

    @ParameterizedTest(name = "{0} should handle single-element array in DESCENDING order")
    @MethodSource("algorithmProvider")
    void testSingleElementDescending(SortingAlgorithm algo) {
        int[] single = {42};
        SortResponse res = algo.sort(single, "NORMAL", com.sortmaster.model.SortOrder.DESCENDING);
        assertNotNull(res);
        assertArrayEquals(new int[]{42}, res.getSortedArray());
    }

    @ParameterizedTest(name = "{0} should handle large array in DESCENDING order")
    @MethodSource("algorithmProvider")
    void testLargeArrayDescending(SortingAlgorithm algo) {
        Random rand = new Random(12345);
        int[] array = new int[60];
        for (int i = 0; i < array.length; i++) {
            array[i] = rand.nextInt(500) - 100;
        }
        int[] expectedAsc = Arrays.copyOf(array, array.length);
        Arrays.sort(expectedAsc);
        int[] expectedDesc = toDescending(expectedAsc);

        SortResponse res = algo.sort(array, "NORMAL", com.sortmaster.model.SortOrder.DESCENDING);
        assertArrayEquals(expectedDesc, res.getSortedArray(), "Algorithm " + algo.getId() + " failed on large array descending.");
    }

    @ParameterizedTest(name = "{0} snapshots must be non-empty and independent")
    @MethodSource("algorithmProvider")
    void testSnapshotImmutability(SortingAlgorithm algo) {
        int[] array = {12, 5, 8, 3, 10};
        SortResponse res = algo.sort(array, "NORMAL", com.sortmaster.model.SortOrder.DESCENDING);

        List<SortingEvent> events = res.getEvents();
        assertFalse(events.isEmpty());
        assertEquals(SortingEventType.START, events.get(0).getEventType());
        assertEquals(SortingEventType.COMPLETE, events.get(events.size() - 1).getEventType());

        // Snapshot immutability test
        SortingEvent ev = events.get(0);
        int[] snap = ev.getArray();
        int originalFirstVal = snap[0];
        snap[0] = 9999;
        assertEquals(originalFirstVal, ev.getArray()[0]);
    }

    @Test
    @DisplayName("Counting Sort and Pigeonhole Sort must reject excessive ranges to prevent OOM")
    void testRangeProtection() {
        int[] oversizedRange = {1, 500_000};
        CountingSort countingSort = new CountingSort();
        assertThrows(InvalidSortRequestException.class, () -> countingSort.sort(oversizedRange, "NORMAL"));

        PigeonholeSort pigeonholeSort = new PigeonholeSort();
        assertThrows(InvalidSortRequestException.class, () -> pigeonholeSort.sort(oversizedRange, "NORMAL"));
    }

    @ParameterizedTest(name = "{0} must provide non-null and valid Python implementation")
    @MethodSource("algorithmProvider")
    void testAllAlgorithmsHaveValidPythonCode(SortingAlgorithm algo) {
        assertNotNull(algo.getMetadata(), "Metadata must not be null");
        String pyCode = algo.getMetadata().getPythonCode();
        assertNotNull(pyCode, "Python code must not be null for " + algo.getId());
        assertFalse(pyCode.isBlank(), "Python code must not be blank for " + algo.getId());
        assertTrue(pyCode.contains("def "), "Python code must define a function for " + algo.getId());
        assertFalse(pyCode.contains("sorted("), "Must not use built-in sorted() for " + algo.getId());
    }

    @ParameterizedTest(name = "{0} must provide non-null and valid descending source code")
    @MethodSource("algorithmProvider")
    void testDescendingSourceCodeProvided(SortingAlgorithm algo) {
        assertNotNull(algo.getMetadata(), "Metadata must not be null");
        String javaDesc = algo.getMetadata().getJavaCode(com.sortmaster.model.SortOrder.DESCENDING);
        assertNotNull(javaDesc, "Java descending code must not be null for " + algo.getId());
        assertFalse(javaDesc.isBlank(), "Java descending code must not be blank for " + algo.getId());

        String pyDesc = algo.getMetadata().getPythonCode(com.sortmaster.model.SortOrder.DESCENDING);
        assertNotNull(pyDesc, "Python descending code must not be null for " + algo.getId());
        assertFalse(pyDesc.isBlank(), "Python descending code must not be blank for " + algo.getId());
        assertTrue(pyDesc.contains("def "), "Python descending code must define a function for " + algo.getId());

        String pseudoDesc = algo.getMetadata().getPseudocode(com.sortmaster.model.SortOrder.DESCENDING);
        assertNotNull(pseudoDesc, "Pseudocode descending must not be null for " + algo.getId());
        assertFalse(pseudoDesc.isBlank(), "Pseudocode descending must not be blank for " + algo.getId());
    }
}
