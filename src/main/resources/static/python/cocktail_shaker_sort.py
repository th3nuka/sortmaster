"""
SortMaster - Cocktail Shaker Sort
Time Complexity: O(n^2) average/worst, O(n) best
Auxiliary Space: O(1)
Stability: Stable
In-Place: Yes
"""
from typing import List


def cocktail_shaker_sort(arr: List[int]) -> List[int]:
    """Sorts a list in ascending order using bidirectional Cocktail Shaker Sort."""
    a = arr.copy()
    start = 0
    end = len(a) - 1
    swapped = True

    while swapped:
        swapped = False
        for i in range(start, end):
            if a[i] > a[i + 1]:
                a[i], a[i + 1] = a[i + 1], a[i]
                swapped = True
        if not swapped:
            break
        swapped = False
        end -= 1
        for i in range(end - 1, start - 1, -1):
            if a[i] > a[i + 1]:
                a[i], a[i + 1] = a[i + 1], a[i]
                swapped = True
        start += 1

    return a


def cocktail_shaker_sort_descending(arr: List[int]) -> List[int]:
    """Sorts a list in descending order using bidirectional Cocktail Shaker Sort."""
    a = arr.copy()
    start = 0
    end = len(a) - 1
    swapped = True

    while swapped:
        swapped = False
        for i in range(start, end):
            if a[i] < a[i + 1]:
                a[i], a[i + 1] = a[i + 1], a[i]
                swapped = True
        if not swapped:
            break
        swapped = False
        end -= 1
        for i in range(end - 1, start - 1, -1):
            if a[i] < a[i + 1]:
                a[i], a[i + 1] = a[i + 1], a[i]
                swapped = True
        start += 1

    return a


if __name__ == "__main__":
    sample = [5, 1, 4, 2, 8, 0, 2, -3]
    print("Original:   ", sample)
    print("Ascending:  ", cocktail_shaker_sort(sample))
    print("Descending: ", cocktail_shaker_sort_descending(sample))
