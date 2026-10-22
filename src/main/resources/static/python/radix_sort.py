"""
SortMaster - Radix Sort (LSD)
Time Complexity: O(d * (n + b)) where d = digits, b = base
Auxiliary Space: O(n + b)
Stability: Stable
In-Place: No
"""
from typing import List


def radix_sort(arr: List[int]) -> List[int]:
    """Sorts integers in ascending order using Least Significant Digit (LSD) Radix Sort."""
    if not arr:
        return []

    negatives = [-x for x in arr if x < 0]
    positives = [x for x in arr if x >= 0]

    def _counting_sort_by_digit(a: List[int], exp: int) -> List[int]:
        n = len(a)
        output = [0] * n
        count = [0] * 10
        for x in a:
            digit = (x // exp) % 10
            count[digit] += 1
        for i in range(1, 10):
            count[i] += count[i - 1]
        for x in reversed(a):
            digit = (x // exp) % 10
            output[count[digit] - 1] = x
            count[digit] -= 1
        return output

    def _sort_non_negative(a: List[int]) -> List[int]:
        if not a:
            return []
        max_val = max(a)
        exp = 1
        while max_val // exp > 0:
            a = _counting_sort_by_digit(a, exp)
            exp *= 10
        return a

    sorted_neg = _sort_non_negative(negatives)
    sorted_neg = [-x for x in reversed(sorted_neg)]
    sorted_pos = _sort_non_negative(positives)

    return sorted_neg + sorted_pos


def radix_sort_descending(arr: List[int]) -> List[int]:
    """Sorts integers in descending order using Least Significant Digit (LSD) Radix Sort."""
    if not arr:
        return []

    negatives = [-x for x in arr if x < 0]
    positives = [x for x in arr if x >= 0]

    def _counting_sort_by_digit_desc(a: List[int], exp: int) -> List[int]:
        n = len(a)
        output = [0] * n
        count = [0] * 10
        for x in a:
            digit = (x // exp) % 10
            count[digit] += 1
        for i in range(8, -1, -1):
            count[i] += count[i + 1]
        for x in reversed(a):
            digit = (x // exp) % 10
            output[count[digit] - 1] = x
            count[digit] -= 1
        return output

    def _counting_sort_by_digit_asc(a: List[int], exp: int) -> List[int]:
        n = len(a)
        output = [0] * n
        count = [0] * 10
        for x in a:
            digit = (x // exp) % 10
            count[digit] += 1
        for i in range(1, 10):
            count[i] += count[i - 1]
        for x in reversed(a):
            digit = (x // exp) % 10
            output[count[digit] - 1] = x
            count[digit] -= 1
        return output

    def _sort_desc(a: List[int]) -> List[int]:
        if not a:
            return []
        max_val = max(a)
        exp = 1
        while max_val // exp > 0:
            a = _counting_sort_by_digit_desc(a, exp)
            exp *= 10
        return a

    def _sort_asc(a: List[int]) -> List[int]:
        if not a:
            return []
        max_val = max(a)
        exp = 1
        while max_val // exp > 0:
            a = _counting_sort_by_digit_asc(a, exp)
            exp *= 10
        return a

    sorted_pos = _sort_desc(positives)
    sorted_neg = _sort_asc(negatives)
    return sorted_pos + [-x for x in sorted_neg]


if __name__ == "__main__":
    sample = [170, 45, 75, 90, 802, 24, 2, 66, -15, -3]
    print("Original:   ", sample)
    print("Ascending:  ", radix_sort(sample))
    print("Descending: ", radix_sort_descending(sample))
