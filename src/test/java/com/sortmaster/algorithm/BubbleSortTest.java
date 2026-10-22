package com.sortmaster.algorithm;

import com.sortmaster.dto.SortResponse;
import com.sortmaster.model.SortingEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class BubbleSortTest {

    private BubbleSort bubbleSort;

    @BeforeEach
    void setUp() {
        bubbleSort = new BubbleSort();
    }

    @Test
    @DisplayName("Should handle empty array")
    void testEmptyArray() {
        int[] empty = new int[0];
        SortResponse response = bubbleSort.sort(empty, "NORMAL");
        assertNotNull(response);
        assertEquals(0, response.getSortedArray().length);
        assertFalse(response.getEvents().isEmpty());
    }

    @Test
    @DisplayName("Should handle single-element array")
    void testSingleElement() {
        int[] single = {42};
        SortResponse response = bubbleSort.sort(single, "NORMAL");
        assertNotNull(response);
        assertArrayEquals(new int[]{42}, response.getSortedArray());
    }

    @Test
    @DisplayName("Should sort standard unsorted array and match Arrays.sort")
    void testUnsortedArray() {
        int[] array = {64, 34, 25, 12, 22, 11, 90};
        int[] expected = Arrays.copyOf(array, array.length);
        Arrays.sort(expected);

        SortResponse response = bubbleSort.sort(array, "NORMAL");
        assertArrayEquals(expected, response.getSortedArray());
        assertTrue(response.getStatistics().getComparisons() > 0);
        assertTrue(response.getStatistics().getSwaps() > 0);
    }

    @Test
    @DisplayName("Should handle already sorted array with early exit in O(n)")
    void testAlreadySortedArray() {
        int[] array = {1, 2, 3, 4, 5, 6};
        SortResponse response = bubbleSort.sort(array, "NORMAL");
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6}, response.getSortedArray());
        assertEquals(0, response.getStatistics().getSwaps());
        assertEquals(5, response.getStatistics().getComparisons()); // Exactly n-1 comparisons
    }

    @Test
    @DisplayName("Should handle reverse sorted array")
    void testReverseSortedArray() {
        int[] array = {9, 8, 7, 6, 5, 4, 3, 2, 1};
        int[] expected = {1, 2, 3, 4, 5, 6, 7, 8, 9};

        SortResponse response = bubbleSort.sort(array, "NORMAL");
        assertArrayEquals(expected, response.getSortedArray());
    }

    @Test
    @DisplayName("Should handle duplicates and negative numbers")
    void testDuplicatesAndNegatives() {
        int[] array = {-5, 10, -5, 0, 7, -2, 10, 3};
        int[] expected = Arrays.copyOf(array, array.length);
        Arrays.sort(expected);

        SortResponse response = bubbleSort.sort(array, "NORMAL");
        assertArrayEquals(expected, response.getSortedArray());
    }

    @Test
    @DisplayName("Event array snapshots must be independent and immutable across events")
    void testSnapshotImmutability() {
        int[] array = {5, 1, 4, 2, 8};
        SortResponse response = bubbleSort.sort(array, "NORMAL");

        // The first event should reflect initial array or start
        SortingEvent firstEvent = response.getEvents().get(0);
        int[] firstArray = firstEvent.getArray();

        // Mutating firstArray must not affect the event or later events
        firstArray[0] = 99999;
        assertNotEquals(99999, firstEvent.getArray()[0]);

        // Verify final event has sorted array
        SortingEvent lastEvent = response.getEvents().get(response.getEvents().size() - 1);
        int[] lastArray = lastEvent.getArray();
        int[] expected = {1, 2, 4, 5, 8};
        assertArrayEquals(expected, lastArray);
    }
}
