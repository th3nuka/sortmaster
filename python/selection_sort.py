"""
SortMaster - Selection Sort
Time Complexity: O(n^2) best/worst/average
Auxiliary Space: O(1)
Stability: Unstable (by default)
In-Place: Yes
"""
from typing import List


def selection_sort(arr: List[int]) -> List[int]:
    """Sorts a list in ascending order using Selection Sort."""
    a = arr.copy()
    n = len(a)
    for i in range(n - 1):
        min_idx = i
        for j in range(i + 1, n):
            if a[j] < a[min_idx]:
                min_idx = j
        if min_idx != i:
            a[i], a[min_idx] = a[min_idx], a[i]
    return a


def selection_sort_descending(arr: List[int]) -> List[int]:
    """Sorts a list in descending order using Selection Sort."""
    a = arr.copy()
    n = len(a)
    for i in range(n - 1):
        max_idx = i
        for j in range(i + 1, n):
            if a[j] > a[max_idx]:
                max_idx = j
        if max_idx != i:
            a[i], a[max_idx] = a[max_idx], a[i]
    return a


if __name__ == "__main__":
    sample = [29, 10, 14, 37, 14, -3, 0]
    print("Original:   ", sample)
    print("Ascending:  ", selection_sort(sample))
    print("Descending: ", selection_sort_descending(sample))
