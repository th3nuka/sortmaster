"""
SortMaster - Gnome Sort (Stupid Sort)
Time Complexity: O(n^2) worst/average, O(n) best
Auxiliary Space: O(1)
Stability: Stable
In-Place: Yes
"""
from typing import List


def gnome_sort(arr: List[int], order: str = "ASCENDING") -> List[int]:
    """
    Sorts a list by stepping forward when ordered and stepping backward when swapping.
    Supports 'ASCENDING' and 'DESCENDING' order.
    """
    if order.upper() == "DESCENDING":
        return gnome_sort_descending(arr)

    a = arr.copy()
    n = len(a)
    index = 0
    while index < n:
        if index == 0 or a[index] >= a[index - 1]:
            index += 1
        else:
            a[index], a[index - 1] = a[index - 1], a[index]
            index -= 1
    return a


def gnome_sort_descending(arr: List[int]) -> List[int]:
    """
    Sorts a list in descending order (largest to smallest) using Gnome Sort.
    """
    a = arr.copy()
    n = len(a)
    index = 0
    while index < n:
        if index == 0 or a[index] <= a[index - 1]:
            index += 1
        else:
            a[index], a[index - 1] = a[index - 1], a[index]
            index -= 1
    return a


if __name__ == "__main__":
    sample = [34, 2, 10, -9, 0, 2, 15]
    print("Original:   ", sample)
    print("Ascending:  ", gnome_sort(sample))
    print("Descending: ", gnome_sort_descending(sample))

