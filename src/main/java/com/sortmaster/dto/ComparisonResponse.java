package com.sortmaster.dto;

import com.sortmaster.model.SortStatistics;

import java.util.Arrays;
import java.util.List;

public class ComparisonResponse {
    private int[] initialArray;
    private List<AlgorithmComparisonItem> results;
    private String order = "ASCENDING";

    public ComparisonResponse() {
    }

    public ComparisonResponse(int[] initialArray, List<AlgorithmComparisonItem> results) {
        this(initialArray, results, "ASCENDING");
    }

    public ComparisonResponse(int[] initialArray, List<AlgorithmComparisonItem> results, String order) {
        this.initialArray = initialArray != null ? Arrays.copyOf(initialArray, initialArray.length) : new int[0];
        this.results = results;
        this.order = order != null ? order : "ASCENDING";
    }

    public int[] getInitialArray() {
        return initialArray != null ? Arrays.copyOf(initialArray, initialArray.length) : new int[0];
    }

    public void setInitialArray(int[] initialArray) {
        this.initialArray = initialArray != null ? Arrays.copyOf(initialArray, initialArray.length) : new int[0];
    }

    public List<AlgorithmComparisonItem> getResults() {
        return results;
    }

    public void setResults(List<AlgorithmComparisonItem> results) {
        this.results = results;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public static class AlgorithmComparisonItem {
        private String algorithmId;
        private String algorithmName;
        private String category;
        private String bestTimeComplexity;
        private String averageTimeComplexity;
        private String worstTimeComplexity;
        private String spaceComplexity;
        private boolean stable;
        private boolean inPlace;
        private SortStatistics statistics;
        private int[] sortedArray;

        public AlgorithmComparisonItem() {
        }

        public AlgorithmComparisonItem(String algorithmId,
                                       String algorithmName,
                                       String category,
                                       String bestTimeComplexity,
                                       String averageTimeComplexity,
                                       String worstTimeComplexity,
                                       String spaceComplexity,
                                       boolean stable,
                                       boolean inPlace,
                                       SortStatistics statistics) {
            this(algorithmId, algorithmName, category, bestTimeComplexity, averageTimeComplexity,
                 worstTimeComplexity, spaceComplexity, stable, inPlace, statistics, new int[0]);
        }

        public AlgorithmComparisonItem(String algorithmId,
                                       String algorithmName,
                                       String category,
                                       String bestTimeComplexity,
                                       String averageTimeComplexity,
                                       String worstTimeComplexity,
                                       String spaceComplexity,
                                       boolean stable,
                                       boolean inPlace,
                                       SortStatistics statistics,
                                       int[] sortedArray) {
            this.algorithmId = algorithmId;
            this.algorithmName = algorithmName;
            this.category = category;
            this.bestTimeComplexity = bestTimeComplexity;
            this.averageTimeComplexity = averageTimeComplexity;
            this.worstTimeComplexity = worstTimeComplexity;
            this.spaceComplexity = spaceComplexity;
            this.stable = stable;
            this.inPlace = inPlace;
            this.statistics = statistics;
            this.sortedArray = sortedArray != null ? Arrays.copyOf(sortedArray, sortedArray.length) : new int[0];
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

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getBestTimeComplexity() {
            return bestTimeComplexity;
        }

        public void setBestTimeComplexity(String bestTimeComplexity) {
            this.bestTimeComplexity = bestTimeComplexity;
        }

        public String getAverageTimeComplexity() {
            return averageTimeComplexity;
        }

        public void setAverageTimeComplexity(String averageTimeComplexity) {
            this.averageTimeComplexity = averageTimeComplexity;
        }

        public String getWorstTimeComplexity() {
            return worstTimeComplexity;
        }

        public void setWorstTimeComplexity(String worstTimeComplexity) {
            this.worstTimeComplexity = worstTimeComplexity;
        }

        public String getSpaceComplexity() {
            return spaceComplexity;
        }

        public void setSpaceComplexity(String spaceComplexity) {
            this.spaceComplexity = spaceComplexity;
        }

        public boolean isStable() {
            return stable;
        }

        public void setStable(boolean stable) {
            this.stable = stable;
        }

        public boolean isInPlace() {
            return inPlace;
        }

        public void setInPlace(boolean inPlace) {
            this.inPlace = inPlace;
        }

        public SortStatistics getStatistics() {
            return statistics;
        }

        public void setStatistics(SortStatistics statistics) {
            this.statistics = statistics;
        }

        public int[] getSortedArray() {
            return sortedArray != null ? Arrays.copyOf(sortedArray, sortedArray.length) : new int[0];
        }

        public void setSortedArray(int[] sortedArray) {
            this.sortedArray = sortedArray != null ? Arrays.copyOf(sortedArray, sortedArray.length) : new int[0];
        }
    }
}
