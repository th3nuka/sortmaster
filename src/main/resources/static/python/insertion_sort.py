"""
SortMaster - Insertion Sort
Time Complexity: O(n^2) worst/average, O(n) best
Auxiliary Space: O(1)
Stability: Stable
In-Place: Yes
"""
from typing import List


def insertion_sort(arr: List[int]) -> List[int]:
    """Sorts a list in ascending order using Insertion Sort."""
    a = arr.copy()
    n = len(a)
    for i in range(1, n):
        key = a[i]
        j = i - 1
        while j >= 0 and a[j] > key:
            a[j + 1] = a[j]
            j -= 1
        a[j + 1] = key
    return a


def insertion_sort_descending(arr: List[int]) -> List[int]:
    """Sorts a list in descending order using Insertion Sort."""
    a = arr.copy()
    n = len(a)
    for i in range(1, n):
        key = a[i]
        j = i - 1
        while j >= 0 and a[j] < key:
            a[j + 1] = a[j]
            j -= 1
        a[j + 1] = key
    return a


if __name__ == "__main__":
    sample = [12, 11, 13, 5, 6, -2, 11]
    print("Original:   ", sample)
    print("Ascending:  ", insertion_sort(sample))
    print("Descending: ", insertion_sort_descending(sample))
