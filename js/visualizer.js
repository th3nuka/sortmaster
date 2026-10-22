/**
 * SortMaster Animation Engine & Block Visualizer
 * Technical Navy Aesthetic, Rectangular Number Blocks, Dynamic SVG Comparison Arcs,
 * Algorithm-Specific Badges, Zero Emojis
 */

class SortingVisualizer {
  constructor(barsContainerId) {
    this.container = document.getElementById(barsContainerId);
    this.stageCard = document.getElementById('stage-card');
    this.svgLayer = document.getElementById('svg-connections-layer');
    this.algoShelf = document.getElementById('algo-specific-shelf');

    this.events = [];
    this.currentStep = 0;
    this.isPlaying = false;
    this.timer = null;
    this.speedMs = 300;
    this.initialArray = [];
    this.currentResponse = null;

    // DOM UI elements
    this.playBtn = document.getElementById('play-btn');
    this.prevBtn = document.getElementById('prev-btn');
    this.nextBtn = document.getElementById('next-btn');
    this.resetBtn = document.getElementById('reset-btn');
    this.jumpEndBtn = document.getElementById('jump-end-btn');
    this.restartBtn = document.getElementById('btn-restart');

    this.comparisonsDisplay = document.getElementById('metric-comparisons');
    this.swapsDisplay = document.getElementById('metric-swaps');
    this.writesDisplay = document.getElementById('metric-writes');
    this.stepCounterDisplay = document.getElementById('metric-step');
    this.totalStepsDisplay = document.getElementById('metric-total-steps');
    this.progressBar = document.getElementById('step-progress-fill');
    this.timelineStatus = document.getElementById('timeline-status');
    this.explanationText = document.getElementById('explanation-text');
    this.explanationBadge = document.getElementById('explanation-badge');

    this.initControls();
    this.updateControlsState();
  }

  initControls() {
    if (this.playBtn) this.playBtn.addEventListener('click', () => this.togglePlay());
    if (this.prevBtn) this.prevBtn.addEventListener('click', () => this.stepBackward());
    if (this.nextBtn) this.nextBtn.addEventListener('click', () => this.stepForward());
    if (this.resetBtn) this.resetBtn.addEventListener('click', () => this.resetToStart());
    if (this.jumpEndBtn) this.jumpEndBtn.addEventListener('click', () => this.jumpToEnd());
    if (this.restartBtn) this.restartBtn.addEventListener('click', () => this.restart());

    const speedSlider = document.getElementById('speed-slider');
    if (speedSlider) {
      speedSlider.addEventListener('input', (e) => {
        const val = parseInt(e.target.value, 10);
        // Map 1 (fast: 30ms) to 100 (slow: 850ms)
        this.speedMs = Math.max(30, Math.round(850 - (val * 8.2)));
        const speedValLabel = document.getElementById('speed-value-label');
        if (speedValLabel) speedValLabel.textContent = `${(1000 / this.speedMs).toFixed(1)}x`;
        if (this.isPlaying) {
          this.pause();
          this.play();
        }
      });
    }

    // Handle window resize to re-render SVG arc positions
    window.addEventListener('resize', () => {
      if (this.events.length > 0 && this.currentStep < this.events.length) {
        const currentEvent = this.events[this.currentStep];
        this.renderSvgConnectors(currentEvent.indices, currentEvent.eventType);
      }
    });
  }

  loadData(response) {
    this.pause();
    this.currentResponse = response;
    this.events = response.events || [];
    this.initialArray = response.initialArray || [];
    this.currentStep = 0;

    if (this.totalStepsDisplay) {
      this.totalStepsDisplay.textContent = this.events.length;
    }

    if (this.timelineStatus) {
      this.timelineStatus.textContent = this.events.length > 0 ? 'Ready' : 'Idle';
    }

    // Render initial array state
    if (this.events.length > 0) {
      this.renderStep(0);
    } else {
      this.renderBlocks(this.initialArray);
      this.clearSvgConnectors();
      this.clearAlgoShelf();
    }
    this.updateControlsState();
  }

