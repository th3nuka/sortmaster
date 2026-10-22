const assert = require('assert');
const SortingEngine = require('../src/main/resources/static/js/sorting-engine.js');

const algorithms = [
  'bubble-sort', 'selection-sort', 'insertion-sort', 'merge-sort',
  'quick-sort', 'heap-sort', 'shell-sort', 'counting-sort',
  'radix-sort', 'bucket-sort', 'cocktail-shaker-sort', 'comb-sort',
  'gnome-sort', 'cycle-sort', 'pigeonhole-sort', 'tim-sort'
];

console.log('--- Testing JavaScript Client-Side Sorting Engine ---');

const testCases = [
  { name: 'Standard unsorted', array: [5, 2, 8, 1, 4] },
  { name: 'With duplicates', array: [7, 3, 7, 2, 3, 1, 5] },
  { name: 'Reverse sorted', array: [9, 8, 7, 6, 5, 4, 3, 2, 1] },
  { name: 'Already sorted', array: [1, 2, 3, 4, 5] },
  { name: 'Single element', array: [42] },
  { name: 'Empty array', array: [] }
];

let totalChecks = 0;

for (const algoId of algorithms) {
  for (const tc of testCases) {
    // Ascending
    const ascRes = SortingEngine.sort(algoId, tc.array, 'NORMAL', 'ASCENDING');
    const expectedAsc = [...tc.array].sort((a, b) => a - b);
    assert.deepStrictEqual(ascRes.sortedArray, expectedAsc, `${algoId} failed ASCENDING on ${tc.name}`);
    assert(ascRes.events.length > 0 || tc.array.length === 0);
    totalChecks++;

    // Descending
    const descRes = SortingEngine.sort(algoId, tc.array, 'NORMAL', 'DESCENDING');
    const expectedDesc = [...tc.array].sort((a, b) => b - a);
    assert.deepStrictEqual(descRes.sortedArray, expectedDesc, `${algoId} failed DESCENDING on ${tc.name}`);
    assert(descRes.events.length > 0 || tc.array.length === 0);
    totalChecks++;
  }

  // Compare test
  const compRes = SortingEngine.compare([algoId, 'bubble-sort'], [5, 1, 9, 3], 'ASCENDING');
  assert.strictEqual(compRes.results.length, 2);
  totalChecks++;

  console.log(`✓ ${algoId}: Passed all Ascending, Descending, and Compare checks`);
}

console.log(`\n🎉 ALL ${totalChecks} JAVASCRIPT SORTING ENGINE CHECKS PASSED! 🎉\n`);
