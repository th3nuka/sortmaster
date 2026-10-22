package com.sortmaster.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SortingEvent {
    private int stepIndex;
    private SortingEventType eventType;
    private int[] array;
    private List<Integer> indices;
    private String message;
    private int[] activeRange;
    private Integer pivotIndex;
    private List<Integer> sortedIndices;
    private String conceptualStep;

    public SortingEvent() {
    }

    public SortingEvent(int stepIndex,
                        SortingEventType eventType,
                        int[] array,
                        List<Integer> indices,
                        String message,
                        int[] activeRange,
                        Integer pivotIndex,
                        List<Integer> sortedIndices,
                        String conceptualStep) {
        this.stepIndex = stepIndex;
        this.eventType = eventType;
        this.array = array != null ? Arrays.copyOf(array, array.length) : new int[0];
        this.indices = indices != null ? new ArrayList<>(indices) : Collections.emptyList();
        this.message = message;
        this.activeRange = activeRange != null ? Arrays.copyOf(activeRange, activeRange.length) : null;
        this.pivotIndex = pivotIndex;
        this.sortedIndices = sortedIndices != null ? new ArrayList<>(sortedIndices) : Collections.emptyList();
        this.conceptualStep = conceptualStep;
    }

    public int getStepIndex() {
        return stepIndex;
    }

    public void setStepIndex(int stepIndex) {
        this.stepIndex = stepIndex;
    }

    public SortingEventType getEventType() {
        return eventType;
    }

    public void setEventType(SortingEventType eventType) {
        this.eventType = eventType;
    }

    public int[] getArray() {
        return array != null ? Arrays.copyOf(array, array.length) : new int[0];
    }

    public void setArray(int[] array) {
        this.array = array != null ? Arrays.copyOf(array, array.length) : new int[0];
    }

    public List<Integer> getIndices() {
        return indices;
    }

    public void setIndices(List<Integer> indices) {
        this.indices = indices;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int[] getActiveRange() {
        return activeRange != null ? Arrays.copyOf(activeRange, activeRange.length) : null;
    }

    public void setActiveRange(int[] activeRange) {
        this.activeRange = activeRange != null ? Arrays.copyOf(activeRange, activeRange.length) : null;
    }

    public Integer getPivotIndex() {
        return pivotIndex;
    }

    public void setPivotIndex(Integer pivotIndex) {
        this.pivotIndex = pivotIndex;
    }

    public List<Integer> getSortedIndices() {
        return sortedIndices;
    }

    public void setSortedIndices(List<Integer> sortedIndices) {
        this.sortedIndices = sortedIndices;
    }

    public String getConceptualStep() {
        return conceptualStep;
    }

    public void setConceptualStep(String conceptualStep) {
        this.conceptualStep = conceptualStep;
    }
}
