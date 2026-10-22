"""
SortMaster - Merge Sort
Time Complexity: O(n log n) best/worst/average
Auxiliary Space: O(n)
Stability: Stable
In-Place: No
"""
from typing import List


def merge_sort(arr: List[int]) -> List[int]:
    """Sorts a list in ascending order using Merge Sort."""
    if len(arr) <= 1:
        return arr.copy()

    mid = len(arr) // 2
    left = merge_sort(arr[:mid])
    right = merge_sort(arr[mid:])

    result = []
    i = j = 0
    while i < len(left) and j < len(right):
        if left[i] <= right[j]:
            result.append(left[i])
            i += 1
        else:
            result.append(right[j])
            j += 1
    result.extend(left[i:])
    result.extend(right[j:])
    return result


def merge_sort_descending(arr: List[int]) -> List[int]:
    """Sorts a list in descending order using Merge Sort."""
    if len(arr) <= 1:
        return arr.copy()

    mid = len(arr) // 2
    left = merge_sort_descending(arr[:mid])
    right = merge_sort_descending(arr[mid:])

    result = []
    i = j = 0
    while i < len(left) and j < len(right):
        if left[i] >= right[j]:
            result.append(left[i])
            i += 1
        else:
            result.append(right[j])
            j += 1
    result.extend(left[i:])
    result.extend(right[j:])
    return result


if __name__ == "__main__":
    sample = [38, 27, 43, 3, 9, 82, 10, -8, 27]
    print("Original:   ", sample)
    print("Ascending:  ", merge_sort(sample))
    print("Descending: ", merge_sort_descending(sample))
