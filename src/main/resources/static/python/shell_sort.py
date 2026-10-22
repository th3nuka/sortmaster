"""
SortMaster - Shell Sort
Time Complexity: O(n log^2 n) depending on gap sequence, O(n) best
Auxiliary Space: O(1)
Stability: Unstable
In-Place: Yes
"""
from typing import List


def shell_sort(arr: List[int]) -> List[int]:
    """Sorts a list in ascending order using Shell Sort."""
    a = arr.copy()
    n = len(a)
    gap = n // 2
    while gap > 0:
        for i in range(gap, n):
            temp = a[i]
            j = i
            while j >= gap and a[j - gap] > temp:
                a[j] = a[j - gap]
                j -= gap
            a[j] = temp
        gap //= 2
    return a


def shell_sort_descending(arr: List[int]) -> List[int]:
    """Sorts a list in descending order using Shell Sort."""
    a = arr.copy()
    n = len(a)
    gap = n // 2
    while gap > 0:
        for i in range(gap, n):
            temp = a[i]
            j = i
            while j >= gap and a[j - gap] < temp:
                a[j] = a[j - gap]
                j -= gap
            a[j] = temp
        gap //= 2
    return a


if __name__ == "__main__":
    sample = [12, 34, 54, 2, 3, -1, 34]
    print("Original:   ", sample)
    print("Ascending:  ", shell_sort(sample))
    print("Descending: ", shell_sort_descending(sample))
