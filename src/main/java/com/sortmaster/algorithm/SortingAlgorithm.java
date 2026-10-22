package com.sortmaster.algorithm;

import com.sortmaster.dto.SortResponse;
import com.sortmaster.model.AlgorithmMetadata;
import com.sortmaster.model.SortOrder;

public interface SortingAlgorithm {
    String getId();
    AlgorithmMetadata getMetadata();

    default SortResponse sort(int[] initialArray, String detailLevel) {
        return sort(initialArray, detailLevel, SortOrder.ASCENDING);
    }

    SortResponse sort(int[] initialArray, String detailLevel, SortOrder order);
}
