"""
SortMaster - Pigeonhole Sort
Time Complexity: O(n + Range)
Auxiliary Space: O(Range)
Stability: Stable
In-Place: No
"""
from typing import List


def pigeonhole_sort(arr: List[int], order: str = "ASCENDING") -> List[int]:
    """
    Sorts a list by placing elements into holes based on offset from minimum value.
    Supports 'ASCENDING' and 'DESCENDING' order.
    """
    if order.upper() == "DESCENDING":
        return pigeonhole_sort_descending(arr)

    if len(arr) <= 1:
        return arr.copy()

    min_val = min(arr)
    max_val = max(arr)
    size = max_val - min_val + 1
    holes: List[List[int]] = [[] for _ in range(size)]

    for x in arr:
        holes[x - min_val].append(x)

    result = []
    for hole in holes:
        result.extend(hole)

    return result


def pigeonhole_sort_descending(arr: List[int]) -> List[int]:
    """
    Sorts a list in descending order (largest to smallest) using Pigeonhole Sort.
    """
    if len(arr) <= 1:
        return arr.copy()

    min_val = min(arr)
    max_val = max(arr)
    size = max_val - min_val + 1
    holes: List[List[int]] = [[] for _ in range(size)]

    for x in arr:
        holes[x - min_val].append(x)

    result = []
    for hole in reversed(holes):
        result.extend(hole)

    return result


if __name__ == "__main__":
    sample = [8, 3, 2, 7, 4, 6, 8, -5, 3]
    print("Original:   ", sample)
    print("Ascending:  ", pigeonhole_sort(sample))
    print("Descending: ", pigeonhole_sort_descending(sample))

