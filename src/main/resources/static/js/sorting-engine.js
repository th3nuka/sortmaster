/**
 * SortMaster Client-Side Sorting Engine
 * Enables 100% offline, static execution of all 16 sorting algorithms
 * with frame-by-frame animation snapshots and telemetry identical to the Java backend.
 */

(function (global) {
  'use strict';

  const MAX_EVENTS = 5000;

  class AlgorithmExecution {
    constructor(id, name, array, order = 'ASCENDING') {
      this.id = id;
      this.name = name;
      this.originalArray = [...array];
      this.workArray = [...array];
      this.order = order.toUpperCase();
      this.isAscending = this.order === 'ASCENDING';
      this.isDescending = !this.isAscending;

      this.stepCounter = 0;
      this.comparisons = 0;
      this.swaps = 0;
      this.writes = 0;
      this.events = [];
      this.sortedIndices = new Set();
      this.eventLimitReached = false;
      this.startTime = performance.now();
    }

    isOutOfOrder(first, second) {
      return this.isAscending ? (first > second) : (first < second);
    }

    isBeforeOrEqual(first, second) {
      return this.isAscending ? (first <= second) : (first >= second);
    }

    isStrictlyBefore(first, second) {
      return this.isAscending ? (first < second) : (first > second);
    }

    recordEvent(type, array, indices = [], message = '', activeRange = null, pivotIndex = null, conceptualStep = '') {
      if (this.events.length >= MAX_EVENTS) {
        this.eventLimitReached = true;
        return;
      }
      this.stepCounter++;
      this.events.push({
        stepIndex: this.stepCounter,
        step: this.stepCounter,
        eventType: type,
        type: type,
        array: [...array],
        snapshot: [...array],
        indices: [...indices],
        activeIndices: [...indices],
        message: message,
        activeRange: activeRange ? [...activeRange] : null,
        pivotIndex: pivotIndex,
        sortedIndices: Array.from(this.sortedIndices),
        conceptualStep: conceptualStep
      });
    }

    emitStart(arr, message) {
      this.recordEvent('START', arr, [], message, null, null, 'start');
    }

    emitCompare(arr, i, j, message, conceptualStep = 'compare') {
      this.comparisons++;
      this.recordEvent('COMPARE', arr, [i, j], message, null, null, conceptualStep);
    }

    emitSwap(arr, i, j, message, conceptualStep = 'swap') {
      this.swaps++;
      this.writes += 2;
      this.recordEvent('SWAP', arr, [i, j], message, null, null, conceptualStep);
    }

    emitWrite(arr, index, value, message, conceptualStep = 'write') {
      this.writes++;
      arr[index] = value;
      this.recordEvent('WRITE', arr, [index], message, null, null, conceptualStep);
    }

    emitInsert(arr, index, message, conceptualStep = 'insert') {
      this.writes++;
      this.recordEvent('INSERT', arr, [index], message, null, null, conceptualStep);
    }

    emitPivot(arr, pivotIdx, range, message, conceptualStep = 'pivot') {
      this.recordEvent('PIVOT', arr, [pivotIdx], message, range, pivotIdx, conceptualStep);
    }

    emitPartition(arr, left, right, pivotIdx, message, conceptualStep = 'partition') {
      this.recordEvent('PARTITION', arr, [left, right], message, [left, right], pivotIdx, conceptualStep);
    }

    emitMerge(arr, left, mid, right, message, conceptualStep = 'merge') {
      this.recordEvent('MERGE', arr, [left, mid, right], message, [left, right], null, conceptualStep);
    }

    emitMarkSorted(arr, indices, message, conceptualStep = 'mark_sorted') {
      indices.forEach(idx => this.sortedIndices.add(idx));
      this.recordEvent('MARK_SORTED', arr, indices, message, null, null, conceptualStep);
    }

    emitComplete(arr, message) {
      this.recordEvent('COMPLETE', arr, [], message, null, null, 'complete');
    }

    swap(arr, i, j, conceptualStep = 'swap') {
      const temp = arr[i];
      arr[i] = arr[j];
      arr[j] = temp;
      this.emitSwap(arr, i, j, `Swapped index ${i} (${arr[j]}) and index ${j} (${arr[i]}).`, conceptualStep);
    }

    compare(arr, i, j, conceptualStep = 'compare') {
      this.emitCompare(arr, i, j, `Comparing index ${i} (${arr[i]}) with index ${j} (${arr[j]}) in ${this.order.toLowerCase()} order.`, conceptualStep);
      return arr[i] === arr[j] ? 0 : (arr[i] < arr[j] ? -1 : 1);
    }

    compareOrdered(arr, i, j, conceptualStep = 'compare') {
      this.emitCompare(arr, i, j, `Comparing index ${i} (${arr[i]}) with index ${j} (${arr[j]}) in ${this.order.toLowerCase()} order.`, conceptualStep);
      if (arr[i] === arr[j]) return 0;
      return this.isAscending ? (arr[i] - arr[j]) : (arr[j] - arr[i]);
    }

    finish() {
      for (let i = 0; i < this.workArray.length; i++) {
        this.sortedIndices.add(i);
      }
      this.emitComplete(this.workArray, `${this.name} completed successfully in ${this.order} order! Array is fully sorted.`);
      const elapsed = Math.max(0.1, performance.now() - this.startTime);
      return {
        algorithm: this.id,
        algorithmId: this.id,
        algorithmName: this.name,
        initialArray: this.originalArray,
        originalArray: this.originalArray,
        sortedArray: this.workArray,
        totalSteps: this.events.length,
        events: this.events,
        order: this.order,
        metadata: SortingEngine.getAlgorithmMetadata(this.id),
        statistics: {
          comparisons: this.comparisons,
          swaps: this.swaps,
          writes: this.writes,
          executionTimeMs: Math.round(elapsed * 100) / 100,
          totalEvents: this.events.length
        }
      };
    }
  }

  // --- Algorithm Implementations ---

  function bubbleSort(exec) {
    const a = exec.workArray;
    const n = a.length;
    for (let i = 0; i < n - 1; i++) {
      let swapped = false;
      for (let j = 0; j < n - i - 1; j++) {
        const cmp = exec.compare(a, j, j + 1, 'compare_adjacent');
        const shouldSwap = exec.isAscending ? (cmp > 0) : (cmp < 0);
        if (shouldSwap) {
          exec.swap(a, j, j + 1, 'swap_elements');
          swapped = true;
        }
      }
      exec.emitMarkSorted(a, [n - 1 - i], `Element at index ${n - 1 - i} is in its final sorted position.`, 'mark_sorted');
      if (!swapped) break;
    }
  }

  function selectionSort(exec) {
    const a = exec.workArray;
    const n = a.length;
    for (let i = 0; i < n - 1; i++) {
      let targetIdx = i;
      exec.emitPivot(a, i, [i, n - 1], `Starting pass ${i + 1}: current target candidate is index ${i} (${a[i]}).`, 'set_target');
      for (let j = i + 1; j < n; j++) {
        const cmp = exec.compare(a, j, targetIdx, 'compare_candidate');
        const isBetter = exec.isAscending ? (cmp < 0) : (cmp > 0);
        if (isBetter) {
          targetIdx = j;
          exec.emitPivot(a, targetIdx, [i, n - 1], `New target candidate found at index ${j} (${a[j]}).`, 'new_candidate');
        }
      }
      if (targetIdx !== i) {
        exec.swap(a, i, targetIdx, 'swap_to_position');
      }
      exec.emitMarkSorted(a, [i], `Position ${i} is now sorted with value ${a[i]}.`, 'mark_sorted');
    }
  }

  function insertionSort(exec) {
    const a = exec.workArray;
    const n = a.length;
    exec.emitMarkSorted(a, [0], `Index 0 (${a[0]}) forms initial sorted prefix.`, 'init_prefix');
    for (let i = 1; i < n; i++) {
      const key = a[i];
      exec.emitPivot(a, i, [0, i], `Selected key = ${key} at index ${i}.`, 'select_key');
      let j = i - 1;
      while (j >= 0) {
        exec.emitCompare(a, j, j + 1, `Comparing key ${key} with prefix element index ${j} (${a[j]}).`, 'compare_key');
        const shouldShift = exec.isAscending ? (a[j] > key) : (a[j] < key);
        if (shouldShift) {
          exec.emitWrite(a, j + 1, a[j], `Shifted element ${a[j]} right to index ${j + 1}.`, 'shift_right');
          j--;
        } else {
          break;
        }
      }
      exec.emitWrite(a, j + 1, key, `Inserted key ${key} at index ${j + 1}.`, 'insert_key');
      const sortedNow = [];
      for (let s = 0; s <= i; s++) sortedNow.push(s);
      exec.emitMarkSorted(a, sortedNow, `Sorted prefix extended to index ${i}.`, 'mark_sorted');
    }
  }

  function mergeSort(exec) {
    const a = exec.workArray;
    function sortRange(left, right) {
      if (left >= right) return;
      const mid = Math.floor((left + right) / 2);
      exec.emitPartition(a, left, right, mid, `Dividing range [${left}..${right}] into [${left}..${mid}] and [${mid + 1}..${right}].`, 'divide');
      sortRange(left, mid);
      sortRange(mid + 1, right);
      merge(left, mid, right);
    }

    function merge(left, mid, right) {
      exec.emitMerge(a, left, mid, right, `Merging sorted subarrays [${left}..${mid}] and [${mid + 1}..${right}].`, 'merge_start');
      const leftSub = a.slice(left, mid + 1);
      const rightSub = a.slice(mid + 1, right + 1);
      let i = 0, j = 0, k = left;

      while (i < leftSub.length && j < rightSub.length) {
        exec.comparisons++;
        exec.recordEvent('COMPARE', a, [left + i, mid + 1 + j], `Comparing left subarray ${leftSub[i]} with right subarray ${rightSub[j]}.`, [left, right], null, 'compare_elements');
        const takeLeft = exec.isAscending ? (leftSub[i] <= rightSub[j]) : (leftSub[i] >= rightSub[j]);
        if (takeLeft) {
          exec.emitWrite(a, k, leftSub[i], `Placed ${leftSub[i]} at index ${k}.`, 'place_element');
          i++;
        } else {
          exec.emitWrite(a, k, rightSub[j], `Placed ${rightSub[j]} at index ${k}.`, 'place_element');
          j++;
        }
        k++;
      }
      while (i < leftSub.length) {
        exec.emitWrite(a, k, leftSub[i], `Placed remaining left element ${leftSub[i]} at index ${k}.`, 'place_remaining');
        i++;
        k++;
      }
      while (j < rightSub.length) {
        exec.emitWrite(a, k, rightSub[j], `Placed remaining right element ${rightSub[j]} at index ${k}.`, 'place_remaining');
        j++;
        k++;
      }
    }
    sortRange(0, a.length - 1);
  }

  function quickSort(exec) {
    const a = exec.workArray;
    function sortRange(low, high) {
      if (low < high) {
        const pi = partition(low, high);
        sortRange(low, pi - 1);
        sortRange(pi + 1, high);
      } else if (low === high) {
        exec.emitMarkSorted(a, [low], `Index ${low} is sorted.`, 'single_sorted');
      }
    }

    function partition(low, high) {
      const pivot = a[high];
      exec.emitPivot(a, high, [low, high], `Chosen pivot = ${pivot} at index ${high} for range [${low}..${high}].`, 'choose_pivot');
      let i = low - 1;
      for (let j = low; j < high; j++) {
        const cmp = exec.compare(a, j, high, 'compare_with_pivot');
        const belongsInLeft = exec.isAscending ? (cmp < 0) : (cmp > 0);
        if (belongsInLeft) {
          i++;
          if (i !== j) exec.swap(a, i, j, 'swap_partition');
        }
      }
      exec.swap(a, i + 1, high, 'place_pivot');
      exec.emitMarkSorted(a, [i + 1], `Pivot ${a[i + 1]} placed in final sorted position ${i + 1}.`, 'pivot_placed');
      return i + 1;
    }
    sortRange(0, a.length - 1);
  }

  function heapSort(exec) {
    const a = exec.workArray;
    const n = a.length;
    // Build heap
    for (let i = Math.floor(n / 2) - 1; i >= 0; i--) {
      heapify(n, i);
    }
    // Extract elements
    for (let i = n - 1; i > 0; i--) {
      exec.swap(a, 0, i, 'extract_root');
      exec.emitMarkSorted(a, [i], `Element ${a[i]} at index ${i} is in final position.`, 'mark_sorted');
      heapify(i, 0);
    }

    function heapify(heapSize, root) {
      let extreme = root;
      const left = 2 * root + 1;
      const right = 2 * root + 2;

      if (left < heapSize) {
        const cmp = exec.compare(a, left, extreme, 'compare_left_child');
        const leftIsBetter = exec.isAscending ? (cmp > 0) : (cmp < 0);
        if (leftIsBetter) extreme = left;
      }
      if (right < heapSize) {
        const cmp = exec.compare(a, right, extreme, 'compare_right_child');
        const rightIsBetter = exec.isAscending ? (cmp > 0) : (cmp < 0);
        if (rightIsBetter) extreme = right;
      }
      if (extreme !== root) {
        exec.swap(a, root, extreme, 'sift_down');
        heapify(heapSize, extreme);
      }
    }
  }

  function shellSort(exec) {
    const a = exec.workArray;
    const n = a.length;
    for (let gap = Math.floor(n / 2); gap > 0; gap = Math.floor(gap / 2)) {
      exec.emitStart(a, `Shell Sort pass with gap = ${gap}.`);
      for (let i = gap; i < n; i++) {
        const temp = a[i];
        let j = i;
        while (j >= gap) {
          exec.emitCompare(a, j - gap, j, `Comparing index ${j - gap} with index ${j} (gap = ${gap}).`, 'compare_gap');
          const shouldShift = exec.isAscending ? (a[j - gap] > temp) : (a[j - gap] < temp);
          if (shouldShift) {
            exec.emitWrite(a, j, a[j - gap], `Shifted ${a[j - gap]} to index ${j}.`, 'shift');
            j -= gap;
          } else {
            break;
          }
        }
        exec.emitWrite(a, j, temp, `Placed ${temp} at index ${j}.`, 'insert_gap');
      }
    }
  }

  function countingSort(exec) {
    const a = exec.workArray;
    const n = a.length;
    if (n <= 1) return;
    let min = a[0], max = a[0];
    for (let v of a) {
      if (v < min) min = v;
      if (v > max) max = v;
    }
    const range = max - min + 1;
    const count = new Array(range).fill(0);

    for (let i = 0; i < n; i++) {
      count[a[i] - min]++;
      exec.emitCompare(a, i, i, `Counted frequency of element ${a[i]}.`, 'count_frequency');
    }

    if (exec.isAscending) {
      for (let i = 1; i < range; i++) count[i] += count[i - 1];
    } else {
      for (let i = range - 2; i >= 0; i--) count[i] += count[i + 1];
    }

    const output = new Array(n);
    for (let i = n - 1; i >= 0; i--) {
      const idx = a[i] - min;
      output[count[idx] - 1] = a[i];
      count[idx]--;
    }

    for (let i = 0; i < n; i++) {
      exec.emitWrite(a, i, output[i], `Writing sorted value ${output[i]} into index ${i}.`, 'write_output');
      exec.emitMarkSorted(a, [i], `Index ${i} sorted.`, 'mark_sorted');
    }
  }

  function radixSort(exec) {
    const a = exec.workArray;
    const n = a.length;
    if (n <= 1) return;

    let minVal = a[0], maxVal = a[0];
    for (let v of a) {
      if (v < minVal) minVal = v;
      if (v > maxVal) maxVal = v;
    }
    const maxOffset = maxVal - minVal;

    for (let exp = 1; Math.floor(maxOffset / exp) > 0 || exp === 1; exp *= 10) {
      exec.emitStart(a, `Radix Sort: processing digit bucket exp = ${exp} (10^${Math.log10(exp)}).`);
      const count = new Array(10).fill(0);
      const output = new Array(n);

      for (let i = 0; i < n; i++) {
        const digit = Math.floor(((a[i] - minVal) / exp)) % 10;
        count[digit]++;
        exec.emitCompare(a, i, i, `Examined digit ${digit} for element ${a[i]}.`, 'digit_inspect');
      }

      if (exec.isAscending) {
        for (let i = 1; i < 10; i++) count[i] += count[i - 1];
      } else {
        for (let i = 8; i >= 0; i--) count[i] += count[i + 1];
      }

      for (let i = n - 1; i >= 0; i--) {
        const digit = Math.floor(((a[i] - minVal) / exp)) % 10;
        output[count[digit] - 1] = a[i];
        count[digit]--;
      }

      for (let i = 0; i < n; i++) {
        exec.emitWrite(a, i, output[i], `Placed ${output[i]} into index ${i} for digit pass ${exp}.`, 'bucket_flush');
      }
      if (Math.floor(maxOffset / (exp * 10)) === 0) break;
    }
  }

  function bucketSort(exec) {
    const a = exec.workArray;
    const n = a.length;
    if (n <= 1) return;
    let min = a[0], max = a[0];
    for (let v of a) {
      if (v < min) min = v;
      if (v > max) max = v;
    }
    const bucketCount = Math.max(2, Math.min(n, 10));
    const range = (max - min) / bucketCount || 1;
    const buckets = Array.from({ length: bucketCount }, () => []);

    for (let i = 0; i < n; i++) {
      let bIdx = Math.floor((a[i] - min) / range);
      if (bIdx >= bucketCount) bIdx = bucketCount - 1;
      buckets[bIdx].push(a[i]);
      exec.emitCompare(a, i, i, `Assigned element ${a[i]} to bucket ${bIdx}.`, 'assign_bucket');
    }

    // Sort buckets
    for (let b = 0; b < bucketCount; b++) {
      buckets[b].sort((x, y) => exec.isAscending ? (x - y) : (y - x));
    }

    let writeIdx = 0;
    const bucketIter = exec.isAscending ? buckets : buckets.slice().reverse();
    for (let bucket of bucketIter) {
      for (let val of bucket) {
        exec.emitWrite(a, writeIdx, val, `Extracted ${val} from bucket into index ${writeIdx}.`, 'flush_bucket');
        exec.emitMarkSorted(a, [writeIdx], `Index ${writeIdx} sorted.`, 'mark_sorted');
        writeIdx++;
      }
    }
  }

  function cocktailShakerSort(exec) {
    const a = exec.workArray;
    let swapped = true;
    let start = 0;
    let end = a.length - 1;

    while (swapped) {
      swapped = false;
      for (let i = start; i < end; i++) {
        const cmp = exec.compare(a, i, i + 1, 'compare_forward');
        const shouldSwap = exec.isAscending ? (cmp > 0) : (cmp < 0);
        if (shouldSwap) {
          exec.swap(a, i, i + 1, 'swap_forward');
          swapped = true;
        }
      }
      exec.emitMarkSorted(a, [end], `Forward pass completed: index ${end} sorted.`, 'mark_sorted');
      if (!swapped) break;
      swapped = false;
      end--;

      for (let i = end - 1; i >= start; i--) {
        const cmp = exec.compare(a, i, i + 1, 'compare_backward');
        const shouldSwap = exec.isAscending ? (cmp > 0) : (cmp < 0);
        if (shouldSwap) {
          exec.swap(a, i, i + 1, 'swap_backward');
          swapped = true;
        }
      }
      exec.emitMarkSorted(a, [start], `Backward pass completed: index ${start} sorted.`, 'mark_sorted');
      start++;
    }
  }

  function combSort(exec) {
    const a = exec.workArray;
    const n = a.length;
    let gap = n;
    const shrink = 1.3;
    let sorted = false;

    while (!sorted) {
      gap = Math.floor(gap / shrink);
      if (gap <= 1) {
        gap = 1;
        sorted = true;
      }
      exec.emitStart(a, `Comb Sort pass with gap = ${gap}.`);
      for (let i = 0; i + gap < n; i++) {
        const cmp = exec.compare(a, i, i + gap, 'compare_comb');
        const shouldSwap = exec.isAscending ? (cmp > 0) : (cmp < 0);
        if (shouldSwap) {
          exec.swap(a, i, i + gap, 'swap_comb');
          sorted = false;
        }
      }
    }
  }

  function gnomeSort(exec) {
    const a = exec.workArray;
    const n = a.length;
    let idx = 0;

    while (idx < n) {
      if (idx === 0) {
        idx++;
      } else {
        const cmp = exec.compare(a, idx, idx - 1, 'compare_gnome');
        const inOrder = exec.isAscending ? (cmp >= 0) : (cmp <= 0);
        if (inOrder) {
          idx++;
        } else {
          exec.swap(a, idx, idx - 1, 'swap_gnome');
          idx--;
        }
      }
    }
  }

  function cycleSort(exec) {
    const a = exec.workArray;
    const n = a.length;

    for (let cycleStart = 0; cycleStart < n - 1; cycleStart++) {
      let item = a[cycleStart];
      let pos = cycleStart;

      for (let i = cycleStart + 1; i < n; i++) {
        exec.emitCompare(a, i, cycleStart, `Finding rank for element ${item}.`, 'count_rank');
        const comesBefore = exec.isAscending ? (a[i] < item) : (a[i] > item);
        if (comesBefore) pos++;
      }

      if (pos === cycleStart) {
        exec.emitMarkSorted(a, [cycleStart], `Element ${item} already at correct position.`, 'mark_sorted');
        continue;
      }

      while (item === a[pos]) pos++;
      if (pos !== cycleStart) {
        const temp = item;
        item = a[pos];
        exec.emitWrite(a, pos, temp, `Put ${temp} into cycle position ${pos}.`, 'write_cycle');
        exec.emitMarkSorted(a, [pos], `Position ${pos} sorted.`, 'mark_sorted');
      }

      while (pos !== cycleStart) {
        pos = cycleStart;
        for (let i = cycleStart + 1; i < n; i++) {
          exec.emitCompare(a, i, cycleStart, `Finding rank for element ${item}.`, 'count_rank');
          const comesBefore = exec.isAscending ? (a[i] < item) : (a[i] > item);
          if (comesBefore) pos++;
        }
        while (item === a[pos]) pos++;
        if (item !== a[pos]) {
          const temp = item;
          item = a[pos];
          exec.emitWrite(a, pos, temp, `Put ${temp} into cycle position ${pos}.`, 'write_cycle');
          exec.emitMarkSorted(a, [pos], `Position ${pos} sorted.`, 'mark_sorted');
        }
      }
    }
  }

  function pigeonholeSort(exec) {
    const a = exec.workArray;
    const n = a.length;
    if (n <= 1) return;
    let min = a[0], max = a[0];
    for (let v of a) {
      if (v < min) min = v;
      if (v > max) max = v;
    }
    const range = max - min + 1;
    const holes = Array.from({ length: range }, () => []);

    for (let i = 0; i < n; i++) {
      holes[a[i] - min].push(a[i]);
      exec.emitCompare(a, i, i, `Placed ${a[i]} into pigeonhole ${a[i] - min}.`, 'pigeonhole_assign');
    }

    let writeIdx = 0;
    const holeIter = exec.isAscending ? holes : holes.slice().reverse();
    for (let hole of holeIter) {
      for (let val of hole) {
        exec.emitWrite(a, writeIdx, val, `Extracted ${val} from pigeonhole into index ${writeIdx}.`, 'pigeonhole_flush');
        exec.emitMarkSorted(a, [writeIdx], `Position ${writeIdx} sorted.`, 'mark_sorted');
        writeIdx++;
      }
    }
  }

  function timSort(exec) {
    const a = exec.workArray;
    const n = a.length;
    if (n <= 1) return;

    const minRun = Math.max(4, Math.min(32, Math.floor(n / 2) || 4));
    exec.emitStart(a, `TimSort initialized with minRun = ${minRun}.`);

    function insertionSortRange(left, right) {
      for (let i = left + 1; i <= right; i++) {
        const key = a[i];
        let j = i - 1;
        while (j >= left) {
          exec.emitCompare(a, j, j + 1, `TimSort run insertion: comparing key ${key} with index ${j}.`, 'timsort_insert');
          const shouldShift = exec.isAscending ? (a[j] > key) : (a[j] < key);
          if (shouldShift) {
            exec.emitWrite(a, j + 1, a[j], `Shifted ${a[j]} right.`, 'timsort_shift');
            j--;
          } else {
            break;
          }
        }
        exec.emitWrite(a, j + 1, key, `Placed key ${key} at index ${j + 1}.`, 'timsort_place');
      }
    }

    for (let i = 0; i < n; i += minRun) {
      const right = Math.min(i + minRun - 1, n - 1);
      insertionSortRange(i, right);
    }

    for (let size = minRun; size < n; size = 2 * size) {
      for (let left = 0; left < n; left += 2 * size) {
        const mid = Math.min(n - 1, left + size - 1);
        const right = Math.min(left + 2 * size - 1, n - 1);
        if (mid < right) {
          merge(left, mid, right);
        }
      }
    }

    function merge(left, mid, right) {
      exec.emitMerge(a, left, mid, right, `Merging TimSort runs [${left}..${mid}] and [${mid + 1}..${right}].`, 'timsort_merge');
      const leftSub = a.slice(left, mid + 1);
      const rightSub = a.slice(mid + 1, right + 1);
      let i = 0, j = 0, k = left;

      while (i < leftSub.length && j < rightSub.length) {
        exec.comparisons++;
        exec.recordEvent('COMPARE', a, [left + i, mid + 1 + j], `Comparing ${leftSub[i]} with ${rightSub[j]}.`, [left, right], null, 'timsort_compare');
        const takeLeft = exec.isAscending ? (leftSub[i] <= rightSub[j]) : (leftSub[i] >= rightSub[j]);
        if (takeLeft) {
          exec.emitWrite(a, k, leftSub[i], `Placed ${leftSub[i]} at index ${k}.`, 'timsort_merge_write');
          i++;
        } else {
          exec.emitWrite(a, k, rightSub[j], `Placed ${rightSub[j]} at index ${k}.`, 'timsort_merge_write');
          j++;
        }
        k++;
      }
      while (i < leftSub.length) {
        exec.emitWrite(a, k, leftSub[i], `Placed remaining ${leftSub[i]} at index ${k}.`, 'timsort_merge_rem');
        i++;
        k++;
      }
      while (j < rightSub.length) {
        exec.emitWrite(a, k, rightSub[j], `Placed remaining ${rightSub[j]} at index ${k}.`, 'timsort_merge_rem');
        j++;
        k++;
      }
    }
  }

  const ALGO_DISPATCH = {
    'bubble-sort': { fn: bubbleSort, name: 'Bubble Sort' },
    'selection-sort': { fn: selectionSort, name: 'Selection Sort' },
    'insertion-sort': { fn: insertionSort, name: 'Insertion Sort' },
    'merge-sort': { fn: mergeSort, name: 'Merge Sort' },
    'quick-sort': { fn: quickSort, name: 'Quick Sort' },
    'heap-sort': { fn: heapSort, name: 'Heap Sort' },
    'shell-sort': { fn: shellSort, name: 'Shell Sort' },
    'counting-sort': { fn: countingSort, name: 'Counting Sort' },
    'radix-sort': { fn: radixSort, name: 'Radix Sort' },
    'bucket-sort': { fn: bucketSort, name: 'Bucket Sort' },
    'cocktail-shaker-sort': { fn: cocktailShakerSort, name: 'Cocktail Shaker Sort' },
    'comb-sort': { fn: combSort, name: 'Comb Sort' },
    'gnome-sort': { fn: gnomeSort, name: 'Gnome Sort' },
    'cycle-sort': { fn: cycleSort, name: 'Cycle Sort' },
    'pigeonhole-sort': { fn: pigeonholeSort, name: 'Pigeonhole Sort' },
    'tim-sort': { fn: timSort, name: 'Tim Sort' }
  };

  const SortingEngine = {
    getAllMetadata() {
      if (global.SORTMASTER_ALGORITHMS_DATA) {
        return global.SORTMASTER_ALGORITHMS_DATA;
      }
      return Object.keys(ALGO_DISPATCH).map(id => ({
        id,
        name: ALGO_DISPATCH[id].name
      }));
    },

    getAlgorithmMetadata(id) {
      const all = this.getAllMetadata();
      return all.find(a => a.id.toLowerCase() === id.toLowerCase()) || null;
    },

    sort(algorithmId, array, detailLevel = 'NORMAL', order = 'ASCENDING') {
      const entry = ALGO_DISPATCH[algorithmId.toLowerCase()];
      if (!entry) {
        throw new Error(`Unknown algorithm: ${algorithmId}`);
      }
      const exec = new AlgorithmExecution(algorithmId, entry.name, array, order);
      exec.emitStart(exec.workArray, `Beginning ${entry.name} in ${order.toUpperCase()} order on ${array.length} elements.`);
      if (exec.workArray.length <= 1) {
        return exec.finish();
      }
      entry.fn(exec);
      return exec.finish();
    },

    compare(algorithmIds, array, order = 'ASCENDING') {
      const results = [];
      let fastestTime = Infinity;
      let winnerId = null;
      let minComps = Infinity;
      let leastCompsId = null;

      for (const id of algorithmIds) {
        const res = this.sort(id, array, 'NORMAL', order);
        results.push({
          algorithmId: id,
          algorithmName: res.algorithmName,
          executionTimeMs: res.statistics.executionTimeMs,
          comparisons: res.statistics.comparisons,
          swaps: res.statistics.swaps,
          writes: res.statistics.writes,
          totalEvents: res.statistics.totalEvents,
          timeComplexity: this.getAlgorithmMetadata(id)?.averageTimeComplexity || 'O(n log n)',
          spaceComplexity: this.getAlgorithmMetadata(id)?.spaceComplexity || 'O(1)',
          stable: this.getAlgorithmMetadata(id)?.stable ?? false
        });

        if (res.statistics.executionTimeMs < fastestTime) {
          fastestTime = res.statistics.executionTimeMs;
          winnerId = id;
        }
        if (res.statistics.comparisons < minComps) {
          minComps = res.statistics.comparisons;
          leastCompsId = id;
        }
      }

      return {
        results,
        winnerId,
        fastestTimeMs: fastestTime,
        leastComparisonsId: leastCompsId,
        arraySize: array.length,
        order
      };
    }
  };

  global.SortingEngine = SortingEngine;
  if (typeof module !== 'undefined' && module.exports) {
    module.exports = SortingEngine;
  }
})(typeof window !== 'undefined' ? window : global);
