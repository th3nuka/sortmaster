"""
SortMaster - Counting Sort
Time Complexity: O(n + k) where k is the range of values
Auxiliary Space: O(n + k)
Stability: Stable
In-Place: No
"""
from typing import List


def counting_sort(arr: List[int]) -> List[int]:
    """Sorts a list of integers (including negatives via offset) in ascending order."""
    if not arr:
        return []
    min_val = min(arr)
    max_val = max(arr)
    k = max_val - min_val + 1
    count = [0] * k

    for x in arr:
        count[x - min_val] += 1

    for i in range(1, k):
        count[i] += count[i - 1]

    output = [0] * len(arr)
    for x in reversed(arr):
        idx = count[x - min_val] - 1
        output[idx] = x
        count[x - min_val] -= 1

    return output


def counting_sort_descending(arr: List[int]) -> List[int]:
    """Sorts a list of integers (including negatives via offset) in descending order."""
    if not arr:
        return []
    min_val = min(arr)
    max_val = max(arr)
    k = max_val - min_val + 1
    count = [0] * k

    for x in arr:
        count[x - min_val] += 1

    for i in range(k - 2, -1, -1):
        count[i] += count[i + 1]

    output = [0] * len(arr)
    for x in reversed(arr):
        idx = count[x - min_val] - 1
        output[idx] = x
        count[x - min_val] -= 1

    return output


if __name__ == "__main__":
    sample = [4, 2, 2, 8, 3, 3, 1, -2, 5, -2]
    print("Original:   ", sample)
    print("Ascending:  ", counting_sort(sample))
    print("Descending: ", counting_sort_descending(sample))
