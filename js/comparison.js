/**
 * SortMaster Multi-Algorithm Comparison Lab
 * Runs fair benchmarks across selected algorithms on identical inputs (Zero Emojis)
 */

const ComparisonLab = {
  allAlgorithms: [],
  currentArray: [],
  currentOrder: 'ASCENDING',

  init(algorithms) {
    this.allAlgorithms = algorithms || [];
    this.renderCheckboxes();
    this.generateTestArray('random');
    this.bindEvents();
    this.updateOrderUI();
  },

  setOrder(order, syncGlobal = true) {
    const normalized = (order || 'ASCENDING').toUpperCase();
    this.currentOrder = normalized;
    this.updateOrderUI();

    if (syncGlobal && window.App && typeof window.App.setOrder === 'function') {
      window.App.setOrder(this.currentOrder, false);
    }
  },

  updateOrderUI() {
    const ascBtn = document.getElementById('compare-order-btn-asc');
    const descBtn = document.getElementById('compare-order-btn-desc');
    const badge = document.getElementById('compare-order-badge');

    const isAsc = this.currentOrder === 'ASCENDING';
    if (ascBtn) {
      ascBtn.classList.toggle('active', isAsc);
      ascBtn.setAttribute('aria-checked', isAsc ? 'true' : 'false');
      ascBtn.setAttribute('tabindex', isAsc ? '0' : '-1');
    }
    if (descBtn) {
      descBtn.classList.toggle('active', !isAsc);
      descBtn.setAttribute('aria-checked', !isAsc ? 'true' : 'false');
      descBtn.setAttribute('tabindex', !isAsc ? '0' : '-1');
    }
    if (badge) {
      badge.textContent = isAsc
        ? 'Ascending: Smallest to Largest'
        : 'Descending: Largest to Smallest';
    }
  },

  renderCheckboxes() {
    const grid = document.getElementById('compare-algo-checkboxes');
    if (!grid) return;

    grid.innerHTML = '';
    const defaultSelected = new Set(['quick-sort', 'merge-sort', 'heap-sort', 'tim-sort', 'bubble-sort', 'insertion-sort']);

    this.allAlgorithms.forEach(algo => {
      const label = document.createElement('label');
      label.className = 'algo-check-label';

      const isChecked = defaultSelected.has(algo.id) ? 'checked' : '';
      label.innerHTML = `
        <input type="checkbox" name="compare-algo" value="${algo.id}" ${isChecked}>
        <span>${algo.name}</span>
      `;
      grid.appendChild(label);
    });
  },

  generateTestArray(type) {
    const size = 30;
    let arr = [];

    if (type === 'random') {
      for (let i = 0; i < size; i++) arr.push(Math.floor(Math.random() * 90) + 10);
    } else if (type === 'reversed') {
      for (let i = size; i >= 1; i--) arr.push(i * 3);
    } else if (type === 'nearly_sorted') {
      for (let i = 1; i <= size; i++) arr.push(i * 3);
      for (let k = 0; k < 3; k++) {
        const i = Math.floor(Math.random() * size);
        const j = Math.floor(Math.random() * size);
        const tmp = arr[i]; arr[i] = arr[j]; arr[j] = tmp;
      }
    } else if (type === 'duplicates') {
      const pool = [10, 25, 40, 65, 80];
      for (let i = 0; i < size; i++) arr.push(pool[Math.floor(Math.random() * pool.length)]);
    }

    this.currentArray = arr;
    const previewEl = document.getElementById('compare-array-preview');
    if (previewEl) {
      previewEl.textContent = `[${arr.slice(0, 15).join(', ')} ... (${arr.length} elements)]`;
    }
  },

  bindEvents() {
    const runBtn = document.getElementById('run-compare-btn');
    if (runBtn) {
      runBtn.addEventListener('click', () => this.runBenchmark());
    }

    const ascBtn = document.getElementById('compare-order-btn-asc');
    const descBtn = document.getElementById('compare-order-btn-desc');
    const orderControl = document.getElementById('compare-order-selector');

    if (ascBtn) {
      ascBtn.addEventListener('click', () => this.setOrder('ASCENDING', true));
    }
    if (descBtn) {
      descBtn.addEventListener('click', () => this.setOrder('DESCENDING', true));
    }

    if (orderControl) {
      orderControl.addEventListener('keydown', (e) => {
        if (e.key === 'ArrowLeft' || e.key === 'ArrowUp' || e.key === 'Home') {
          e.preventDefault();
          this.setOrder('ASCENDING', true);
          if (ascBtn) ascBtn.focus();
        } else if (e.key === 'ArrowRight' || e.key === 'ArrowDown' || e.key === 'End') {
          e.preventDefault();
          this.setOrder('DESCENDING', true);
          if (descBtn) descBtn.focus();
        }
      });
    }

    const typeSelect = document.getElementById('compare-array-type');
    if (typeSelect) {
      typeSelect.addEventListener('change', (e) => {
        this.generateTestArray(e.target.value);
      });
    }

    const selectAllBtn = document.getElementById('compare-select-all-btn');
    if (selectAllBtn) {
      selectAllBtn.addEventListener('click', () => {
        document.querySelectorAll('input[name="compare-algo"]').forEach(cb => cb.checked = true);
      });
    }

    const deselectAllBtn = document.getElementById('compare-deselect-all-btn');
    if (deselectAllBtn) {
      deselectAllBtn.addEventListener('click', () => {
        document.querySelectorAll('input[name="compare-algo"]').forEach(cb => cb.checked = false);
      });
    }
  },

  async runBenchmark() {
    const selectedCheckboxes = document.querySelectorAll('input[name="compare-algo"]:checked');
    const selectedIds = Array.from(selectedCheckboxes).map(cb => cb.value);

    if (selectedIds.length === 0) {
      alert('Please select at least one algorithm to run the comparison.');
      return;
    }

    const runBtn = document.getElementById('run-compare-btn');
    if (runBtn) {
      runBtn.disabled = true;
      runBtn.innerHTML = `
        <svg class="spin-icon" width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><circle cx="12" cy="12" r="10" stroke-dasharray="32" stroke-linecap="round"></circle></svg>
        Running Benchmark...
      `;
    }

    const statusEl = document.getElementById('compare-results-status');
    if (statusEl) {
      statusEl.textContent = `Benchmarking ${selectedIds.length} algorithms in ${this.currentOrder.toLowerCase()} order...`;
    }

    try {
      const response = await API.compare(selectedIds, this.currentArray, this.currentOrder);
      this.renderResultsTable(response.results, response.order || this.currentOrder);
      if (statusEl) {
        statusEl.textContent = `Completed fair benchmark across ${response.results.length} algorithms in ${this.currentOrder.toLowerCase()} order.`;
      }
    } catch (err) {
      alert(`Benchmark error: ${err.message}`);
      if (statusEl) {
        statusEl.textContent = `Benchmark failed: ${err.message}`;
      }
    } finally {
      if (runBtn) {
        runBtn.disabled = false;
        runBtn.innerHTML = `
          <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polygon points="5 3 19 12 5 21 5 3"/></svg>
          Run Fair Benchmark
        `;
      }
    }
  },

  verifySorted(arr, order) {
    if (!arr || arr.length <= 1) return true;
    const isAsc = (order || 'ASCENDING').toUpperCase() === 'ASCENDING';
    for (let i = 0; i < arr.length - 1; i++) {
      if (isAsc && arr[i] > arr[i + 1]) return false;
      if (!isAsc && arr[i] < arr[i + 1]) return false;
    }
    return true;
  },

  renderResultsTable(results, order) {
    const container = document.getElementById('compare-results-body');
    if (!container || !results) return;

    const maxComps = Math.max(...results.map(r => (r.statistics && r.statistics.comparisons) || 1));
    const maxWrites = Math.max(...results.map(r => (r.statistics && r.statistics.writes) || 1));
    const activeOrder = (order || this.currentOrder || 'ASCENDING').toUpperCase();
    const isAsc = activeOrder === 'ASCENDING';

    container.innerHTML = results.map(item => {
      const stats = item.statistics || { comparisons: 0, swaps: 0, writes: 0, executionTimeMillis: 0 };
      const compPct = Math.max(5, Math.min(100, (stats.comparisons / maxComps) * 100));
      const writePct = Math.max(5, Math.min(100, (stats.writes / maxWrites) * 100));
      const timeMs = stats.executionTimeMillis ? stats.executionTimeMillis.toFixed(3) : '< 0.01';

      const isCorrectlySorted = this.verifySorted(item.sortedArray, activeOrder);
      const directionLabel = isAsc ? 'Asc (Valid)' : 'Desc (Valid)';

      const previewSnippet = (item.sortedArray && item.sortedArray.length > 0)
        ? `[${item.sortedArray.slice(0, 4).join(', ')} ... ${item.sortedArray.slice(-2).join(', ')}]`
        : '[]';

      return `
        <tr>
          <td>
            <strong>${item.algorithmName}</strong>
            <div style="font-size: 0.75rem; color: var(--text-muted);">${item.category}</div>
          </td>
          <td><code>${item.bestTimeComplexity}</code></td>
          <td><code>${item.averageTimeComplexity}</code></td>
          <td><code>${item.worstTimeComplexity}</code></td>
          <td><code>${item.spaceComplexity}</code></td>
          <td>
            <span style="color: ${item.stable ? 'var(--success)' : 'var(--danger)'}; font-weight: 700;">
              ${item.stable ? 'Yes' : 'No'}
            </span>
          </td>
          <td>
            <span style="color: ${item.inPlace ? 'var(--success)' : 'var(--danger)'}; font-weight: 700;">
              ${item.inPlace ? 'Yes' : 'No'}
            </span>
          </td>
          <td>
            <div style="font-weight: 700; color: var(--primary);">${stats.comparisons}</div>
            <div class="stat-bar-preview" style="width: ${compPct}%; background: var(--primary);"></div>
          </td>
          <td>
            <div style="font-weight: 700; color: var(--danger);">${stats.swaps}</div>
          </td>
          <td>
            <div style="font-weight: 700; color: var(--secondary);">${stats.writes}</div>
            <div class="stat-bar-preview" style="width: ${writePct}%; background: var(--secondary);"></div>
          </td>
          <td>
            <span style="font-family: var(--font-code); font-weight: 600;">${timeMs} ms</span>
          </td>
          <td>
            <span style="color: ${isCorrectlySorted ? 'var(--success)' : 'var(--danger)'}; font-weight: 700; display: inline-flex; align-items: center; gap: 0.25rem;">
              <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><polyline points="20 6 9 17 4 12"></polyline></svg>
              ${isCorrectlySorted ? directionLabel : 'Invalid Order'}
            </span>
            <div style="font-family: var(--font-code); font-size: 0.72rem; color: var(--text-muted); margin-top: 0.2rem;">
              ${previewSnippet}
            </div>
          </td>
        </tr>
      `;
    }).join('');
  }
};

window.ComparisonLab = ComparisonLab;
