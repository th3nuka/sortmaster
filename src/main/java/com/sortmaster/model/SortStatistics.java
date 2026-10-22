package com.sortmaster.model;

public class SortStatistics {
    private long comparisons;
    private long swaps;
    private long writes;
    private long executionTimeNanos;
    private int totalSteps;

    public SortStatistics() {
    }

    public SortStatistics(long comparisons, long swaps, long writes, long executionTimeNanos, int totalSteps) {
        this.comparisons = comparisons;
        this.swaps = swaps;
        this.writes = writes;
        this.executionTimeNanos = executionTimeNanos;
        this.totalSteps = totalSteps;
    }

    public long getComparisons() {
        return comparisons;
    }

    public void setComparisons(long comparisons) {
        this.comparisons = comparisons;
    }

    public long getSwaps() {
        return swaps;
    }

    public void setSwaps(long swaps) {
        this.swaps = swaps;
    }

    public long getWrites() {
        return writes;
    }

    public void setWrites(long writes) {
        this.writes = writes;
    }

    public long getExecutionTimeNanos() {
        return executionTimeNanos;
    }

    public void setExecutionTimeNanos(long executionTimeNanos) {
        this.executionTimeNanos = executionTimeNanos;
    }

    public double getExecutionTimeMillis() {
        return executionTimeNanos / 1_000_000.0;
    }

    public int getTotalSteps() {
        return totalSteps;
    }

    public void setTotalSteps(int totalSteps) {
        this.totalSteps = totalSteps;
    }
}
