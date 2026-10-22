"""
SortMaster - Bucket Sort
Time Complexity: O(n + k) average, O(n^2) worst
Auxiliary Space: O(n + k)
Stability: Stable
In-Place: No
"""
from typing import List


def bucket_sort(arr: List[int]) -> List[int]:
    """Sorts a list in ascending order using Bucket Sort."""
    if len(arr) <= 1:
        return arr.copy()

    min_val, max_val = min(arr), max(arr)
    if min_val == max_val:
        return arr.copy()

    bucket_count = max(1, len(arr) // 2)
    buckets: List[List[int]] = [[] for _ in range(bucket_count)]
    val_range = max_val - min_val + 1

    for x in arr:
        b_idx = int((x - min_val) / val_range * bucket_count)
        if b_idx >= bucket_count:
            b_idx = bucket_count - 1
        buckets[b_idx].append(x)

    result = []
    for bucket in buckets:
        for i in range(1, len(bucket)):
            key = bucket[i]
            j = i - 1
            while j >= 0 and bucket[j] > key:
                bucket[j + 1] = bucket[j]
                j -= 1
            bucket[j + 1] = key
        result.extend(bucket)

    return result


def bucket_sort_descending(arr: List[int]) -> List[int]:
    """Sorts a list in descending order using Bucket Sort."""
    if len(arr) <= 1:
        return arr.copy()

    min_val, max_val = min(arr), max(arr)
    if min_val == max_val:
        return arr.copy()

    bucket_count = max(1, len(arr) // 2)
    buckets: List[List[int]] = [[] for _ in range(bucket_count)]
    val_range = max_val - min_val + 1

    for x in arr:
        b_idx = int((x - min_val) / val_range * bucket_count)
        if b_idx >= bucket_count:
            b_idx = bucket_count - 1
        buckets[b_idx].append(x)

    result = []
    for bucket in reversed(buckets):
        for i in range(1, len(bucket)):
            key = bucket[i]
            j = i - 1
            while j >= 0 and bucket[j] < key:
                bucket[j + 1] = bucket[j]
                j -= 1
            bucket[j + 1] = key
        result.extend(bucket)

    return result


if __name__ == "__main__":
    sample = [42, 32, 33, 52, 37, 47, 51, -10, 32]
    print("Original:   ", sample)
    print("Ascending:  ", bucket_sort(sample))
    print("Descending: ", bucket_sort_descending(sample))