  renderBlocks(array, indices = [], eventType = null, pivotIndex = null, activeRange = null, sortedIndices = []) {
    if (!this.container) return;
    this.container.innerHTML = '';

    if (!array || array.length === 0) {
      this.container.innerHTML = '<div style="color: var(--text-muted); margin: auto;">Array is empty</div>';
      return;
    }

    const minVal = Math.min(...array);
    const maxVal = Math.max(...array);
    const range = (maxVal - minVal) === 0 ? 1 : (maxVal - minVal);
    const minHeightPx = 28;
    const maxHeightPx = 250;
    const showValues = array.length <= 45;
    const showIndices = array.length <= 32;

    const indicesSet = new Set(indices || []);
    const sortedSet = new Set(sortedIndices || []);

    array.forEach((val, idx) => {
      const wrapper = document.createElement('div');
      wrapper.className = 'v-block-wrapper';
      wrapper.dataset.index = idx;

      // Active range highlight (e.g. Merge Sort sub-arrays)
      if (activeRange && activeRange.length === 2) {
        if (idx >= activeRange[0] && idx <= activeRange[1]) {
          wrapper.classList.add('active-range');
        }
      }

      const block = document.createElement('div');
      block.className = 'v-block';

      // Normalized height proportion
      const normalizedRatio = (val - minVal) / range;
      const heightPx = Math.round(minHeightPx + (normalizedRatio * (maxHeightPx - minHeightPx)));
      block.style.height = `${heightPx}px`;

      // Assign styling states
      if (idx === pivotIndex) {
        block.classList.add('pivot');
      } else if (indicesSet.has(idx)) {
        if (eventType === 'SWAP') {
          block.classList.add('swapping');
        } else if (eventType === 'COMPARE') {
          block.classList.add('comparing');
        } else if (eventType === 'WRITE' || eventType === 'INSERT' || eventType === 'MERGE') {
          block.classList.add('swapping');
        }
      } else if (sortedSet.has(idx)) {
        block.classList.add('sorted');
      }

      // Value label inside/top of block
      if (showValues) {
        const valLabel = document.createElement('span');
        valLabel.className = 'v-block-val';
        valLabel.textContent = val;
        block.appendChild(valLabel);
      }

      wrapper.appendChild(block);

      // Array index label below block
      if (showIndices) {
        const idxLabel = document.createElement('span');
        idxLabel.className = 'v-block-idx';
        idxLabel.textContent = idx;
        wrapper.appendChild(idxLabel);
      }

      this.container.appendChild(wrapper);
    });
  }

  renderSvgConnectors(indices, eventType) {
    if (!this.svgLayer || !this.stageCard || !this.container) return;
    this.svgLayer.innerHTML = '';

    if (!indices || indices.length < 2) return;
    if (eventType !== 'COMPARE' && eventType !== 'SWAP') return;

    const idx1 = indices[0];
    const idx2 = indices[1];
    const el1 = this.container.querySelector(`.v-block-wrapper[data-index="${idx1}"]`);
    const el2 = this.container.querySelector(`.v-block-wrapper[data-index="${idx2}"]`);
    if (!el1 || !el2) return;

    const stageRect = this.stageCard.getBoundingClientRect();
    const rect1 = el1.getBoundingClientRect();
    const rect2 = el2.getBoundingClientRect();

    const block1 = el1.querySelector('.v-block');
    const block2 = el2.querySelector('.v-block');
    const bRect1 = block1 ? block1.getBoundingClientRect() : rect1;
    const bRect2 = block2 ? block2.getBoundingClientRect() : rect2;

    // Coordinate centers relative to stage
    const x1 = rect1.left + (rect1.width / 2) - stageRect.left;
    const y1 = bRect1.top - stageRect.top;
    const x2 = rect2.left + (rect2.width / 2) - stageRect.left;
    const y2 = bRect2.top - stageRect.top;

    const dx = Math.abs(x2 - x1);
    const arcHeight = Math.min(100, Math.max(25, dx * 0.38));
    const midX = (x1 + x2) / 2;
    const peakY = Math.min(y1, y2) - arcHeight;

    const isSwap = eventType === 'SWAP';
    const strokeColor = isSwap ? '#ef4444' : '#38bdf8';
    const glowFilter = isSwap ? 'url(#glow-red)' : 'url(#glow-cyan)';

    // Curved connector arc path
    const path = document.createElementNS('http://www.w3.org/2000/svg', 'path');
    path.setAttribute('d', `M ${x1} ${y1} Q ${midX} ${peakY} ${x2} ${y2}`);
    path.setAttribute('fill', 'none');
    path.setAttribute('stroke', strokeColor);
    path.setAttribute('stroke-width', '2.5');
    path.setAttribute('stroke-linecap', 'round');
    path.setAttribute('filter', glowFilter);
    this.svgLayer.appendChild(path);

    // Glowing connection node dots at element tops
    [ { x: x1, y: y1 }, { x: x2, y: y2 } ].forEach(pt => {
      const circle = document.createElementNS('http://www.w3.org/2000/svg', 'circle');
      circle.setAttribute('cx', pt.x);
      circle.setAttribute('cy', pt.y);
      circle.setAttribute('r', '4');
      circle.setAttribute('fill', strokeColor);
      circle.setAttribute('filter', glowFilter);
      this.svgLayer.appendChild(circle);
    });

    // Central operation tag on arc peak
    const tag = document.createElementNS('http://www.w3.org/2000/svg', 'text');
    tag.setAttribute('x', midX);
    tag.setAttribute('y', peakY - 6);
    tag.setAttribute('text-anchor', 'middle');
    tag.setAttribute('fill', strokeColor);
    tag.setAttribute('font-family', 'JetBrains Mono, monospace');
    tag.setAttribute('font-size', '11');
    tag.setAttribute('font-weight', '700');
    tag.textContent = isSwap ? '< SWAP >' : '< COMPARE >';
    this.svgLayer.appendChild(tag);
  }

