"""
SortMaster - Comb Sort
Time Complexity: O(n^2) worst, O(n log n) average
Auxiliary Space: O(1)
Stability: Unstable
In-Place: Yes
"""
from typing import List


def comb_sort(arr: List[int], order: str = "ASCENDING") -> List[int]:
    """
    Sorts using Comb Sort with shrink factor 1.3 to eliminate turtles quickly.
    Supports 'ASCENDING' and 'DESCENDING' order.
    """
    if order.upper() == "DESCENDING":
        return comb_sort_descending(arr)

    a = arr.copy()
    n = len(a)
    gap = n
    shrink = 1.3
    sorted_flag = False

    while not sorted_flag:
        gap = int(gap / shrink)
        if gap <= 1:
            gap = 1
            sorted_flag = True
        for i in range(n - gap):
            if a[i] > a[i + gap]:
                a[i], a[i + gap] = a[i + gap], a[i]
                sorted_flag = False

    return a


def comb_sort_descending(arr: List[int]) -> List[int]:
    """
    Sorts using Comb Sort in descending order (largest to smallest).
    """
    a = arr.copy()
    n = len(a)
    gap = n
    shrink = 1.3
    sorted_flag = False

    while not sorted_flag:
        gap = int(gap / shrink)
        if gap <= 1:
            gap = 1
            sorted_flag = True
        for i in range(n - gap):
            if a[i] < a[i + gap]:
                a[i], a[i + gap] = a[i + gap], a[i]
                sorted_flag = False

    return a


if __name__ == "__main__":
    sample = [8, 4, 1, 56, 3, -44, 23, 0, 8]
    print("Original:   ", sample)
    print("Ascending:  ", comb_sort(sample))
    print("Descending: ", comb_sort_descending(sample))

