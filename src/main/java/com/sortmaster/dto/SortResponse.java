package com.sortmaster.dto;

import com.sortmaster.model.AlgorithmMetadata;
import com.sortmaster.model.SortStatistics;
import com.sortmaster.model.SortingEvent;

import java.util.Arrays;
import java.util.List;

public class SortResponse {
    private String algorithmId;
    private String algorithmName;
    private int[] initialArray;
    private int[] sortedArray;
    private List<SortingEvent> events;
    private SortStatistics statistics;
    private AlgorithmMetadata metadata;
    private boolean truncated;
    private String message;
    private String order = "ASCENDING";

    public SortResponse() {
    }

    public SortResponse(String algorithmId,
                        String algorithmName,
                        int[] initialArray,
                        int[] sortedArray,
                        List<SortingEvent> events,
                        SortStatistics statistics,
                        AlgorithmMetadata metadata) {
        this(algorithmId, algorithmName, initialArray, sortedArray, events, statistics, metadata, "ASCENDING");
    }

    public SortResponse(String algorithmId,
                        String algorithmName,
                        int[] initialArray,
                        int[] sortedArray,
                        List<SortingEvent> events,
                        SortStatistics statistics,
                        AlgorithmMetadata metadata,
                        String order) {
        this.algorithmId = algorithmId;
        this.algorithmName = algorithmName;
        this.initialArray = initialArray != null ? Arrays.copyOf(initialArray, initialArray.length) : new int[0];
        this.sortedArray = sortedArray != null ? Arrays.copyOf(sortedArray, sortedArray.length) : new int[0];
        this.events = events;
        this.statistics = statistics;
        this.metadata = metadata;
        this.truncated = false;
        this.message = "Sorting completed successfully";
        this.order = order != null ? order : "ASCENDING";
    }

    public String getAlgorithmId() {
        return algorithmId;
    }

    public void setAlgorithmId(String algorithmId) {
        this.algorithmId = algorithmId;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public int[] getInitialArray() {
        return initialArray != null ? Arrays.copyOf(initialArray, initialArray.length) : new int[0];
    }

    public void setInitialArray(int[] initialArray) {
        this.initialArray = initialArray != null ? Arrays.copyOf(initialArray, initialArray.length) : new int[0];
    }

    public int[] getSortedArray() {
        return sortedArray != null ? Arrays.copyOf(sortedArray, sortedArray.length) : new int[0];
    }

    public void setSortedArray(int[] sortedArray) {
        this.sortedArray = sortedArray != null ? Arrays.copyOf(sortedArray, sortedArray.length) : new int[0];
    }

    public List<SortingEvent> getEvents() {
        return events;
    }

    public void setEvents(List<SortingEvent> events) {
        this.events = events;
    }

    public SortStatistics getStatistics() {
        return statistics;
    }

    public void setStatistics(SortStatistics statistics) {
        this.statistics = statistics;
    }

    public AlgorithmMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(AlgorithmMetadata metadata) {
        this.metadata = metadata;
    }

    public boolean isTruncated() {
        return truncated;
    }

    public void setTruncated(boolean truncated) {
        this.truncated = truncated;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
