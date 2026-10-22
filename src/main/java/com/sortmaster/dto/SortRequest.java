package com.sortmaster.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.sortmaster.model.SortOrder;
import jakarta.validation.constraints.Size;

public class SortRequest {

    @JsonAlias({"algorithm", "algo"})
    private String algorithmId;

    @Size(max = 200, message = "Array size cannot exceed 200 elements for visualization")
    private int[] array;

    private String detailLevel = "NORMAL"; // MINIMAL, NORMAL, DETAILED

    @JsonAlias({"sortOrder", "sortingOrder", "direction"})
    private String order = "ASCENDING";

    public SortRequest() {
    }

    public SortRequest(String algorithmId, int[] array, String detailLevel) {
        this(algorithmId, array, detailLevel, "ASCENDING");
    }

    public SortRequest(String algorithmId, int[] array, String detailLevel, String order) {
        this.algorithmId = algorithmId;
        this.array = array;
        this.detailLevel = detailLevel != null ? detailLevel : "NORMAL";
        this.order = order != null ? order : "ASCENDING";
    }

    public String getAlgorithmId() {
        return algorithmId;
    }

    public void setAlgorithmId(String algorithmId) {
        this.algorithmId = algorithmId;
    }

    public void setAlgorithm(String algorithm) {
        if (this.algorithmId == null || this.algorithmId.isBlank()) {
            this.algorithmId = algorithm;
        }
    }

    public int[] getArray() {
        return array;
    }

    public void setArray(int[] array) {
        this.array = array;
    }

    public String getDetailLevel() {
        return detailLevel;
    }

    public void setDetailLevel(String detailLevel) {
        this.detailLevel = detailLevel;
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
