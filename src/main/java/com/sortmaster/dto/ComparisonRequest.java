package com.sortmaster.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.sortmaster.model.SortOrder;

import java.util.List;

public class ComparisonRequest {
    private List<String> algorithmIds;
    private int[] array;

    @JsonAlias({"sortOrder", "sortingOrder", "direction"})
    private String order = "ASCENDING";

    public ComparisonRequest() {
    }

    public ComparisonRequest(List<String> algorithmIds, int[] array) {
        this(algorithmIds, array, "ASCENDING");
    }

    public ComparisonRequest(List<String> algorithmIds, int[] array, String order) {
        this.algorithmIds = algorithmIds;
        this.array = array;
        this.order = order != null ? order : "ASCENDING";
    }

    public List<String> getAlgorithmIds() {
        return algorithmIds;
    }

    public void setAlgorithmIds(List<String> algorithmIds) {
        this.algorithmIds = algorithmIds;
    }

    public int[] getArray() {
        return array;
    }

    public void setArray(int[] array) {
        this.array = array;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public SortOrder getSortOrder() {
        return SortOrder.fromString(order);
    }
}
