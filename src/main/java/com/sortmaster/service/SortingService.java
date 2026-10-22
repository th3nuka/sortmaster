package com.sortmaster.service;

import com.sortmaster.algorithm.SortingAlgorithm;
import com.sortmaster.dto.*;
import com.sortmaster.exception.InvalidSortRequestException;
import com.sortmaster.model.AlgorithmMetadata;
import com.sortmaster.model.SortOrder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SortingService {

    private static final int MAX_ARRAY_SIZE = 200;
    private final AlgorithmRegistry registry;

    public SortingService(AlgorithmRegistry registry) {
        this.registry = registry;
    }

    public SortResponse sort(SortRequest request) {
        if (request == null) {
            throw new InvalidSortRequestException("Sort request body cannot be null.");
        }
        if (request.getAlgorithmId() == null || request.getAlgorithmId().isBlank()) {
            throw new InvalidSortRequestException("Algorithm ID must be specified.");
        }

        int[] array = request.getArray();
        if (array == null) {
            array = new int[0];
        }

        if (array.length > MAX_ARRAY_SIZE) {
            throw new InvalidSortRequestException("Array size (" + array.length + ") exceeds maximum allowed limit of " + MAX_ARRAY_SIZE + " for animation stability.");
        }

        SortOrder order = request.getSortOrder();
        SortingAlgorithm algorithm = registry.getAlgorithm(request.getAlgorithmId());
        return algorithm.sort(array, request.getDetailLevel(), order);
    }

    public ComparisonResponse compare(ComparisonRequest request) {
        if (request == null) {
            throw new InvalidSortRequestException("Comparison request body cannot be null.");
        }
        if (request.getAlgorithmIds() == null || request.getAlgorithmIds().isEmpty()) {
            throw new InvalidSortRequestException("At least one algorithm must be selected for comparison.");
        }

        int[] array = request.getArray();
        if (array == null) {
            array = new int[0];
        }

        if (array.length > MAX_ARRAY_SIZE) {
            throw new InvalidSortRequestException("Array size (" + array.length + ") exceeds maximum allowed limit of " + MAX_ARRAY_SIZE + ".");
        }

        SortOrder order = request.getSortOrder();
        List<ComparisonResponse.AlgorithmComparisonItem> items = new ArrayList<>();

        for (String id : request.getAlgorithmIds()) {
            SortingAlgorithm algorithm = registry.getAlgorithm(id);
            // Run on a clean copy of the initial array
            int[] arrayCopy = Arrays.copyOf(array, array.length);
            SortResponse result = algorithm.sort(arrayCopy, "MINIMAL", order);
            AlgorithmMetadata meta = algorithm.getMetadata();

            items.add(new ComparisonResponse.AlgorithmComparisonItem(
                    meta.getId(),
                    meta.getName(),
                    meta.getCategory(),
                    meta.getBestTimeComplexity(),
                    meta.getAverageTimeComplexity(),
                    meta.getWorstTimeComplexity(),
                    meta.getSpaceComplexity(),
                    meta.isStable(),
                    meta.isInPlace(),
                    result.getStatistics(),
                    result.getSortedArray()
            ));
        }

        return new ComparisonResponse(array, items, order.name());
    }

    public List<AlgorithmMetadata> getAllAlgorithms() {
        return registry.getAllMetadata();
    }

    public AlgorithmMetadata getAlgorithmMetadata(String id) {
        return registry.getAlgorithm(id).getMetadata();
    }
}
