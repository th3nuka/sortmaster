"""
SortMaster - Heap Sort
Time Complexity: O(n log n) best/worst/average
Auxiliary Space: O(1)
Stability: Unstable
In-Place: Yes
"""
from typing import List


def heap_sort(arr: List[int]) -> List[int]:
    """Sorts a list in ascending order by building a Max-Heap and repeatedly extracting the maximum."""
    a = arr.copy()
    n = len(a)

    def _heapify(heap_size: int, i: int):
        largest = i
        left = 2 * i + 1
        right = 2 * i + 2
        if left < heap_size and a[left] > a[largest]:
            largest = left
        if right < heap_size and a[right] > a[largest]:
            largest = right
        if largest != i:
            a[i], a[largest] = a[largest], a[i]
            _heapify(heap_size, largest)

    for i in range(n // 2 - 1, -1, -1):
        _heapify(n, i)

    for i in range(n - 1, 0, -1):
        a[0], a[i] = a[i], a[0]
        _heapify(i, 0)

    return a


def heap_sort_descending(arr: List[int]) -> List[int]:
    """Sorts a list in descending order by building a Min-Heap and repeatedly extracting the minimum."""
    a = arr.copy()
    n = len(a)

    def _heapify_min(heap_size: int, i: int):
        smallest = i
        left = 2 * i + 1
        right = 2 * i + 2
        if left < heap_size and a[left] < a[smallest]:
            smallest = left
        if right < heap_size and a[right] < a[smallest]:
            smallest = right
        if smallest != i:
            a[i], a[smallest] = a[smallest], a[i]
            _heapify_min(heap_size, smallest)

    for i in range(n // 2 - 1, -1, -1):
        _heapify_min(n, i)

    for i in range(n - 1, 0, -1):
        a[0], a[i] = a[i], a[0]
        _heapify_min(i, 0)

    return a


if __name__ == "__main__":
    sample = [4, 10, 3, 5, 1, -7, 10, 2]
    print("Original:   ", sample)
    print("Ascending:  ", heap_sort(sample))
    print("Descending: ", heap_sort_descending(sample))
