/**
 * SortMaster Main Application Orchestrator
 * Controls navigation, views, state management, search, themes, and study academy.
 * Pure Technical Aesthetic, Zero Emojis.
 */

const App = {
  algorithms: [],
  currentAlgorithmId: 'bubble-sort',
  currentArray: [],
  currentOrder: 'ASCENDING',
  visualizer: null,

  async init() {
    this.initTheme();
    this.initNavigation();
    this.initModals();
    this.bindOrderSelector();
    this.initFooter();

    this.visualizer = new SortingVisualizer('visualizer-bars');
    CodePanel.init();

    try {
      this.algorithms = await API.getAllAlgorithms();
      this.renderHomeCards(this.algorithms);
      this.populateVisualizerDropdown();
      this.initStudyGuide();
      ComparisonLab.init(this.algorithms);
      QuizEngine.init();

      // Setup initial array for visualizer
      this.generatePresetArray('random');
      this.loadAlgorithmForVisualization(this.currentAlgorithmId);
    } catch (err) {
      this.showToast(`Backend connection issue: ${err.message}`, 'error');
    }

    this.bindSearchAndFilters();
    this.bindVisualizerToolbar();
  },

  bindOrderSelector() {
    const ascBtn = document.getElementById('order-btn-asc');
    const descBtn = document.getElementById('order-btn-desc');
    const orderControl = document.getElementById('order-selector');

    if (ascBtn) {
      ascBtn.addEventListener('click', () => this.setOrder('ASCENDING'));
    }
    if (descBtn) {
      descBtn.addEventListener('click', () => this.setOrder('DESCENDING'));
    }

    if (orderControl) {
      orderControl.addEventListener('keydown', (e) => {
        if (e.key === 'ArrowLeft' || e.key === 'ArrowUp' || e.key === 'Home') {
          e.preventDefault();
          this.setOrder('ASCENDING');
          if (ascBtn) ascBtn.focus();
        } else if (e.key === 'ArrowRight' || e.key === 'ArrowDown' || e.key === 'End') {
          e.preventDefault();
          this.setOrder('DESCENDING');
          if (descBtn) descBtn.focus();
        }
      });
    }
  },

  setOrder(order, syncOtherViews = true) {
    const normalized = (order || 'ASCENDING').toUpperCase();
    if (this.currentOrder === normalized && this.visualizer && this.visualizer.events.length > 0) {
      return;
    }
    this.currentOrder = normalized;

    // Update Visualizer order buttons
    const ascBtn = document.getElementById('order-btn-asc');
    const descBtn = document.getElementById('order-btn-desc');
    const badge = document.getElementById('order-status-badge');

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

    // Invalidate stale animation steps to prevent accidentally playing previous order steps
    if (this.visualizer) {
      this.visualizer.pause();
      this.visualizer.events = [];
      this.visualizer.currentStep = 0;
      this.visualizer.updateControlsState();
    }

    // Update synchronized code panel
    CodePanel.setOrder(this.currentOrder);

    // Synchronize Compare Lab and Quiz
    if (syncOtherViews) {
      if (window.ComparisonLab && typeof window.ComparisonLab.setOrder === 'function') {
        window.ComparisonLab.setOrder(this.currentOrder, false);
      }
      if (window.QuizEngine && typeof window.QuizEngine.setOrder === 'function') {
        window.QuizEngine.setOrder(this.currentOrder);
      }
    }

    // Keep the currently selected algorithm and input array; reload steps using the new mode
    return this.loadAlgorithmForVisualization(this.currentAlgorithmId);
  },

  initFooter() {
    const yearEl = document.getElementById('current-year');
    if (yearEl) {
      yearEl.textContent = new Date().getFullYear();
    }
  },

  initTheme() {
    const savedTheme = localStorage.getItem('sortmaster_theme') || 'dark';
    document.documentElement.setAttribute('data-theme', savedTheme);
    this.updateThemeIcon(savedTheme);

    const toggleBtn = document.getElementById('theme-toggle-btn');
    if (toggleBtn) {
      toggleBtn.addEventListener('click', () => {
        const current = document.documentElement.getAttribute('data-theme') || 'dark';
        const next = current === 'dark' ? 'light' : 'dark';
        document.documentElement.setAttribute('data-theme', next);
        localStorage.setItem('sortmaster_theme', next);
        this.updateThemeIcon(next);
      });
    }
  },

  updateThemeIcon(theme) {
    const btn = document.getElementById('theme-toggle-btn');
    if (!btn) return;

    if (theme === 'dark') {
      btn.innerHTML = `
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <circle cx="12" cy="12" r="5"></circle>
          <line x1="12" y1="1" x2="12" y2="3"></line>
          <line x1="12" y1="21" x2="12" y2="23"></line>
          <line x1="4.22" y1="4.22" x2="5.64" y2="5.64"></line>
          <line x1="18.36" y1="18.36" x2="19.78" y2="19.78"></line>
          <line x1="1" y1="12" x2="3" y2="12"></line>
          <line x1="21" y1="12" x2="23" y2="12"></line>
          <line x1="4.22" y1="19.78" x2="5.64" y2="18.36"></line>
          <line x1="18.36" y1="5.64" x2="19.78" y2="4.22"></line>
        </svg>
      `;
      btn.title = 'Switch to Light Theme';
    } else {
      btn.innerHTML = `
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"></path>
        </svg>
      `;
      btn.title = 'Switch to Dark Theme';
    }
  },

  initNavigation() {
    const navButtons = document.querySelectorAll('.nav-item button');
    navButtons.forEach(btn => {
      btn.addEventListener('click', () => {
        const targetViewId = btn.dataset.view;
        this.switchView(targetViewId);
      });
    });

    const heroCtaVisualizer = document.getElementById('hero-cta-visualizer');
    if (heroCtaVisualizer) {
      heroCtaVisualizer.addEventListener('click', () => this.switchView('view-visualizer'));
    }


    const heroCtaQuiz = document.getElementById('hero-cta-quiz');
    if (heroCtaQuiz) {
      heroCtaQuiz.addEventListener('click', () => this.switchView('view-quiz'));
    }
  },

  switchView(viewId) {
    document.querySelectorAll('.tab-view').forEach(view => view.classList.remove('active'));
    document.querySelectorAll('.nav-item button').forEach(btn => btn.classList.remove('active'));

    const targetView = document.getElementById(viewId);
    if (targetView) targetView.classList.add('active');

    const matchingNavBtn = document.querySelector(`.nav-item button[data-view="${viewId}"]`);
    if (matchingNavBtn) matchingNavBtn.classList.add('active');

    window.scrollTo({ top: 0, behavior: 'smooth' });
  },

  renderHomeCards(algoList) {
    const grid = document.getElementById('algorithm-cards-grid');
    if (!grid) return;

    grid.innerHTML = '';
    if (!algoList || algoList.length === 0) {
      grid.innerHTML = '<div style="color: var(--text-muted); grid-column: 1 / -1; text-align: center; padding: 3rem;">No algorithms match your search criteria.</div>';
      return;
    }

    algoList.forEach(algo => {
      const card = document.createElement('div');
      card.className = 'algo-card';
      card.innerHTML = `
        <div>
          <div class="algo-card-header">
            <h3 class="algo-title">${algo.name}</h3>
            <span class="algo-category-tag">${algo.category}</span>
          </div>
          <p class="algo-desc">${algo.shortDescription}</p>
          <div class="algo-meta-matrix">
            <div class="meta-row"><span>Average:</span> <span class="meta-val">${algo.averageTimeComplexity}</span></div>
            <div class="meta-row"><span>Worst:</span> <span class="meta-val">${algo.worstTimeComplexity}</span></div>
            <div class="meta-row"><span>Space:</span> <span class="meta-val">${algo.spaceComplexity}</span></div>
            <div class="meta-row"><span>Stable:</span> <span class="meta-val" style="color: ${algo.stable ? 'var(--success)' : 'var(--danger)'};">${algo.stable ? 'Yes' : 'No'}</span></div>
          </div>
        </div>
        <div class="algo-card-footer">
          <button class="btn-card primary" onclick="App.openVisualizerFor('${algo.id}')">
            <svg width="14" height="14" viewBox="0 0 24 24" fill="currentColor"><polygon points="5 3 19 12 5 21 5 3"/></svg>
            Visualize
          </button>
          <button class="btn-card" onclick="App.openStudyGuideFor('${algo.id}')">
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20"/><path d="M6.5 2H20v20H6.5A2.5 2.5 0 0 1 4 19.5v-15A2.5 2.5 0 0 1 6.5 2z"/></svg>
            Study Guide
          </button>
        </div>
      `;
      grid.appendChild(card);
    });
  },

  bindSearchAndFilters() {
    const searchInput = document.getElementById('home-search-input');
    const pills = document.querySelectorAll('.category-pills .pill-btn');

    let activeFilter = 'all';

    const filterHandler = () => {
      const query = (searchInput ? searchInput.value : '').toLowerCase().trim();
      const filtered = this.algorithms.filter(algo => {
        const matchesQuery = algo.name.toLowerCase().includes(query) ||
                             algo.shortDescription.toLowerCase().includes(query) ||
                             algo.category.toLowerCase().includes(query);

        if (!matchesQuery) return false;

        if (activeFilter === 'all') return true;
        if (activeFilter === 'stable') return algo.stable === true;
        return algo.category.toLowerCase().includes(activeFilter);
      });
      this.renderHomeCards(filtered);
    };

    if (searchInput) {
      searchInput.addEventListener('input', filterHandler);
    }

    pills.forEach(pill => {
      pill.addEventListener('click', () => {
        pills.forEach(p => p.classList.remove('active'));
        pill.classList.add('active');
        activeFilter = pill.dataset.filter;
        filterHandler();
      });
    });
  },

  populateVisualizerDropdown() {
    const select = document.getElementById('visualizer-algo-select');
    if (!select) return;

    select.innerHTML = '';
    this.algorithms.forEach(algo => {
      const opt = document.createElement('option');
      opt.value = algo.id;
      opt.textContent = `${algo.name} (${algo.category})`;
      select.appendChild(opt);
    });

    select.value = this.currentAlgorithmId;
    select.addEventListener('change', (e) => {
      this.currentAlgorithmId = e.target.value;
      this.loadAlgorithmForVisualization(this.currentAlgorithmId);
    });
  },

  bindVisualizerToolbar() {
    const sizeSlider = document.getElementById('size-slider');
    if (sizeSlider) {
      sizeSlider.addEventListener('change', (e) => {
        const size = parseInt(e.target.value, 10);
        const label = document.getElementById('size-value-label');
        if (label) label.textContent = size;
        this.generatePresetArray('random', size);
        this.loadAlgorithmForVisualization(this.currentAlgorithmId);
      });
    }

    document.querySelectorAll('.preset-btn[data-preset]').forEach(btn => {
      btn.addEventListener('click', () => {
        const preset = btn.dataset.preset;
        this.generatePresetArray(preset);
        this.loadAlgorithmForVisualization(this.currentAlgorithmId);
      });
    });

    const customModalBtn = document.getElementById('btn-custom-array-modal');
    if (customModalBtn) {
      customModalBtn.addEventListener('click', () => {
        const modal = document.getElementById('custom-array-modal');
        const textarea = document.getElementById('custom-array-input');
        if (textarea) textarea.value = this.currentArray.join(', ');
        if (modal) modal.classList.add('active');
      });
    }


    const newArrayBtn = document.getElementById('btn-new-random-array');
    if (newArrayBtn) {
      newArrayBtn.addEventListener('click', () => {
        this.generatePresetArray('random');
        this.loadAlgorithmForVisualization(this.currentAlgorithmId);
      });
    }
  },

  generatePresetArray(preset, customSize = null) {
    const sizeSlider = document.getElementById('size-slider');
    const size = customSize || (sizeSlider ? parseInt(sizeSlider.value, 10) : 24);
    let arr = [];

    if (preset === 'random') {
      for (let i = 0; i < size; i++) arr.push(Math.floor(Math.random() * 95) + 5);
    } else if (preset === 'reversed') {
      for (let i = size; i >= 1; i--) arr.push(i * 4);
    } else if (preset === 'nearly_sorted') {
      for (let i = 1; i <= size; i++) arr.push(i * 4);
      for (let k = 0; k < Math.max(1, Math.floor(size / 8)); k++) {
        const a = Math.floor(Math.random() * size);
        const b = Math.floor(Math.random() * size);
        const tmp = arr[a]; arr[a] = arr[b]; arr[b] = tmp;
      }
    } else if (preset === 'few_unique') {
      const vals = [15, 35, 60, 85];
      for (let i = 0; i < size; i++) arr.push(vals[Math.floor(Math.random() * vals.length)]);
    } else {
      for (let i = 0; i < size; i++) arr.push(Math.floor(Math.random() * 95) + 5);
    }

    this.currentArray = arr;
  },

  async loadAlgorithmForVisualization(algoId) {
    this.visualizationRequestId = (this.visualizationRequestId || 0) + 1;
    const reqId = this.visualizationRequestId;

    // Immediately stop current animation and clear stale events to prevent race conditions
    if (this.visualizer) {
      this.visualizer.pause();
      this.visualizer.events = [];
      this.visualizer.currentStep = 0;
      this.visualizer.updateControlsState();
      if (this.visualizer.timelineStatus) {
        this.visualizer.timelineStatus.textContent = 'Preparing...';
      }
    }

    CodePanel.setOrder(this.currentOrder);
    const meta = this.algorithms.find(a => a.id === algoId);
    if (meta) {
      CodePanel.loadMetadata(meta);
    } else {
      try {
        const fetchedMeta = await API.getAlgorithm(algoId);
        if (reqId === this.visualizationRequestId) {
          CodePanel.loadMetadata(fetchedMeta);
        }
      } catch (e) {
        console.warn('Could not fetch metadata for', algoId, e);
      }
    }

    try {
      const response = await API.sort(algoId, this.currentArray, 'NORMAL', this.currentOrder);
      if (reqId !== this.visualizationRequestId) {
        // Discard stale response since a newer request was dispatched
        return;
      }
      this.visualizer.loadData(response);
    } catch (err) {
      if (reqId !== this.visualizationRequestId) return;
      console.warn(`Initial sort fetch failed for ${algoId}: ${err.message}. Retrying...`);
      // Retry after short delay in case server was reloading
      try {
        await new Promise(r => setTimeout(r, 600));
        if (reqId !== this.visualizationRequestId) return;
        const retryResponse = await API.sort(algoId, this.currentArray, 'NORMAL', this.currentOrder);
        if (reqId === this.visualizationRequestId) {
          this.visualizer.loadData(retryResponse);
        }
      } catch (retryErr) {
        if (reqId === this.visualizationRequestId) {
          this.showToast(`Error executing ${algoId}: ${retryErr.message}`, 'error');
        }
      }
    }
  },

  openVisualizerFor(algoId) {
    this.currentAlgorithmId = algoId;
    const select = document.getElementById('visualizer-algo-select');
    if (select) select.value = algoId;
    this.switchView('view-visualizer');
    this.loadAlgorithmForVisualization(algoId);
  },

  openStudyGuideFor(algoId) {
    this.switchView('view-study');
    this.selectStudyAlgorithm(algoId);
  },

  initStudyGuide() {
    const navList = document.getElementById('study-nav-list');
    if (!navList) return;

    navList.innerHTML = '';
    this.algorithms.forEach((algo, idx) => {
      const li = document.createElement('li');
      li.className = 'study-nav-item';
      li.innerHTML = `
        <button data-id="${algo.id}" class="${idx === 0 ? 'active' : ''}">
          ${algo.name}
        </button>
      `;
      navList.appendChild(li);

      li.querySelector('button').addEventListener('click', () => {
        this.selectStudyAlgorithm(algo.id);
      });
    });

    if (this.algorithms.length > 0) {
      this.selectStudyAlgorithm(this.algorithms[0].id);
    }
  },

  selectStudyAlgorithm(algoId) {
    document.querySelectorAll('.study-nav-item button').forEach(btn => {
      if (btn.dataset.id === algoId) btn.classList.add('active');
      else btn.classList.remove('active');
    });

    const algo = this.algorithms.find(a => a.id === algoId);
    const content = document.getElementById('study-content-container');
    if (!content || !algo) return;

    content.innerHTML = `
      <div class="study-header">
        <h2 class="study-title">${algo.name}</h2>
        <div class="study-tags">
          <span class="tag-badge accent">${algo.category}</span>
          <span class="tag-badge">Difficulty: ${algo.difficulty}</span>
          <span class="tag-badge">Stable: ${algo.stable ? 'Yes' : 'No'}</span>
          <span class="tag-badge">In-Place: ${algo.inPlace ? 'Yes' : 'No'}</span>
        </div>
      </div>

      <div class="study-section">
        <h3 class="section-title">Core Intuition &amp; Concept</h3>
        <p style="color: var(--text-secondary); line-height: 1.7; font-size: 1.05rem;">
          ${algo.detailedDescription}
        </p>
      </div>

      <div class="study-section">
        <h3 class="section-title">Complexity Analysis (Big-O)</h3>
        <table class="complexity-table">
          <thead>
            <tr>
              <th>Best-Case Time</th>
              <th>Average-Case Time</th>
              <th>Worst-Case Time</th>
              <th>Auxiliary Space</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td><code>${algo.bestTimeComplexity}</code></td>
              <td><code>${algo.averageTimeComplexity}</code></td>
              <td><code>${algo.worstTimeComplexity}</code></td>
              <td><code>${algo.spaceComplexity}</code></td>
            </tr>
          </tbody>
        </table>
      </div>

      <div class="study-section">
        <div class="pro-con-grid">
          <div class="pro-card">
            <h4 style="color: var(--success); font-weight: 700; margin-bottom: 0.75rem;">Key Advantages</h4>
            <ul class="bullet-list">
              ${algo.advantages.map(adv => `<li>${adv}</li>`).join('')}
            </ul>
          </div>
          <div class="con-card">
            <h4 style="color: var(--danger); font-weight: 700; margin-bottom: 0.75rem;">Limitations &amp; Trade-offs</h4>
            <ul class="bullet-list">
              ${algo.disadvantages.map(dis => `<li>${dis}</li>`).join('')}
            </ul>
          </div>
        </div>
      </div>

      <div class="study-section">
        <h3 class="section-title">Recommended Use Cases</h3>
        <ul class="bullet-list">
          ${algo.recommendedUseCases.map(uc => `<li>${uc}</li>`).join('')}
        </ul>
      </div>

      <div class="study-section">
        <h3 class="section-title">Common Traps &amp; Nuances</h3>
        <ul class="bullet-list">
          ${algo.commonMistakes.map(cm => `<li>${cm}</li>`).join('')}
        </ul>
      </div>

      <div class="study-section">
        <h3 class="section-title">Frequently Asked Exam Questions</h3>
        <div class="faq-container">
          ${algo.examQuestions.map((q, qIdx) => `
            <div class="accordion-item" id="faq-item-${qIdx}">
              <div class="accordion-header" onclick="this.parentElement.classList.toggle('open')">
                <span>${q}</span>
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><polyline points="6 9 12 15 18 9"></polyline></svg>
              </div>
              <div class="accordion-body">
                Detailed answer and analysis discussed in lecture notes. Walk through the algorithm step-by-step with an array of size 5 to verify each mechanical transition.
              </div>
            </div>
          `).join('')}
        </div>
      </div>

      <div class="study-section">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 0.75rem; flex-wrap: wrap; gap: 0.5rem;">
          <h3 class="section-title" style="margin-bottom: 0;">Java 21 &amp; Python 3 Source Implementations</h3>
          <span class="order-status-badge">${this.currentOrder === 'DESCENDING' ? 'Descending Order' : 'Ascending Order'}</span>
        </div>
        <div style="display: flex; gap: 0.5rem; margin-bottom: 0.75rem;">
          <button class="preset-btn active" id="study-show-java" onclick="document.getElementById('study-code-java').style.display='block'; document.getElementById('study-code-py').style.display='none'; this.classList.add('active'); document.getElementById('study-show-py').classList.remove('active');">Java 21</button>
          <button class="preset-btn" id="study-show-py" onclick="document.getElementById('study-code-py').style.display='block'; document.getElementById('study-code-java').style.display='none'; this.classList.add('active'); document.getElementById('study-show-java').classList.remove('active');">Python 3</button>
        </div>
        <div class="code-block-container" id="study-code-java">
          <pre class="code-display">${((this.currentOrder === 'DESCENDING' ? (algo.javaCodeDescending || algo.javaCode) : algo.javaCode) || '').replace(/</g, '&lt;').replace(/>/g, '&gt;')}</pre>
        </div>
        <div class="code-block-container" id="study-code-py" style="display: none;">
          <pre class="code-display">${((this.currentOrder === 'DESCENDING' ? (algo.pythonCodeDescending || algo.pythonCode) : algo.pythonCode) || '# Python code available').replace(/</g, '&lt;').replace(/>/g, '&gt;')}</pre>
        </div>
      </div>
    `;
  },

  initModals() {
    const modal = document.getElementById('custom-array-modal');
    const closeBtn = document.getElementById('modal-close-btn');
    const applyBtn = document.getElementById('modal-apply-btn');
    const textarea = document.getElementById('custom-array-input');

    if (closeBtn && modal) {
      closeBtn.addEventListener('click', () => modal.classList.remove('active'));
    }

    if (applyBtn && modal && textarea) {
      applyBtn.addEventListener('click', () => {
        const raw = textarea.value;
        const parsed = raw.split(/[\s,]+/)
          .map(s => s.trim())
          .filter(s => s.length > 0)
          .map(s => parseInt(s, 10))
          .filter(n => !isNaN(n));

        if (parsed.length === 0) {
          alert('Please enter at least one valid integer number.');
          return;
        }

        if (parsed.length > 100) {
          alert('Maximum 100 elements allowed for animation clarity.');
          return;
        }

        this.currentArray = parsed;
        modal.classList.remove('active');
        this.loadAlgorithmForVisualization(this.currentAlgorithmId);
        this.showToast(`Custom array of ${parsed.length} elements loaded.`, 'success');
      });
    }
  },

  showToast(message, type = 'info') {
    const container = document.getElementById('toast-container');
    if (!container) return;

    const toast = document.createElement('div');
    toast.className = `toast ${type}`;
    const iconSvg = type === 'error'
      ? `<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><circle cx="12" cy="12" r="10"></circle><line x1="12" y1="8" x2="12" y2="12"></line><line x1="12" y1="16" x2="12.01" y2="16"></line></svg>`
      : `<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><polyline points="20 6 9 17 4 12"></polyline></svg>`;

    toast.innerHTML = `
      <span>${iconSvg}</span>
      <span>${message}</span>
    `;
    container.appendChild(toast);

    setTimeout(() => {
      toast.style.opacity = '0';
      toast.style.transform = 'translateY(10px)';
      setTimeout(() => toast.remove(), 250);
    }, 3500);
  }
};

window.App = App;
document.addEventListener('DOMContentLoaded', () => App.init());
