package com.sortmaster.service;

import com.sortmaster.algorithm.SortingAlgorithm;
import com.sortmaster.exception.AlgorithmNotFoundException;
import com.sortmaster.model.AlgorithmMetadata;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AlgorithmRegistry {

    private final Map<String, SortingAlgorithm> algorithmsMap = new LinkedHashMap<>();

    public AlgorithmRegistry(List<SortingAlgorithm> algorithmList) {
        if (algorithmList != null) {
            for (SortingAlgorithm algo : algorithmList) {
                algorithmsMap.put(algo.getId().toLowerCase(), algo);
            }
        }
    }

    public SortingAlgorithm getAlgorithm(String id) {
        if (id == null) {
            throw new AlgorithmNotFoundException("null");
        }
        SortingAlgorithm algo = algorithmsMap.get(id.toLowerCase().trim());
        if (algo == null) {
            throw new AlgorithmNotFoundException(id);
        }
        return algo;
    }

    public List<AlgorithmMetadata> getAllMetadata() {
        return algorithmsMap.values().stream()
                .map(SortingAlgorithm::getMetadata)
                .collect(Collectors.toList());
    }

    public List<String> getAllIds() {
        return new ArrayList<>(algorithmsMap.keySet());
    }

    public boolean hasAlgorithm(String id) {
        return id != null && algorithmsMap.containsKey(id.toLowerCase().trim());
    }

    public int size() {
        return algorithmsMap.size();
    }
}
