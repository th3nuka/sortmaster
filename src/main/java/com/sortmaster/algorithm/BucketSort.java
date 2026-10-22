package com.sortmaster.algorithm;

import com.sortmaster.model.AlgorithmMetadata;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class BucketSort extends AbstractSortingAlgorithm {

    public static final String ID = "bucket-sort";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    protected void executeSort(int[] array, String detailLevel) {
        int n = array.length;
        if (n <= 1) return;

        int min = array[0];
        int max = array[0];
        for (int val : array) {
            if (val < min) min = val;
            if (val > max) max = val;
        }

        if (min == max) {
            emitStart(array, "All elements are identical (" + min + "). Array is already sorted.");
            return;
        }

        int bucketCount = Math.max(2, (int) Math.sqrt(n));
        List<List<Integer>> buckets = new ArrayList<>(bucketCount);
        for (int i = 0; i < bucketCount; i++) {
            buckets.add(new ArrayList<>());
        }

        emitStart(array, "Created " + bucketCount + " buckets spanning range [" + min + ".." + max + "]. Distributing elements in " + order.getDisplayName().toLowerCase() + " order.");

        // 1. Distribute elements into buckets
        double range = (double) (max - min);
        for (int i = 0; i < n; i++) {
            int val = array[i];
            int bucketIdx = (int) (((long) (val - min) * (bucketCount - 1)) / range);
            buckets.get(bucketIdx).add(val);

            emitCompare(array, i, i,
                    "Assigned element " + val + " at index " + i + " into bucket #" + bucketIdx + ".",
                    "distribute");
        }

        // 2. Sort individual buckets using insertion sort and gather
        emitStart(array, "Sorting elements within each bucket and merging back into main array.");

        int currentIdx = 0;
        int startBucket = isAscending() ? 0 : (bucketCount - 1);
        int endBucket = isAscending() ? bucketCount : -1;
        int step = isAscending() ? 1 : -1;

        for (int b = startBucket; b != endBucket; b += step) {
            List<Integer> bucket = buckets.get(b);
            // Insertion sort within bucket for stability
            for (int i = 1; i < bucket.size(); i++) {
                int key = bucket.get(i);
                int j = i - 1;
                while (j >= 0 && (isAscending() ? (bucket.get(j) > key) : (bucket.get(j) < key))) {
                    bucket.set(j + 1, bucket.get(j));
                    comparisons++;
                    writes++;
                    j--;
                }
                bucket.set(j + 1, key);
                writes++;
            }

            for (int val : bucket) {
                array[currentIdx] = val;
                emitWrite(array, currentIdx, val,
                        "Flushing sorted element " + val + " from bucket #" + b + " into index " + currentIdx + ".",
                        "flush_bucket");
                emitMarkSorted(array, List.of(currentIdx),
                        "Position " + currentIdx + " placed from bucket #" + b + ".",
                        "mark_sorted");
                currentIdx++;
            }
        }
    }

    @Override
    public AlgorithmMetadata getMetadata() {
        return new AlgorithmMetadata(
                ID,
                "Bucket Sort",
                "Distribution Sort",
                "Distributes elements into a number of buckets, then sorts each bucket individually.",
                "Bucket Sort is a scatter-gather sorting algorithm. It distributes the elements of an array into several dynamically sized buckets based on their values. Each bucket is subsequently sorted individually—often using Insertion Sort or recursion—and finally concatenated into the output array. When input data is uniformly distributed across a range, Bucket Sort executes in linear O(n) average time.",
                "O(n + k)",
                "O(n + k)",
                "O(n²)",
                "O(n + k)",
                true,
                false,
                "Medium",
                List.of(
                        "Uniformly distributed floating-point or integer datasets over a known range",
                        "External sorting where buckets correspond to independent disks or nodes",
                        "Preprocessing step for parallel computing architectures"
                ),
                List.of(
                        "Achieves average linear O(n) time when elements are evenly distributed",
                        "Stable when underlying bucket sort is stable (e.g. insertion sort)",
                        "Easily distributed across multiple processor threads"
                ),
                List.of(
                        "Degrades to O(n²) if all elements cluster into a single bucket (skewed distribution)",
                        "Requires extra auxiliary space for bucket data structures",
                        "Inefficient when data distribution is unknown or severely clumped"
                ),
                List.of(
                        "Assuming uniform distribution on highly clustered or skewed inputs",
                        "Bucket index calculation resulting in out-of-bounds error for maximum element",
                        "Overlooking negative integers by assuming values lie in [0, 1)"
                ),
                List.of(
                        "Under what input conditions does Bucket Sort achieve O(n) average time complexity?",
                        "What happens to the performance of Bucket Sort if all elements map to the exact same bucket?",
                        "Compare Bucket Sort with Counting Sort in terms of memory requirements for large ranges."
                ),
                """
                procedure bucketSort(A, k)
                    create k empty buckets
                    minVal = min(A), maxVal = max(A)
                    for x in A do
                        insert x into bucket[floor((x - minVal) / range * (k - 1))]
                    end for
                    for each bucket do
                        insertionSort(bucket)
                    end for
                    concatenate all buckets into A
                end procedure
                """,
                """
                public void bucketSort(int[] arr) {
                    int n = arr.length;
                    if (n <= 1) return;
                    int min = arr[0], max = arr[0];
                    for (int x : arr) {
                        if (x < min) min = x;
                        if (x > max) max = x;
                    }
                    int bucketCount = Math.max(2, (int) Math.sqrt(n));
                    List<List<Integer>> buckets = new ArrayList<>(bucketCount);
                    for (int i = 0; i < bucketCount; i++) buckets.add(new ArrayList<>());
                    for (int x : arr) {
                        int idx = (int) (((long) (x - min) * (bucketCount - 1)) / (max - min));
                        buckets.get(idx).add(x);
                    }
                    int k = 0;
                    for (List<Integer> b : buckets) {
                        Collections.sort(b);
                        for (int x : b) arr[k++] = x;
                    }
                }
                """,
                """
                def bucket_sort(arr: list[int]) -> list[int]:
                    if len(arr) <= 1:
                        return arr
                    min_val, max_val = min(arr), max(arr)
                    if min_val == max_val:
                        return arr
                    bucket_count = max(2, int(len(arr) ** 0.5))
                    buckets = [[] for _ in range(bucket_count)]
                    val_range = float(max_val - min_val)

                    for x in arr:
                        idx = int(((x - min_val) * (bucket_count - 1)) / val_range)
                        buckets[idx].append(x)

                    arr.clear()
                    for b in buckets:
                        for i in range(1, len(b)):
                            key = b[i]
                            j = i - 1
                            while j >= 0 and b[j] > key:
                                b[j + 1] = b[j]
                                j -= 1
                            b[j + 1] = key
                        arr.extend(b)
                    return arr
                """,
                """
                procedure bucketSortDescending(A, k)
                    create k empty buckets
                    minVal = min(A), maxVal = max(A)
                    for x in A do
                        insert x into bucket[floor((x - minVal) / range * (k - 1))]
                    end for
                    for each bucket do
                        insertionSortDescending(bucket)
                    end for
                    concatenate buckets in reverse order into A
                end procedure
                """,
                """
                public void bucketSortDescending(int[] arr) {
                    int n = arr.length;
                    if (n <= 1) return;
                    int min = arr[0], max = arr[0];
                    for (int x : arr) {
                        if (x < min) min = x;
                        if (x > max) max = x;
                    }
                    int bucketCount = Math.max(2, (int) Math.sqrt(n));
                    List<List<Integer>> buckets = new ArrayList<>(bucketCount);
                    for (int i = 0; i < bucketCount; i++) buckets.add(new ArrayList<>());
                    for (int x : arr) {
                        int idx = (int) (((long) (x - min) * (bucketCount - 1)) / (max - min));
                        buckets.get(idx).add(x);
                    }
                    int k = 0;
                    for (int b = bucketCount - 1; b >= 0; b--) {
                        List<Integer> bucket = buckets.get(b);
                        bucket.sort(Collections.reverseOrder());
                        for (int x : bucket) arr[k++] = x;
                    }
                }
                """,
                """
                def bucket_sort_descending(arr: list[int]) -> list[int]:
                    if len(arr) <= 1:
                        return arr
                    min_val, max_val = min(arr), max(arr)
                    if min_val == max_val:
                        return arr
                    bucket_count = max(2, int(len(arr) ** 0.5))
                    buckets = [[] for _ in range(bucket_count)]
                    val_range = float(max_val - min_val)

                    for x in arr:
                        idx = int(((x - min_val) * (bucket_count - 1)) / val_range)
                        buckets[idx].append(x)

                    arr.clear()
                    for b in reversed(buckets):
                        for i in range(1, len(b)):
                            key = b[i]
                            j = i - 1
                            while j >= 0 and b[j] < key:
                                b[j + 1] = b[j]
                                j -= 1
                            b[j + 1] = key
                        arr.extend(b)
                    return arr
                """,
                Map.of(
                        "distribute", "Scatter current element into its mathematically determined bucket interval.",
                        "flush_bucket", "Gather elements from ordered buckets back into contiguous array positions."
                )
        );
    }
}
