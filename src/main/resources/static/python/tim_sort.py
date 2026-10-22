"""
SortMaster - Tim Sort
Time Complexity: O(n log n) worst/average, O(n) best
Auxiliary Space: O(n)
Stability: Stable
In-Place: No
"""
from typing import List


def min_run_length(n: int) -> int:
    """Returns minimum run length between 32 and 64 for given n."""
    r = 0
    while n >= 32:
        r |= n & 1
        n >>= 1
    return n + r


def insertion_sort_slice(arr: List[int], left: int, right: int, descending: bool = False) -> None:
    for i in range(left + 1, right + 1):
        key = arr[i]
        j = i - 1
        if not descending:
            while j >= left and arr[j] > key:
                arr[j + 1] = arr[j]
                j -= 1
        else:
            while j >= left and arr[j] < key:
                arr[j + 1] = arr[j]
                j -= 1
        arr[j + 1] = key


def merge_slices(arr: List[int], left: int, mid: int, right: int, descending: bool = False) -> None:
    left_part = arr[left:mid + 1]
    right_part = arr[mid + 1:right + 1]
    i = j = 0
    k = left
    while i < len(left_part) and j < len(right_part):
        condition = (left_part[i] >= right_part[j]) if descending else (left_part[i] <= right_part[j])
        if condition:
            arr[k] = left_part[i]
            i += 1
        else:
            arr[k] = right_part[j]
            j += 1
        k += 1
    while i < len(left_part):
        arr[k] = left_part[i]
        i += 1
        k += 1
    while j < len(right_part):
        arr[k] = right_part[j]
        j += 1
        k += 1


def tim_sort(arr: List[int], order: str = "ASCENDING") -> List[int]:
    """
    Genuine Tim Sort combining run insertion sorting and power-of-two run merging.
    Supports 'ASCENDING' and 'DESCENDING' order.
    """
    if order.upper() == "DESCENDING":
        return tim_sort_descending(arr)

    a = arr.copy()
    n = len(a)
    min_run = min_run_length(n)

    for start in range(0, n, min_run):
        end = min(start + min_run - 1, n - 1)
        insertion_sort_slice(a, start, end, descending=False)

    size = min_run
    while size < n:
        for left in range(0, n, 2 * size):
            mid = min(n - 1, left + size - 1)
            right = min(left + 2 * size - 1, n - 1)
            if mid < right:
                merge_slices(a, left, mid, right, descending=False)
        size *= 2

    return a


def tim_sort_descending(arr: List[int]) -> List[int]:
    """
    Sorts a list in descending order (largest to smallest) using Tim Sort.
    """
    a = arr.copy()
    n = len(a)
    min_run = min_run_length(n)

    for start in range(0, n, min_run):
        end = min(start + min_run - 1, n - 1)
        insertion_sort_slice(a, start, end, descending=True)

    size = min_run
    while size < n:
        for left in range(0, n, 2 * size):
            mid = min(n - 1, left + size - 1)
            right = min(left + 2 * size - 1, n - 1)
            if mid < right:
                merge_slices(a, left, mid, right, descending=True)
        size *= 2

    return a


if __name__ == "__main__":
    sample = [5, 21, -3, 7, 23, 19, 10, -3, 100, 2, 45, 12, 8]
    print("Original:   ", sample)
    print("Ascending:  ", tim_sort(sample))
    print("Descending: ", tim_sort_descending(sample))

