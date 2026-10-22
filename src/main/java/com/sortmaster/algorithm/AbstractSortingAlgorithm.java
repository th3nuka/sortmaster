package com.sortmaster.algorithm;

import com.sortmaster.dto.SortResponse;
import com.sortmaster.model.AlgorithmMetadata;
import com.sortmaster.model.SortOrder;
import com.sortmaster.model.SortStatistics;
import com.sortmaster.model.SortingEvent;
import com.sortmaster.model.SortingEventType;

import java.util.*;

public abstract class AbstractSortingAlgorithm implements SortingAlgorithm {

    protected static final int MAX_EVENTS = 5000;

    protected int stepCounter = 0;
    protected long comparisons = 0;
    protected long swaps = 0;
    protected long writes = 0;
    protected List<SortingEvent> events = new ArrayList<>();
    protected Set<Integer> sortedIndices = new HashSet<>();
    protected boolean eventLimitReached = false;
    protected SortOrder order = SortOrder.ASCENDING;

    @Override
    public SortResponse sort(int[] initialArray, String detailLevel) {
        return sort(initialArray, detailLevel, SortOrder.ASCENDING);
    }

    @Override
    public SortResponse sort(int[] initialArray, String detailLevel, SortOrder sortOrder) {
        this.order = sortOrder != null ? sortOrder : SortOrder.ASCENDING;
        resetState();
        int[] workArray = initialArray != null ? Arrays.copyOf(initialArray, initialArray.length) : new int[0];
        long startTime = System.nanoTime();

        emitStart(workArray, "Beginning " + getMetadata().getName() + " in " + this.order.getDisplayName().toUpperCase() + " order (" + this.order.getDescription() + ") on " + workArray.length + " elements.");

        if (workArray.length <= 1) {
            if (workArray.length == 1) {
                sortedIndices.add(0);
                emitMarkSorted(workArray, List.of(0), "Single element array is already sorted.", "complete");
            }
            emitComplete(workArray, "Sorting completed.");
            long elapsed = System.nanoTime() - startTime;
            SortStatistics stats = new SortStatistics(comparisons, swaps, writes, elapsed, events.size());
            return new SortResponse(getId(), getMetadata().getName(), initialArray, workArray, events, stats, getMetadata(), this.order.name());
        }

        executeSort(workArray, detailLevel);

        // Mark all as sorted at the end
        List<Integer> allIndices = new ArrayList<>(workArray.length);
        for (int i = 0; i < workArray.length; i++) {
            allIndices.add(i);
        }
        sortedIndices.addAll(allIndices);

        emitComplete(workArray, getMetadata().getName() + " completed successfully in " + this.order.getDisplayName() + " order! Array is fully sorted.");

        long elapsed = System.nanoTime() - startTime;
        SortStatistics stats = new SortStatistics(comparisons, swaps, writes, elapsed, events.size());
        SortResponse response = new SortResponse(getId(), getMetadata().getName(), initialArray, workArray, events, stats, getMetadata(), this.order.name());
        if (eventLimitReached) {
            response.setTruncated(true);
            response.setMessage("Event stream reached max display limit (" + MAX_EVENTS + " events) to protect browser memory.");
        }
        return response;
    }

    protected abstract void executeSort(int[] array, String detailLevel);

    protected void resetState() {
        stepCounter = 0;
        comparisons = 0;
        swaps = 0;
        writes = 0;
        events = new ArrayList<>();
        sortedIndices = new HashSet<>();
        eventLimitReached = false;
    }

    public SortOrder getOrder() {
        return order;
    }

    public String getOrderName() {
        return order != null ? order.getDisplayName() : "Ascending";
    }

    public boolean isAscending() {
        return order == SortOrder.ASCENDING;
    }

    public boolean isDescending() {
        return order == SortOrder.DESCENDING;
    }

    /**
     * Determines whether 'first' and 'second' are out of order according to the active sorting order.
     * Ascending: true if first > second
     * Descending: true if first < second
     */
    protected boolean isOutOfOrder(int first, int second) {
        return isAscending() ? (first > second) : (first < second);
    }

    /**
     * Determines whether 'first' comes before or equals 'second' according to the active sorting order.
     * Ascending: true if first <= second
     * Descending: true if first >= second
     */
    protected boolean isBeforeOrEqual(int first, int second) {
        return isAscending() ? (first <= second) : (first >= second);
    }