  clearSvgConnectors() {
    if (this.svgLayer) this.svgLayer.innerHTML = '';
  }

  renderAlgoShelf(event) {
    if (!this.algoShelf) return;
    this.algoShelf.innerHTML = '';

    const badges = [];

    // QuickSort Pivot Indicator
    if (event.pivotIndex != null && event.array && event.pivotIndex < event.array.length) {
      badges.push(`
        <div class="algo-badge pivot-badge">
          <span>PIVOT: [${event.pivotIndex}] = ${event.array[event.pivotIndex]}</span>
        </div>
      `);
    }

    // Active Range Indicator (Merge Sort partitions, binary bounds)
    if (event.activeRange && event.activeRange.length === 2) {
      badges.push(`
        <div class="algo-badge range-badge">
          <span>RANGE: [${event.activeRange[0]} ... ${event.activeRange[1]}]</span>
        </div>
      `);
    }

    if (badges.length > 0) {
      this.algoShelf.innerHTML = badges.join('');
    }
  }

  clearAlgoShelf() {
    if (this.algoShelf) this.algoShelf.innerHTML = '';
  }

  renderStep(index) {
    if (index < 0 || index >= this.events.length) return;
    this.currentStep = index;
    const event = this.events[index];

    this.renderBlocks(
      event.array,
      event.indices,
      event.eventType,
      event.pivotIndex,
      event.activeRange,
      event.sortedIndices
    );

    // Render dynamic SVG comparison / swap arc
    this.renderSvgConnectors(event.indices, event.eventType);

    // Render algorithm specific partition / pivot shelf
    this.renderAlgoShelf(event);

    // Compute live telemetry metrics up to current step
    this.updateMetrics(index);

    // Update message narrative
    if (this.explanationText) {
      this.explanationText.textContent = event.message || 'Processing algorithm step...';
    }
    if (this.explanationBadge) {
      this.explanationBadge.className = `event-badge ${event.eventType ? event.eventType.toLowerCase() : ''}`;
      this.explanationBadge.textContent = event.eventType || 'STEP';
    }

    // Update timeline status
    if (this.timelineStatus) {
      if (this.currentStep === 0) this.timelineStatus.textContent = 'At Start';
      else if (this.currentStep >= this.events.length - 1) this.timelineStatus.textContent = 'Completed';
      else this.timelineStatus.textContent = `Step ${this.currentStep + 1} of ${this.events.length}`;
    }

    // Synchronize Code Panel
    if (window.CodePanel && typeof window.CodePanel.highlightStep === 'function') {
      window.CodePanel.highlightStep(event.conceptualStep);
    }

    this.updateControlsState();
  }

