"""
SortMaster - Quick Sort
Time Complexity: O(n log n) average/best, O(n^2) worst
Auxiliary Space: O(log n) call stack
Stability: Unstable
In-Place: Yes
"""
from typing import List


def quick_sort(arr: List[int]) -> List[int]:
    """Sorts a list in ascending order using in-place Lomuto partition Quick Sort."""
    a = arr.copy()

    def _sort(low: int, high: int):
        if low < high:
            pivot_idx = _partition(low, high)
            _sort(low, pivot_idx - 1)
            _sort(pivot_idx + 1, high)

    def _partition(low: int, high: int) -> int:
        pivot = a[high]
        i = low - 1
        for j in range(low, high):
            if a[j] <= pivot:
                i += 1
                a[i], a[j] = a[j], a[i]
        a[i + 1], a[high] = a[high], a[i + 1]
        return i + 1

    _sort(0, len(a) - 1)
    return a


def quick_sort_descending(arr: List[int]) -> List[int]:
    """Sorts a list in descending order using in-place Lomuto partition Quick Sort."""
    a = arr.copy()

    def _sort(low: int, high: int):
        if low < high:
            pivot_idx = _partition(low, high)
            _sort(low, pivot_idx - 1)
            _sort(pivot_idx + 1, high)

    def _partition(low: int, high: int) -> int:
        pivot = a[high]
        i = low - 1
        for j in range(low, high):
            if a[j] >= pivot:
                i += 1
                a[i], a[j] = a[j], a[i]
        a[i + 1], a[high] = a[high], a[i + 1]
        return i + 1

    _sort(0, len(a) - 1)
    return a


if __name__ == "__main__":
    sample = [10, 80, 30, 90, 40, 50, 70, -10, 30]
    print("Original:   ", sample)
    print("Ascending:  ", quick_sort(sample))
    print("Descending: ", quick_sort_descending(sample))
