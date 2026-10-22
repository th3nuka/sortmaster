"""
SortMaster - Bubble Sort
Time Complexity: O(n^2) worst/average, O(n) best (with early termination)
Auxiliary Space: O(1)
Stability: Stable
In-Place: Yes
"""
from typing import List


def bubble_sort(arr: List[int]) -> List[int]:
    """Sorts a list of integers in ascending order using Bubble Sort."""
    a = arr.copy()
    n = len(a)
    for i in range(n - 1):
        swapped = False
        for j in range(n - i - 1):
            if a[j] > a[j + 1]:
                a[j], a[j + 1] = a[j + 1], a[j]
                swapped = True
        if not swapped:
            break
    return a


def bubble_sort_descending(arr: List[int]) -> List[int]:
    """Sorts a list of integers in descending order using Bubble Sort."""
    a = arr.copy()
    n = len(a)
    for i in range(n - 1):
        swapped = False
        for j in range(n - i - 1):
            if a[j] < a[j + 1]:
                a[j], a[j + 1] = a[j + 1], a[j]
                swapped = True
        if not swapped:
            break
    return a


if __name__ == "__main__":
    sample = [64, 34, 25, 12, 22, 11, 90, -5, 25]
    print("Original:   ", sample)
    print("Ascending:  ", bubble_sort(sample))
    print("Descending: ", bubble_sort_descending(sample))