  updateMetrics(stepIdx) {
    let comps = 0;
    let swaps = 0;
    let writes = 0;

    for (let i = 0; i <= stepIdx; i++) {
      const type = this.events[i].eventType;
      if (type === 'COMPARE') comps++;
      else if (type === 'SWAP') {
        swaps++;
        writes += 2;
      } else if (type === 'WRITE' || type === 'INSERT' || type === 'MERGE') {
        writes++;
      }
    }

    if (this.comparisonsDisplay) this.comparisonsDisplay.textContent = comps;
    if (this.swapsDisplay) this.swapsDisplay.textContent = swaps;
    if (this.writesDisplay) this.writesDisplay.textContent = writes;

    if (this.stepCounterDisplay) {
      this.stepCounterDisplay.textContent = stepIdx + 1;
    }

    if (this.progressBar && this.events.length > 0) {
      const pct = Math.round(((stepIdx + 1) / this.events.length) * 100);
      this.progressBar.style.width = `${pct}%`;
    }
  }

  togglePlay() {
    if (this.isPlaying) {
      this.pause();
    } else {
      this.play();
    }
  }

  play() {
    if (this.events.length === 0) return;
    if (this.timer) {
      clearTimeout(this.timer);
      this.timer = null;
    }
    if (this.currentStep >= this.events.length - 1) {
      this.currentStep = 0;
      this.renderStep(0);
    }
    this.isPlaying = true;
    this.updatePlayBtnIcon();

    const tick = () => {
      if (!this.isPlaying) return;
      if (this.currentStep < this.events.length - 1) {
        this.renderStep(this.currentStep + 1);
        this.timer = setTimeout(tick, this.speedMs);
      } else {
        this.pause();
      }
    };

    this.timer = setTimeout(tick, this.speedMs);
  }

  pause() {
    this.isPlaying = false;
    if (this.timer) {
      clearTimeout(this.timer);
      this.timer = null;
    }
    this.updatePlayBtnIcon();
  }

  stepForward() {
    this.pause();
    if (this.currentStep < this.events.length - 1) {
      this.renderStep(this.currentStep + 1);
    }
  }

  stepBackward() {
    this.pause();
    if (this.currentStep > 0) {
      this.renderStep(this.currentStep - 1);
    }
  }

  resetToStart() {
    this.pause();
    if (this.events.length > 0) {
      this.renderStep(0);
    }
  }

  restart() {
    this.pause();
    if (this.events.length > 0) {
      this.currentStep = 0;
      this.renderStep(0);
      this.play();
    }
  }

  jumpToEnd() {
    this.pause();
    if (this.events.length > 0) {
      this.renderStep(this.events.length - 1);
    }
  }

  updatePlayBtnIcon() {
    if (!this.playBtn) return;
    if (this.isPlaying) {
      this.playBtn.innerHTML = `
        <svg width="18" height="18" viewBox="0 0 24 24" fill="currentColor">
          <rect x="6" y="4" width="4" height="16"/>
          <rect x="14" y="4" width="4" height="16"/>
        </svg>
      `;
      this.playBtn.title = 'Pause Animation';
    } else {
      this.playBtn.innerHTML = `
        <svg width="18" height="18" viewBox="0 0 24 24" fill="currentColor">
          <polygon points="5 3 19 12 5 21 5 3"/>
        </svg>
      `;
      this.playBtn.title = 'Play Animation';
    }
  }

  updateControlsState() {
    const hasEvents = this.events && this.events.length > 0;
    const isAtStart = !hasEvents || this.currentStep <= 0;
    const isAtEnd = !hasEvents || this.currentStep >= this.events.length - 1;

    if (this.playBtn) this.playBtn.disabled = !hasEvents;
    if (this.prevBtn) this.prevBtn.disabled = isAtStart;
    if (this.nextBtn) this.nextBtn.disabled = isAtEnd;
    if (this.resetBtn) this.resetBtn.disabled = isAtStart;
    if (this.jumpEndBtn) this.jumpEndBtn.disabled = isAtEnd;
  }
}

window.SortingVisualizer = SortingVisualizer;
