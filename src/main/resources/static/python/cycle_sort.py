"""
SortMaster - Cycle Sort
Time Complexity: O(n^2) worst/average/best
Auxiliary Space: O(1)
Stability: Unstable
In-Place: Yes (optimal number of memory writes: O(n))
"""
from typing import List


def cycle_sort(arr: List[int], order: str = "ASCENDING") -> List[int]:
    """
    Sorts by decomposing the permutation into cycles and placing items directly at target index.
    Supports 'ASCENDING' and 'DESCENDING' order.
    """
    if order.upper() == "DESCENDING":
        return cycle_sort_descending(arr)

    a = arr.copy()
    n = len(a)

    for cycle_start in range(0, n - 1):
        item = a[cycle_start]
        pos = cycle_start
        for i in range(cycle_start + 1, n):
            if a[i] < item:
                pos += 1

        if pos == cycle_start:
            continue

        while item == a[pos]:
            pos += 1
        a[pos], item = item, a[pos]

        while pos != cycle_start:
            pos = cycle_start
            for i in range(cycle_start + 1, n):
                if a[i] < item:
                    pos += 1
            while item == a[pos]:
                pos += 1
            a[pos], item = item, a[pos]

    return a


def cycle_sort_descending(arr: List[int]) -> List[int]:
    """
    Sorts by cycle decomposition in descending order (largest to smallest).
    """
    a = arr.copy()
    n = len(a)

    for cycle_start in range(0, n - 1):
        item = a[cycle_start]
        pos = cycle_start
        for i in range(cycle_start + 1, n):
            if a[i] > item:
                pos += 1

        if pos == cycle_start:
            continue

        while item == a[pos]:
            pos += 1
        a[pos], item = item, a[pos]

        while pos != cycle_start:
            pos = cycle_start
            for i in range(cycle_start + 1, n):
                if a[i] > item:
                    pos += 1
            while item == a[pos]:
                pos += 1
            a[pos], item = item, a[pos]

    return a


if __name__ == "__main__":
    sample = [10, 5, 2, 3, 7, 5, -1, 10]
    print("Original:   ", sample)
    print("Ascending:  ", cycle_sort(sample))
    print("Descending: ", cycle_sort_descending(sample))