    /**
     * Determines whether 'first' strictly precedes 'second' according to the active sorting order.
     * Ascending: true if first < second
     * Descending: true if first > second
     */
    protected boolean isStrictlyBefore(int first, int second) {
        return isAscending() ? (first < second) : (first > second);
    }

    /**
     * Compares two elements according to active sorting order and emits a COMPARE event.
     * Returns > 0 if arr[i] belongs after arr[j] (i.e. out of order).
     * Returns < 0 if arr[i] belongs before arr[j].
     */
    protected int compareOrdered(int[] arr, int i, int j, String conceptualStep) {
        emitCompare(arr, i, j,
                "Comparing index " + i + " (" + arr[i] + ") with index " + j + " (" + arr[j] + ") in " + order.getDisplayName().toLowerCase() + " order.",
                conceptualStep);
        return isAscending() ? Integer.compare(arr[i], arr[j]) : Integer.compare(arr[j], arr[i]);
    }

    protected void recordEvent(SortingEventType type,
                               int[] array,
                               List<Integer> indices,
                               String message,
                               int[] activeRange,
                               Integer pivotIndex,
                               String conceptualStep) {
        if (events.size() >= MAX_EVENTS) {
            eventLimitReached = true;
            return;
        }
        stepCounter++;
        SortingEvent event = new SortingEvent(
                stepCounter,
                type,
                array,
                indices,
                message,
                activeRange,
                pivotIndex,
                new ArrayList<>(sortedIndices),
                conceptualStep
        );
        events.add(event);
    }

    protected void emitStart(int[] arr, String message) {
        recordEvent(SortingEventType.START, arr, Collections.emptyList(), message, null, null, "start");
    }

    protected void emitCompare(int[] arr, int i, int j, String message, String conceptualStep) {
        comparisons++;
        recordEvent(SortingEventType.COMPARE, arr, List.of(i, j), message, null, null, conceptualStep);
    }

    protected void emitSwap(int[] arr, int i, int j, String message, String conceptualStep) {
        swaps++;
        writes += 2;
        recordEvent(SortingEventType.SWAP, arr, List.of(i, j), message, null, null, conceptualStep);
    }

    protected void emitWrite(int[] arr, int index, int value, String message, String conceptualStep) {
        writes++;
        arr[index] = value;
        recordEvent(SortingEventType.WRITE, arr, List.of(index), message, null, null, conceptualStep);
    }

    protected void emitInsert(int[] arr, int index, String message, String conceptualStep) {
        writes++;
        recordEvent(SortingEventType.INSERT, arr, List.of(index), message, null, null, conceptualStep);
    }

    protected void emitPivot(int[] arr, int pivotIdx, int[] range, String message, String conceptualStep) {
        recordEvent(SortingEventType.PIVOT, arr, List.of(pivotIdx), message, range, pivotIdx, conceptualStep);
    }

    protected void emitPartition(int[] arr, int left, int right, Integer pivotIdx, String message, String conceptualStep) {
        recordEvent(SortingEventType.PARTITION, arr, List.of(left, right), message, new int[]{left, right}, pivotIdx, conceptualStep);
    }

    protected void emitMerge(int[] arr, int left, int mid, int right, String message, String conceptualStep) {
        recordEvent(SortingEventType.MERGE, arr, List.of(left, mid, right), message, new int[]{left, right}, null, conceptualStep);
    }

    protected void emitMarkSorted(int[] arr, List<Integer> indices, String message, String conceptualStep) {
        sortedIndices.addAll(indices);
        recordEvent(SortingEventType.MARK_SORTED, arr, indices, message, null, null, conceptualStep);
    }

    protected void emitComplete(int[] arr, String message) {
        recordEvent(SortingEventType.COMPLETE, arr, Collections.emptyList(), message, null, null, "complete");
    }

    protected void swap(int[] arr, int i, int j, String conceptualStep) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
        emitSwap(arr, i, j, "Swapped elements at index " + i + " (" + arr[j] + ") and index " + j + " (" + arr[i] + ").", conceptualStep);
    }

    protected int compare(int[] arr, int i, int j, String conceptualStep) {
        emitCompare(arr, i, j, "Comparing index " + i + " (" + arr[i] + ") with index " + j + " (" + arr[j] + ").", conceptualStep);
        return Integer.compare(arr[i], arr[j]);
    }
}
